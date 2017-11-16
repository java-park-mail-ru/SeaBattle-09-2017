package seabattle.game.player;

import seabattle.authorization.views.UserView;
import seabattle.game.ship.Ship;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@SuppressWarnings("unused")
public class Player {
    private static final AtomicLong ID_GENERATOR = new AtomicLong();

    @NotNull
    private static final Long PLAYER_ID = ID_GENERATOR.getAndIncrement();
    @NotNull
    private String username;
    @NotNull
    private UserView user;


    private List<Ship> aliveShips = new ArrayList<>();
    private List<Ship> deadShips = new ArrayList<>();

    public Player(UserView user, List<Ship> ships) {
        this.user = user;
        this.username = user.getLogin();
        this.aliveShips = ships;
    }

    Player(List<Ship> ships) {
        this.username = "Unknown username";
        this.user = null;
        aliveShips.addAll(ships);
    }

    public List<Ship> getAliveShips() {
        return aliveShips;
    }

    public List<Ship> getDeadShips() {
        return deadShips;
    }

    public Long getPlayerId() {
        return PLAYER_ID;
    }

    public Boolean allShipsDead() {
        return aliveShips.isEmpty();
    }

    public String getUsername() {
        return username;
    }
}
