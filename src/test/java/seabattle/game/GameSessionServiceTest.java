package seabattle.game;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import seabattle.game.field.Cell;
import seabattle.game.field.CellStatus;
import seabattle.game.field.Field;
import seabattle.game.gamesession.GameSession;
import seabattle.game.gamesession.GameSessionService;
import seabattle.game.messages.*;
import seabattle.game.player.Player;
import seabattle.game.player.UserPlayer;
import seabattle.game.ship.Ship;
import seabattle.websocket.WebSocketService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@RunWith(SpringRunner.class)
public class GameSessionServiceTest {


    @Autowired
    private GameSessionService gameSessionService;

    @MockBean
    private WebSocketService webSocketService;

    private void createGameSession(Player player1, Player player2) {
        gameSessionService.createSession(player1, player2);
    }

    private List<Ship> testShips() {
        List<Ship> testShips = new ArrayList<>();
        testShips.add(new Ship(0, 6, 4, false));
        testShips.add(new Ship(0, 3, 3, true));
        testShips.add(new Ship(4, 1, 3, true));
        testShips.add(new Ship(0, 0, 2, false));
        testShips.add(new Ship(2, 9, 2, true));
        testShips.add(new Ship(8, 7, 2, true));
        testShips.add(new Ship(5, 6, 1, false));
        testShips.add(new Ship(8, 3, 1, false));
        testShips.add(new Ship(8, 5, 1, false));
        testShips.add(new Ship(9, 0, 1, false));
        return testShips;
    }

    @Test
    public void createSessionTest() {
        UserPlayer player1 = new UserPlayer();
        UserPlayer player2 = new UserPlayer();
        createGameSession(player1, player2);
        try {
            verify(webSocketService).sendMessage(eq(player2.getPlayerId()),
                    eq(new MsgLobbyCreated(player1.getUsername())));
            verify(webSocketService).sendMessage(eq(player1.getPlayerId()),
                    eq(new MsgLobbyCreated(player2.getUsername())));
        } catch (IOException ignore) {
        }
    }

    @Test
    public void tryStartGameWithoutShips() {
        UserPlayer player1 = new UserPlayer();
        UserPlayer player2 = new UserPlayer();
        createGameSession(player1, player2);
        gameSessionService.tryStartGame(gameSessionService.getGameSession(player1.getPlayerId()));
        try {
            verify(webSocketService, never()).sendMessage(eq(player1.getPlayerId()),
                    any(MsgGameStarted.class));
            verify(webSocketService, never()).sendMessage(eq(player2.getPlayerId()),
                    any(MsgGameStarted.class));
        } catch (IOException ignore) {
        }
        assertNull(gameSessionService.getGameSession(player1.getPlayerId()).getDamagedPlayer());
    }

    @Test
    public void tryStartGameWithShips() {
        UserPlayer player1 = new UserPlayer();
        UserPlayer player2 = new UserPlayer();
        createGameSession(player1, player2);
        GameSession gameSession = gameSessionService.getGameSession(player1.getPlayerId());
        gameSession.setField1(new Field(testShips()));
        gameSession.setField2(new Field(testShips()));
        gameSessionService.tryStartGame(gameSession);
        final Long damagedPlayerId = gameSession.getDamagedPlayer().getPlayerId();
        final Long attackingPlayerId;
        if (damagedPlayerId.equals(player1.getPlayerId())) {
            attackingPlayerId = player2.getPlayerId();
        } else {
            attackingPlayerId = player1.getPlayerId();
        }
        try {
            verify(webSocketService).sendMessage(eq(damagedPlayerId),
                    eq(new MsgGameStarted(false)));
            verify(webSocketService).sendMessage(eq(attackingPlayerId),
                    eq(new MsgGameStarted(true)));
        } catch (IOException ignore) {
        }
        assertNotNull(gameSession.getDamagedPlayer());
    }


