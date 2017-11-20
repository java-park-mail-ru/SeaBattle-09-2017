package seabattle.game;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
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

    private GameSession createGameSession() {
        try {
            doNothing().when(webSocketService).sendMessage(eq(player1.getPlayerId()), any(MsgLobbyCreated.class));
            doNothing().when(webSocketService).sendMessage(eq(player2.getPlayerId()), any(MsgLobbyCreated.class));
        } catch (IOException ignore) {

        }
        gameSessionService.createSession(player1, player2);

        return new GameSession(player1, player2, gameSessionService);

    }

    private void tryStartGame(GameSession gameSession) {
        try {
            doNothing().when(webSocketService).sendMessage(eq(player1.getPlayerId()), any(MsgGameStarted.class));
            doNothing().when(webSocketService).sendMessage(eq(player2.getPlayerId()), any(MsgGameStarted.class));
        } catch (IOException ignore) {

        }
        gameSessionService.tryStartGame(gameSession);

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
        GameSession gameSession = createGameSession();
        tryStartGame(gameSession);
        try {
            verify(webSocketService, never()).sendMessage(eq(player1.getPlayerId()), any(MsgGameStarted.class));
            verify(webSocketService, never()).sendMessage(eq(player2.getPlayerId()), any(MsgGameStarted.class));
        } catch (IOException ignore) {
        }
        assertNull(gameSession.getDamagedPlayer());
    }

}
