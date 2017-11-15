package seabattle.game.gamesession;

import org.springframework.stereotype.Service;
import seabattle.game.player.Player;
import seabattle.websocket.WebSocketService;

import javax.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

@SuppressWarnings("unused")
@Service
public class GameSessionService {
    @NotNull
    private final Map<Long, GameSession> gameSessions = new HashMap<>();

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

    public void prepareGameForSetup(@NotNull Player player1, @NotNull Player player2) {
        final Player[] players = { player1, player2 };
        Integer secondPlayerPosition = ThreadLocalRandom.current().nextInt(0, 2);
        Player damagedPlayer = players[secondPlayerPosition];

        final GameSession gameSession = new GameSession(player1, player2, damagedPlayer, this);

        gameSessions.put(player1.getPlayerId(), gameSession);
        gameSessions.put(player2.getPlayerId(), gameSession);

        /* TODO MESSAGES */
    }
}