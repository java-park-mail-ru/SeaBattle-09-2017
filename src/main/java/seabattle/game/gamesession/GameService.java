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
    private final Map<Long, Thread> tasks = new ConcurrentHashMap<>();

    public void addTask(@NotNull Long id, Thread newTask) {
        tasks.put(id, newTask);
    }

    public void removeTask(@NotNull Long id) {
        tasks.remove(id);
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
                        if (tasks.get(id) != null) {
                            if (!tasks.get(id).isAlive()) {
                                try {
                                    tasks.get(id).start();
                                } catch (IllegalStateException ex) {
                                    tasks.remove(id);
                                }
                            }
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
