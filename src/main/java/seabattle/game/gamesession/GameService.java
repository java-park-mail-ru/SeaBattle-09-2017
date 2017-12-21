package seabattle.game.gamesession;

import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


@Service
public class GameService implements Runnable {

    @NotNull
    private static final int IDLE_TIME = 50;

    @NotNull
    private final Map<Long, Runnable> tasks = new ConcurrentHashMap<>();

    public void addTask(@NotNull Long id, Runnable newTask) {
        tasks.put(id, newTask);
    }

    GameService() {
        start();
    }

    public void start() {
        (new Thread(this)).start();
    }

    @Override
    public void run() {
        while (!Thread.interrupted()) {
            try {
                while (!tasks.isEmpty()) {
                    for (Long id : tasks.keySet()) {
                        tasks.remove(id).run();
                    }
                }
                Thread.sleep(IDLE_TIME);
            } catch (InterruptedException e) {
                return;
            }
        }
    }
}
