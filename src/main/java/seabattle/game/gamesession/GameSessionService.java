package seabattle.game.gamesession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.CloseStatus;
import seabattle.authorization.service.UserService;
import seabattle.authorization.views.UserView;
import seabattle.game.field.Cell;
import seabattle.game.field.CellStatus;
import seabattle.game.messages.*;
import seabattle.game.player.Player;
import seabattle.game.player.PlayerAI;
import seabattle.game.ship.Ship;
import seabattle.msgsystem.Message;
import seabattle.websocket.WebSocketService;

import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

@SuppressWarnings({"unused", "WeakerAccess", "SpringAutowiredFieldsWarningInspection"})
@Service
public class GameSessionService {
    private static final Logger LOGGER = LoggerFactory.getLogger(GameSessionService.class);

    @NotNull
    private final Map<Long, GameSession> gameSessions = new HashMap<>();


    @NotNull
    private final ArrayDeque<Player> waitingPlayers = new ArrayDeque<>();

    @NotNull
    private final WebSocketService webSocketService;

    @Autowired
    private UserService dbUsers;

    public GameSessionService(@NotNull WebSocketService webSocketService) {
        this.webSocketService = webSocketService;
    }

    public Boolean isPlaying(@NotNull Long userId) {
        return gameSessions.containsKey(userId);
    }

    public Map<Long, GameSession> getGameSessions() {
        return gameSessions;
    }

    public Set<GameSession> getSessions() {
        final Set<GameSession> result = new HashSet<>();
        result.addAll(gameSessions.values());
        return result;
    }

    public Boolean sessionAlive(@NotNull GameSession session) {
        return webSocketService.isConnected(session.getPlayer1().getPlayerId())
                && webSocketService.isConnected(session.getPlayer2().getPlayerId());
    }

    public GameSession getGameSession(Long userId) {
        return gameSessions.get(userId);
    }

    public void addWaitingPlayer(@NotNull Player player) {
        waitingPlayers.add(player);
        final MsgYouInQueue msgYouInQueue = new MsgYouInQueue(player.getUsername());
        try {
            webSocketService.sendMessage(player.getPlayerId(), msgYouInQueue);
        } catch (IOException ex) {
            try {
                webSocketService.sendMessage(player.getPlayerId(), new MsgError("Could not get in the queue "));
            } catch (IOException sendEx) {
                LOGGER.warn("Unnable to send message");
            }
            webSocketService.closeSession(player.getPlayerId(), CloseStatus.SERVER_ERROR);
        }
        Integer numberOfPlayersInSession = 2;
        if (waitingPlayers.size() >= numberOfPlayersInSession) {
            createSession(waitingPlayers.pollFirst(), waitingPlayers.pollFirst());
        }
    }


    public void createSession(@NotNull Player player1, @NotNull Player player2) {

        final GameSession gameSession = new GameSession(player1, player2, this);

        gameSessions.put(player1.getPlayerId(), gameSession);
        gameSessions.put(player2.getPlayerId(), gameSession);


        try {
            final MsgLobbyCreated initMessage1 = new MsgLobbyCreated(player1.getUsername());
            webSocketService.sendMessage(player2.getPlayerId(), initMessage1);
            final MsgLobbyCreated initMessage2 = new MsgLobbyCreated(player2.getUsername());
            webSocketService.sendMessage(player1.getPlayerId(), initMessage2);
        } catch (IOException ex) {
            final String error = "Failed to create session with " + player1.getPlayerId()
                    + " and " + player2.getPlayerId();
            try {
                webSocketService.sendMessage(player1.getPlayerId(), new MsgError(error));
                webSocketService.sendMessage(player2.getPlayerId(), new MsgError(error));
            } catch (IOException sendEx) {
                LOGGER.warn("Unable to send message");
            }
            webSocketService.closeSession(player1.getPlayerId(), CloseStatus.SERVER_ERROR);
            webSocketService.closeSession(player2.getPlayerId(), CloseStatus.SERVER_ERROR);

            LOGGER.warn(error, ex);
        }
    }

