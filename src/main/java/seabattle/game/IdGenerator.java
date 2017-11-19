package seabattle.game;

public class IdGenerator {
    private static IdGenerator _instance = null;

    private Long id;

    private IdGenerator() {
        id = 0L;
    }

    public static synchronized IdGenerator getInstance() {
        if (_instance == null)
            _instance = new IdGenerator();
        return _instance;
    }


    public Long getId() {
        return ++id;
    }
}
