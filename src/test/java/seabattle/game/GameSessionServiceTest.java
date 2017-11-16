package seabattle.game;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.DataAccessException;
import org.springframework.test.context.junit4.SpringRunner;
import seabattle.game.gamesession.GameSessionService;
import seabattle.game.messages.MsgLobbyCreated;
import seabattle.game.player.Player;
import seabattle.websocket.WebSocketService;

import java.io.IOException;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
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
        final MsgLobbyCreated initMessage1 = new MsgLobbyCreated(player1.getUsername());
        final MsgLobbyCreated initMessage2 = new MsgLobbyCreated(player2.getUsername());
        try {
            doNothing().when(webSocketService).sendMessage(eq(player1.getPlayerId()), eq(initMessage2));
            doNothing().when(webSocketService).sendMessage(eq(player2.getPlayerId()), eq(initMessage1));
        } catch (IOException ignore) {

        }
        gameSessionService.createSession(player1, player2);
        try {
            verify(webSocketService).sendMessage(eq(player2.getPlayerId()), eq(initMessage1));
            verify(webSocketService).sendMessage(eq(player1.getPlayerId()), eq(initMessage2));
        } catch (IOException ignore) {
        }

    }


    @Test
    public void createSession() { createGameSession(); }

}