    public void tryStartGame(@NotNull GameSession gameSession) {

        if (gameSession.bothFieldsAccepted() == Boolean.FALSE) {
            return;
        }
        try {
            Player damagedPlayer = chooseDamagedPlayer(gameSession.getPlayer1(), gameSession.getPlayer2());
            gameSession.setDamagedSide(damagedPlayer);
            MsgGameStarted gameStarted1 = createGameStartedMessage(gameSession.getPlayer1(), damagedPlayer);
            webSocketService.sendMessage(gameSession.getPlayer1Id(), gameStarted1);
            MsgGameStarted gameStarted2 = createGameStartedMessage(gameSession.getPlayer2(), damagedPlayer);
            webSocketService.sendMessage(gameSession.getPlayer2Id(), gameStarted2);
            gameSession.toGamePhase();
        } catch (IOException ex) {
            LOGGER.warn("Can't start game! " + ex);
        }
    }

    private MsgGameStarted createGameStartedMessage(@NotNull Player currentPlayer, @NotNull Player damagedPlayer) {
        if (!currentPlayer.equals(damagedPlayer)) {
            return new MsgGameStarted(Boolean.TRUE);
        }
        return new MsgGameStarted(Boolean.FALSE);
    }

    private Player chooseDamagedPlayer(@NotNull Player player1, @NotNull Player player2) {
        final Player[] players = {player1, player2};
        Integer secondPlayerPosition = ThreadLocalRandom.current().nextInt(0, 2);
        return players[secondPlayerPosition];
    }

    public void endSession(@NotNull GameSession gameSession) {
        try {
            calculationScore(gameSession);
        } catch (IllegalStateException ex) {
            LOGGER.warn(ex.getMessage());
        }
        try {
            MsgEndGame endGame = createMsgEndGame(gameSession, gameSession.getPlayer1());
            webSocketService.sendMessage(gameSession.getPlayer1Id(), endGame);

            endGame = createMsgEndGame(gameSession, gameSession.getPlayer2());
            webSocketService.sendMessage(gameSession.getPlayer2Id(), endGame);
        } catch (IOException ex) {
            LOGGER.warn("Failed to send MsgEndGame to user " + gameSession.getPlayer1().getPlayerId(), ex);
        } catch (IllegalStateException ex) {
            LOGGER.warn(ex.getMessage());
        }
        gameSessions.remove(gameSession.getPlayer1Id());
        gameSessions.remove(gameSession.getPlayer2Id());
        webSocketService.closeSession(gameSession.getPlayer1Id(), CloseStatus.NORMAL);
        webSocketService.closeSession(gameSession.getPlayer2Id(), CloseStatus.NORMAL);
    }

    private void calculationScore(@NotNull GameSession gameSession) throws IllegalStateException {
        final Player winnerPlayer = gameSession.getWinner();
        final Player loserPlayer;
        if (winnerPlayer.equals(gameSession.getPlayer1())) {
            loserPlayer = gameSession.getPlayer2();
        } else {
            loserPlayer = gameSession.getPlayer1();
        }

        if (winnerPlayer.getUser() == null) {
            return;
        }

        final Integer standartScoreInc = 100;
        Integer scoreInc;
        Integer newScore;

        if (loserPlayer.getUser() == null
                || loserPlayer.getScore() < winnerPlayer.getScore()) {
            scoreInc = standartScoreInc;
        } else {
            final float lossFactor = (float) 0.1;
            scoreInc = Math.round(loserPlayer.getScore() * lossFactor);
            newScore = loserPlayer.getScore() - scoreInc;
            final UserView userView = loserPlayer.getUser();
            userView.setScore(newScore);
            if (dbUsers.setScore(userView) != null) {
                loserPlayer.setScore(newScore);
            } else {
                throw new IllegalStateException("Could not add an score! ");
            }
            scoreInc += standartScoreInc;
        }

        newScore = winnerPlayer.getScore() + scoreInc;
        final UserView userView = winnerPlayer.getUser();
        userView.setScore(newScore);
        if (dbUsers.setScore(userView) != null) {
            winnerPlayer.setScore(newScore);
        } else {
            throw new IllegalStateException("Could not add an score! ");
        }

    }

