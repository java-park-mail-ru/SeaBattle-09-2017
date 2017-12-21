package seabattle.game.gamesession;

import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

@Service
public class GameService implements Runnable {

    @NotNull
    private static final int IDLE_TIME = 50;

    @NotNull
    private final Queue<Runnable> tasks = new ConcurrentLinkedQueue<>();

    public void addTask(Runnable newTask) {
        tasks.add(newTask);
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
                    tasks.remove().run();
                }
                Thread.sleep(IDLE_TIME);
            } catch (InterruptedException e) {
                return;
            }
        }
    }
}