    private GameSession makeMoveInit() {
        Player player1 = new UserPlayer();
        Player player2 = new UserPlayer();
        player1.setShips(testShips());
        player2.setShips(testShips());
        createGameSession(player1, player2);
        GameSession gameSession = gameSessionService.getGameSession(player1.getPlayerId());
        gameSession.setField1(new Field(testShips()));
        gameSession.setField2(new Field(testShips()));
        gameSessionService.tryStartGame(gameSession);
        return gameSession;
    }

    @Test
    public void makeSuccessMove (){
        Cell cell = new Cell(0,6);
        GameSession gameSession = makeMoveInit();
        final Long damagedPlayerId = gameSession.getDamagedPlayer().getPlayerId();
        final String futureAttackPlayerNick;
        gameSessionService.makeMove(gameSession, cell);
        assertEquals(damagedPlayerId, gameSession.getDamagedPlayer().getPlayerId());
        if (damagedPlayerId.equals(gameSession.getPlayer1Id())) {
            assertEquals(gameSession.getField1().getCellStatus(cell), CellStatus.ON_FIRE);
            assertEquals(gameSession.getDamagedPlayer().getPlayerId(), gameSession.getPlayer1Id());
            futureAttackPlayerNick = gameSession.getPlayer2().getUsername();
        } else {
            assertEquals(gameSession.getField2().getCellStatus(cell), CellStatus.ON_FIRE);
            assertEquals(gameSession.getDamagedPlayer().getPlayerId(), gameSession.getPlayer2Id());
            futureAttackPlayerNick = gameSession.getPlayer1().getUsername();
        }
        try {
            verify(webSocketService).sendMessage(eq(gameSession.getPlayer1Id()),
                    eq(new MsgResultMove(cell, CellStatus.ON_FIRE, futureAttackPlayerNick)));
            verify(webSocketService).sendMessage(eq(gameSession.getPlayer2Id()),
                    eq(new MsgResultMove(cell, CellStatus.ON_FIRE, futureAttackPlayerNick)));
        } catch (IOException ignore) {
        }
    }

    @Test
    public void makeUnsuccessMove() {
        Cell cell = new Cell(2,5);
        GameSession gameSession = makeMoveInit();
        final Long damagePlayerId = gameSession.getDamagedPlayer().getPlayerId();
        final String attacPlayerNick = gameSession.getDamagedPlayer().getUsername();
        gameSessionService.makeMove(gameSession, cell);
        if (damagePlayerId.equals(gameSession.getPlayer1Id())) {
            assertEquals(gameSession.getPlayer2Id(), gameSession.getDamagedPlayer().getPlayerId());
            assertEquals(gameSession.getField1().getCellStatus(cell), CellStatus.BLOCKED);
        } else {
            assertEquals(gameSession.getPlayer1Id(), gameSession.getDamagedPlayer().getPlayerId());
            assertEquals(gameSession.getField2().getCellStatus(cell), CellStatus.BLOCKED);
        }
        try {
            verify(webSocketService).sendMessage(eq(gameSession.getPlayer1Id()),
                    eq(new MsgResultMove(cell, CellStatus.BLOCKED, attacPlayerNick)));
            verify(webSocketService).sendMessage(eq(gameSession.getPlayer2Id()),
                    eq(new MsgResultMove(cell, CellStatus.BLOCKED, attacPlayerNick)));
        } catch (IOException ignore) {
        }
    }


    @Test
    public void makeFireInOnFireCell () {
        GameSession gameSession = makeMoveInit();
        Cell cell = new Cell(0,6);
        gameSessionService.makeMove(gameSession, cell);
        final Long damagePlayerId = gameSession.getDamagedPlayer().getPlayerId();
        gameSessionService.makeMove(gameSession, cell);
        if (damagePlayerId.equals(gameSession.getPlayer1Id())) {
            assertEquals(gameSession.getField1().getCellStatus(cell), CellStatus.ON_FIRE);
            try {
                verify(webSocketService).sendMessage(eq(gameSession.getPlayer2Id()),
                        eq(new MsgError("unacceptable move ")));
            } catch (IOException ignore) {
            }
        } else {
            assertEquals(gameSession.getField2().getCellStatus(cell), CellStatus.ON_FIRE);
            try {
                verify(webSocketService).sendMessage(eq(gameSession.getPlayer1Id()),
                        eq(new MsgError("unacceptable move ")));
            } catch (IOException ignore) {
            }
        }
    }