    private MsgEndGame createMsgEndGame(@NotNull GameSession gameSession, @NotNull Player current) {
        if (current == gameSession.getWinner()) {
            return new MsgEndGame(Boolean.TRUE, current.getScore());
        }
        return new MsgEndGame(Boolean.FALSE, current.getScore());
    }

    public void makeMove(@NotNull GameSession gameSession, @NotNull Cell cell) {
        if (gameSession.toGamePhase()) {
            try {
                webSocketService.sendMessage(gameSession.getPlayer1Id(),
                        new MsgError("Not Game phase! "));
                webSocketService.sendMessage(gameSession.getPlayer2Id(),
                        new MsgError("Not Game phase! "));
            } catch (IOException ex) {
                LOGGER.warn("Can't send message! ", ex);
            }
            return;
        }
        try {
            CellStatus cellStatus = gameSession.makeMove(cell);
            if (cellStatus == CellStatus.DESTRUCTED
                    && (gameSession.getStatus().equals(GameSessionStatus.WIN_P1)
                    || gameSession.getStatus().equals(GameSessionStatus.WIN_P2))) {
                endSession(gameSession);
            }
            Message resultMove = null;
            if (cellStatus == CellStatus.DESTRUCTED) {
                for (Ship ship: gameSession.getDamagedPlayer().getDeadShips()) {
                    if (ship.inShip(cell)) {
                        resultMove = new MsgShipIsDestroyed(ship, gameSession.getDamagedPlayer().getUsername());
                    }
                }
            } else {
                if (gameSession.getStatus().equals(GameSessionStatus.MOVE_P1)) {
                    resultMove = new MsgResultMove(cell, cellStatus, gameSession.getPlayer1().getUsername());
                } else {
                    resultMove = new MsgResultMove(cell, cellStatus, gameSession.getPlayer2().getUsername());
                }
            }
            try {
                webSocketService.sendMessage(gameSession.getPlayer1Id(), resultMove);
                webSocketService.sendMessage(gameSession.getPlayer2Id(), resultMove);
            } catch (IOException ex) {
                LOGGER.warn("Can't send message! ", ex);
            }

        } catch (IllegalArgumentException ex) {
            if (gameSession.getStatus().equals(GameSessionStatus.MOVE_P1)) {
                try {
                    webSocketService.sendMessage(gameSession.getPlayer1Id(), new MsgError("unacceptable move "));
                } catch (IOException sendEx) {
                    LOGGER.warn("Can't send message! ", ex);
                }

            } else {
                try {
                    webSocketService.sendMessage(gameSession.getPlayer2Id(), new MsgError("unacceptable move "));
                } catch (IOException sendEx) {
                    LOGGER.warn("Can't send message! ", ex);
                }
            }
            LOGGER.warn(ex.getMessage());
        } catch (IllegalStateException sEx) {
            LOGGER.warn(sEx.getMessage());
        }
    }

    public void dellWaitingPlayer(Player player) {
       if (waitingPlayers.contains(player)) {
           waitingPlayers.remove(player);
       }
    }

    public void sendPingMessage(@NotNull Long playerId) {
        try {
            webSocketService.sendMessage(playerId, new MsgPing());
        } catch (IOException ex) {
            LOGGER.warn("Can't send message! ", ex);
        }
    }

    public void sendGeneretedShips(@NotNull Long playerId) {
        MsgShipPosition shipsPosition = new MsgShipPosition();
        shipsPosition.setShips(PlayerAI.generateShips());
        try {
            webSocketService.sendMessage(playerId, shipsPosition);
        } catch (IOException ex) {
            LOGGER.warn("Can't send message! ", ex);
        }
    }
}