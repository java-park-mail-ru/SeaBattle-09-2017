package seabattle.game.gamesession;

import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.Map;

@Service
public class GameSessionService {
    @NotNull
    private final Map<Long, GameSession> gameSessions= new HashMap<>();
}
