package seabattle.game.player;

import seabattle.authorization.views.UserView;
import seabattle.game.field.Cell;
import seabattle.game.field.Field;
import seabattle.game.ship.Ship;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@SuppressWarnings("unused")
public class UserPlayer implements Player {

    @NotNull
    private static final AtomicLong PLAYER_ID_GENERATOR = new AtomicLong(0);

    @NotNull
    private Long playerId;
    @NotNull
    private String username;
    @NotNull
    private Integer score = 0;
    @NotNull
    private UserView user;


    private List<Ship> aliveShips = new ArrayList<>();
    private List<Ship> deadShips = new ArrayList<>();

    public UserPlayer(UserView user, List<Ship> ships) {
        this.playerId = PLAYER_ID_GENERATOR.getAndIncrement();
        this.user = user;
        this.username = user.getLogin();
        this.aliveShips = ships;
    }

    public UserPlayer(List<Ship> ships) {
        this.playerId = PLAYER_ID_GENERATOR.getAndIncrement();
        this.username = "Unknown username " + playerId.toString();
        this.user = null;
        aliveShips.addAll(ships);
    }

    public UserPlayer() {
        this.playerId = PLAYER_ID_GENERATOR.getAndIncrement();
        this.username = "Unknown username " + playerId.toString();
        this.user = null;
    }

    @Override
    public Integer getScore() {
        return score;
    }

    @Override
    public UserView getUser() {
        return user;
    }

    @Override
    public void setScore(Integer score) {
        this.score = score;
    }

    @Override
    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public void setShips(List<Ship> ships) {
        aliveShips.addAll(ships);
    }

    @Override
    public List<Ship> getAliveShips() {
        return aliveShips;
    }

    @Override
    public List<Ship> getDeadShips() {
        return deadShips;
    }

    @Override
    public Long getPlayerId() {
        return playerId;
    }

    @Override
    public List<Ship> generateShips() {
        return null;
    }

    @Override
    public Cell makeDecision(Field field) {
        return null;
    }

    @Override
    public Boolean allShipsDead() {
        return aliveShips.isEmpty();
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public void setUser(UserView user) {
        this.user = user;
        this.username = user.getLogin();
    }
}
