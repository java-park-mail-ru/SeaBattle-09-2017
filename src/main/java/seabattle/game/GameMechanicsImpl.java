package seabattle.game;

import org.springframework.stereotype.Service;
import seabattle.game.gamesession.GameSession;
import seabattle.websocket.WebSocketService;

import javax.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("unused")
@Service
public class GameMechanicsImpl implements GameMechanics {
    private final Map<Long, GameSession> userToLobby = new HashMap<>();

    @NotNull
    private final  WebSocketService webSocketService;

    public GameMechanicsImpl(@NotNull WebSocketService webSocketService) {
        this.webSocketService = webSocketService;
    }

    @Override
    public void addUser(@NotNull Long userId) {
    }

    @Override
    public void deleteUser(@NotNull Long userId) {
        webSocketService.removeUser(userId);
    }

    @Override
    public Boolean isUserAdded(@NotNull Long userId) {
        return webSocketService.isConnected(userId);
    }

    public Map<Long, GameSession> getUserToLobby() {
        return userToLobby;
    }
}