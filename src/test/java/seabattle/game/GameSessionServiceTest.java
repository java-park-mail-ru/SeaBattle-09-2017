package seabattle.game;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import seabattle.game.field.Field;
import seabattle.game.gamesession.GameSession;
import seabattle.game.gamesession.GameSessionService;
import seabattle.game.messages.MsgGameStarted;
import seabattle.game.messages.MsgLobbyCreated;
import seabattle.game.player.Player;
import seabattle.game.player.PlayerAI;
import seabattle.game.ship.Ship;
import seabattle.websocket.WebSocketService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@RunWith(SpringRunner.class)
public class GameSessionServiceTest {

    private Player player1 = new Player();
    private Player player2 = new Player();

    @Autowired
    private GameSessionService gameSessionService;

    @MockBean
    private WebSocketService webSocketService;

    private void createGameSession() {
        try {
            doNothing().when(webSocketService).sendMessage(eq(player1.getPlayerId()), any(MsgLobbyCreated.class));
            doNothing().when(webSocketService).sendMessage(eq(player2.getPlayerId()), any(MsgLobbyCreated.class));
        } catch (IOException ignore) {

        }
        gameSessionService.createSession(player1, player2);


    }

    private void tryStartGame(GameSession gameSession) {
        try {
            doNothing().when(webSocketService).sendMessage(eq(player1.getPlayerId()), any(MsgGameStarted.class));
            doNothing().when(webSocketService).sendMessage(eq(player2.getPlayerId()), any(MsgGameStarted.class));
        } catch (IOException ignore) {

        }
        gameSessionService.tryStartGame(gameSession);

    }

    private List<Ship> tetsShips () {
        List<Ship> testShips = new ArrayList<>();
        testShips.add(new Ship(0, 6, 4, false));
        testShips.add(new Ship(0, 3, 3, true));
        testShips.add(new Ship(4, 1, 3, true));
        testShips.add(new Ship(0, 0, 2, false));
        testShips.add(new Ship(2, 9, 2, true));
        testShips.add(new Ship(8, 7, 2, true));
        testShips.add(new Ship(5, 6, 1, false));
        testShips.add(new Ship(8, 3, 1, false));
        testShips.add(new Ship(8, 6, 1, false));
        testShips.add(new Ship(9, 0, 1, false));
        return testShips;
    }

    @Test
    public void createSessionTest() {
        createGameSession();
        try {
            verify(webSocketService).sendMessage(eq(player2.getPlayerId()), any(MsgLobbyCreated.class));
            verify(webSocketService).sendMessage(eq(player1.getPlayerId()), any(MsgLobbyCreated.class));
        } catch (IOException ignore) {
        }
    }

    @Test
    public void tryStartGameWithoutShips() {
        createGameSession();
        tryStartGame(gameSessionService.getGameSession(player1.getPlayerId()));
        try {
            verify(webSocketService, never()).sendMessage(eq(player1.getPlayerId()), any(MsgGameStarted.class));
            verify(webSocketService, never()).sendMessage(eq(player2.getPlayerId()), any(MsgGameStarted.class));
        } catch (IOException ignore) {
        }
        assertNull(gameSessionService.getGameSession(player1.getPlayerId()).getDamagedPlayer());
    }

    /*@Test
    public void tryStartGameWithShips() {
        createGameSession();
        GameSession gameSession = gameSessionService.getGameSession(player1.getPlayerId());
        gameSession.setField1(new Field(tetsShips()));
        gameSession.setField2(new Field(tetsShips()));
        tryStartGame(gameSession);
        try {
            verify(webSocketService).sendMessage(eq(player1.getPlayerId()), any(MsgGameStarted.class));
            verify(webSocketService).sendMessage(eq(player2.getPlayerId()), any(MsgGameStarted.class));
        } catch (IOException ignore) {
        }
        assertNotNull(gameSession.getDamagedPlayer());
    }*/

}
