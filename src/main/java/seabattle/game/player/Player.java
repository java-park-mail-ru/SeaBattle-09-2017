package seabattle.game.player;

import seabattle.authorization.views.UserView;
import seabattle.game.ship.Ship;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@SuppressWarnings("unused")
public class Player {

    @NotNull
    private static final AtomicLong PLAYER_ID_GENERATOR = new AtomicLong(0);

    @NotNull
    private Long playerId;
    @NotNull
    private String username;
    @NotNull
    private UserView user;


    private List<Ship> aliveShips = new ArrayList<>();
    private List<Ship> deadShips = new ArrayList<>();

    public Player(UserView user, List<Ship> ships) {
        this.playerId = PLAYER_ID_GENERATOR.getAndIncrement();
        this.user = user;
        this.username = user.getLogin();
        this.aliveShips = ships;
    }

    public Player() {
        this.playerId = PLAYER_ID_GENERATOR.getAndIncrement();
        this.username = "Unknown username " + playerId.toString();
        this.user = null;
    }

    Player(List<Ship> ships) {
        this.playerId = PLAYER_ID_GENERATOR.getAndIncrement();
        this.username = "Unknown username " + playerId.toString();
        this.user = null;
        aliveShips.addAll(ships);
    }

    public void setShips(List<Ship> ships) {
        aliveShips.addAll(ships);
    }

    public List<Ship> getAliveShips() {
        return aliveShips;
    }

    public List<Ship> getDeadShips() {
        return deadShips;
    }

    public Long getPlayerId() {
        return playerId;
    }

    public Boolean allShipsDead() {
        return aliveShips.isEmpty();
    }

    public String getUsername() {
        return username;
    }

    public void setUser(UserView user) {
        this.user = user;
        this.username = user.getLogin();
    }
}
