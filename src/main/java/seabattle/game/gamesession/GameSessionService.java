package seabattle.game.gamesession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.CloseStatus;
import seabattle.game.field.Cell;
import seabattle.game.field.CellStatus;
import seabattle.game.messages.*;
import seabattle.game.player.Player;
import seabattle.websocket.WebSocketService;

import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

@SuppressWarnings({"unused", "WeakerAccess"})
@Service
public class GameSessionService {
    private static final Logger LOGGER = LoggerFactory.getLogger(GameSessionService.class);

    @NotNull
    private final Map<Long, GameSession> gameSessions = new HashMap<>();

    @NotNull
    private final ArrayDeque<Player> waitingPlayers = new ArrayDeque<>();

    @NotNull
    private final WebSocketService webSocketService;

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
            final MsgLobbyCreated initMessage1 = new MsgLobbyCreated(player2.getUsername());
            webSocketService.sendMessage(player2.getPlayerId(), initMessage1);
            final MsgLobbyCreated initMessage2 = new MsgLobbyCreated(player1.getUsername());
            webSocketService.sendMessage(player1.getPlayerId(), initMessage1);
        } catch (IOException ex) {
            webSocketService.closeSession(player1.getPlayerId(), CloseStatus.SERVER_ERROR);
            webSocketService.closeSession(player2.getPlayerId(), CloseStatus.SERVER_ERROR);

            LOGGER.warn("Failed to create session with " + player1.getPlayerId() + " and " + player2.getPlayerId(), ex);
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
        } catch (IOException ex) {
            LOGGER.warn("Can't start game! " + ex);
        }
    }

    private MsgGameStarted createGameStartedMessage(@NotNull Player currentPlayer, @NotNull Player damagedPlayer) {
        if (currentPlayer != damagedPlayer) {
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
            MsgEndGame endGame = createMsgEndGame(gameSession, gameSession.getPlayer1());
            webSocketService.sendMessage(gameSession.getPlayer1Id(), endGame);

            endGame = createMsgEndGame(gameSession, gameSession.getPlayer2());
            webSocketService.sendMessage(gameSession.getPlayer2Id(), endGame);
        } catch (IOException ex) {
            LOGGER.warn("Failed to send MsgEndGame to user " + gameSession.getPlayer1().getPlayerId(), ex);
        } catch (IllegalStateException ex) {
            LOGGER.warn(ex.getMessage());
        }
    }

    private MsgEndGame createMsgEndGame(@NotNull GameSession gameSession, @NotNull Player current) {
        if (current == gameSession.getWinner()) {
            return new MsgEndGame(Boolean.TRUE);
        }
        return new MsgEndGame(Boolean.FALSE);
    }

    public void makeMove(@NotNull GameSession gameSession, @NotNull Cell cell) {
        if(!gameSession.toGamePhase()){
            return;
        }
        try {
            CellStatus cellStatus = gameSession.makeMove(cell);
            if (cellStatus == CellStatus.DESTRUCTED
                    && (gameSession.getStatus().equals(GameSessionStatus.WIN_P1)
                    || gameSession.getStatus().equals(GameSessionStatus.WIN_P2))) {
                endSession(gameSession);
            }
            MsgResultMove msgResultMove = new MsgResultMove(cell, cellStatus);
            webSocketService.sendMessage(gameSession.getPlayer1Id(), msgResultMove);
            webSocketService.sendMessage(gameSession.getPlayer2Id(), msgResultMove);
        } catch (IllegalStateException ex) {
            LOGGER.warn(ex.getMessage());
        } catch (IOException ex) {
            LOGGER.warn("Can't send message! ", ex);
        }
    }
}