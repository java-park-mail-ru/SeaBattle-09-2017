package seabattle.game;

public final class IdGenerator {
    private static IdGenerator instance = null;

    private Long id;

    private IdGenerator() {
        id = 0L;
    }

    public static synchronized IdGenerator getInstance() {
        if (instance == null) {
            instance = new IdGenerator();
        }
        return instance;
    }


    public Long getId() {
        return ++id;
    }
}
