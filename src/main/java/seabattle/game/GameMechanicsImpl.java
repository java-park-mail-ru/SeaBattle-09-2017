package seabattle.game;

import org.springframework.stereotype.Service;
import seabattle.game.gamesession.GameSession;
import seabattle.websocket.WebSocketService;

import javax.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.Map;

/*@Service
public class GameMechanicsImpl implements GameMechanics{
    private final Map<Long, GameSession> userToLobby = new HashMap<>();

    @NotNull
    private final  WebSocketService webSocketService;

    public GameMechanicsImpl(@NotNull WebSocketService webSocketService) {
        this.webSocketService = webSocketService;
    }

    @Override
    void addUser(@NotNull Long userId){
        webSocketService.registerUser(userId,);
    }

    @Override
    void deleteUser(@NotNull Long userId){

    }

    @Override
    boolean isUserAdded (@NotNull Long userId){

    }

}*/
