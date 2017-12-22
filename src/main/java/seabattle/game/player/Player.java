package seabattle.game.player;

import seabattle.authorization.views.UserView;
import seabattle.game.field.Cell;
import seabattle.game.field.Field;
import seabattle.game.ship.Ship;

import java.util.List;

public interface Player {

    UserView getUser();

    void setUser(UserView user);

    Integer getScore();

    void setScore(Integer score);

    String getUsername();

    void setUsername(String username);

    void setShips(List<Ship> ships);

    List<Ship> getAliveShips();

    List<Ship> getDeadShips();

    Boolean allShipsDead();

    Long getPlayerId();

    List<Ship> generateShips();

    Cell makeDecision(final Field field);
}
