package seabattle.game;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;


public class IdGenerator {
    private static IdGenerator _instance = null;

    private Long id;

    private IdGenerator() {
        id = new Long(0);
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
