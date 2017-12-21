package seabattle.game.player;

import org.springframework.stereotype.Service;
import seabattle.game.gamesession.GameService;
import seabattle.game.gamesession.GameSession;
import seabattle.game.gamesession.GameSessionService;

import javax.validation.constraints.NotNull;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class AIService implements Runnable {

    @NotNull
    private static final int IDLE_TIME = 500;

    @NotNull
    private GameSessionService gameSessionService;

    @NotNull
    private GameService gameService;

    private final Map<Long, GameSession> gameSessionsWithBot = new ConcurrentHashMap<>();

    AIService(@NotNull GameSessionService gameSessionService, @NotNull GameService gameService) {
        this.gameSessionService = gameSessionService;
        this.gameService = gameService;
        start();
    }

    public void start() {
        (new Thread(this)).start();
    }

    public void addSession(@NotNull Long id, @NotNull GameSession gameSession) {
        gameSessionsWithBot.put(id, gameSession);
    }

    public void delSession(@NotNull Long id) {
        gameSessionsWithBot.remove(id);
    }

    @Override
    public void run() {
        while (!Thread.interrupted()) {
            try {
                while (!gameSessionsWithBot.isEmpty()) {
                    for (GameSession gameSession : gameSessionsWithBot.values()) {
                        if (gameSession.getAttackingPlayer().getPlayerId() == null) {
                            gameService.addTask(() -> gameSessionService.makeMove(gameSession,
                                    gameSession.getAttackingPlayer().makeDecision(gameSession.getDamagedField())));
                        }
                    }
                }
                Thread.sleep(IDLE_TIME);
            } catch (InterruptedException e) {
                return;
            }
        }
    }
}
