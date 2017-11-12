package seabattle.game.player;

import seabattle.game.ship.Ship;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unused")
public class Player {

    private List<Ship> aliveShips = new ArrayList<>();
    private List<Ship> deadShips = new ArrayList<>();

    Player(List<Ship> ships) {
        aliveShips.addAll(ships);
    }

    public List<Ship> getAliveShips() {
        return aliveShips;
    }

    public List<Ship> getDeadShips() {
        return deadShips;
    }

    public Boolean allShipsDead() {
        return aliveShips.isEmpty();
    }
}
