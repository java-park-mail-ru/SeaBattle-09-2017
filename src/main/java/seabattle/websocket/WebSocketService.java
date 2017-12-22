package seabattle.websocket;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import seabattle.game.player.UserPlayer;
import seabattle.msgsystem.Message;

import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class WebSocketService {
    private static final Logger LOGGER = LoggerFactory.getLogger(WebSocketService.class);
    private Map<Long, WebSocketSession> sessions = new ConcurrentHashMap<>();
    private Map<String, UserPlayer> players = new ConcurrentHashMap<>();
    private final ObjectMapper objectMapper;


    public WebSocketService(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public void registerUser(@NotNull UserPlayer player, @NotNull WebSocketSession webSocketSession) {
        sessions.put(player.getPlayerId(), webSocketSession);
        players.put(webSocketSession.getId(), player);
    }

    public boolean isConnected(@NotNull Long userId) {
        return sessions.containsKey(userId) && sessions.get(userId).isOpen();
    }


    public boolean isConnected(@NotNull String sessionId) {
        return players.containsKey(sessionId);
    }

    public Long getUserId(@NotNull String sessionId) {
        return players.get(sessionId).getPlayerId();
    }

    public UserPlayer removeUser(@NotNull String sessionId) {
        final UserPlayer player = players.remove(sessionId);
        sessions.remove(player.getPlayerId());
        return player;
    }

    void removeUser(@NotNull Long userId) {
        players.remove(sessions.remove(userId).getId());
    }

    public void closeSession(@NotNull Long userId, @NotNull CloseStatus closeStatus) {
        final WebSocketSession webSocketSession = sessions.get(userId);
        if (webSocketSession != null && webSocketSession.isOpen()) {
            try {
                webSocketSession.close(closeStatus);
            } catch (IOException ignore) {
                LOGGER.warn("Can't close session");
            }
        }
    }

    public void sendMessage(@NotNull Long userId, @NotNull Message message) throws IOException {
        final WebSocketSession webSocketSession = sessions.get(userId);
        if (webSocketSession == null) {
            throw new IOException("no game websocket for user " + userId);
        }
        if (!webSocketSession.isOpen()) {
            throw new IOException("session is closed or not exsists");
        }
        try {
            webSocketSession.sendMessage(new TextMessage(objectMapper.writeValueAsString(message)));
        } catch (IOException e) {
            throw new IOException("Unnable to send message", e);
        }
    }

}