    @Test
    public void makeFireInBlockedCell () {
        GameSession gameSession = makeMoveInit();
        Cell cell = new Cell(2,5);
        final Long damagePlayerId = gameSession.getDamagedPlayer().getPlayerId();
        gameSessionService.makeMove(gameSession, cell);
        gameSessionService.makeMove(gameSession, cell);
        gameSessionService.makeMove(gameSession, cell);
        if (damagePlayerId.equals(gameSession.getPlayer1Id())) {
            assertEquals(gameSession.getField1().getCellStatus(cell), CellStatus.BLOCKED);
            try {
                verify(webSocketService).sendMessage(eq(gameSession.getPlayer2Id()),
                        eq(new MsgError("unacceptable move ")));
            } catch (IOException ignore) {
            }
        } else {
            assertEquals(gameSession.getField2().getCellStatus(cell), CellStatus.BLOCKED);
            try {
                verify(webSocketService).sendMessage(eq(gameSession.getPlayer1Id()),
                        eq(new MsgError("unacceptable move ")));
            } catch (IOException ignore) {
            }
        }
    }


    @Test
    public void shipWasDestroyed () {
        final GameSession gameSession = makeMoveInit();
        gameSessionService.makeMove(gameSession, new Cell(4,1));
        gameSessionService.makeMove(gameSession, new Cell(5,1));
        gameSessionService.makeMove(gameSession, new Cell(6,1));
        assertFalse(gameSession.getDamagedPlayer().getDeadShips().isEmpty());
        final Player damagedPlayer = gameSession.getDamagedPlayer();
        final Field damagedField;
        if (damagedPlayer.equals(gameSession.getPlayer1())) {
            damagedField = gameSession.getField1();
        } else {
            damagedField = gameSession.getField2();
        }
        for (int i = damagedPlayer.getDeadShips().get(0).getRowPos() - 1;
             i <= damagedPlayer.getDeadShips().get(0).getRowPos() + 1; i++) {
            for (int j = damagedPlayer.getDeadShips().get(0).getColPos() - 1;
                 j <= damagedPlayer.getDeadShips().get(0).getColPos() + 1; j++) {
                final Cell cell = new Cell(i, j);
                if (!damagedPlayer.getDeadShips().get(0).getCells().contains(cell)) {
                    assertEquals(damagedField.getCellStatus(cell), CellStatus.BLOCKED);
                } else {
                    assertEquals(damagedField.getCellStatus(cell), CellStatus.DESTRUCTED);
                }
            }
        }

        final Ship destroyShip = new Ship(4, 1, 3, true);
        try {
            verify(webSocketService).sendMessage(eq(gameSession.getPlayer1Id()),
                    eq(new MsgShipIsDestroyed(destroyShip, damagedPlayer.getUsername())));
            verify(webSocketService).sendMessage(eq(gameSession.getPlayer2Id()),
                    eq(new MsgShipIsDestroyed(destroyShip, damagedPlayer.getUsername())));
        } catch (IOException ignore) {
        }

    }

    @Test
    public void endGame() {
        final GameSession gameSession = makeMoveInit();
        final List<Ship> testShips = testShips();
        for (Ship ship : testShips) {
            for (Cell cell : ship.getCells()) {
                gameSessionService.makeMove(gameSession, cell);
            }
        }
        assertTrue(gameSession.getDamagedPlayer().getAliveShips().isEmpty());
        try {
            verify(webSocketService).sendMessage(eq(gameSession.getWinner().getPlayerId()),
                    eq(new MsgEndGame(true, 0)));
            verify(webSocketService).sendMessage(eq(gameSession.getDamagedPlayer().getPlayerId()),
                    eq(new MsgEndGame(false, 0)));
        } catch (IOException ignore) {
        }
    }

}

