package seabattle.msgsystem;

import seabattle.game.ship.Ship;

import java.util.ArrayList;
import java.util.List;

public class MsgShipPosition extends Message{
    private List<Ship> ships = new ArrayList<>();

    public MsgShipPosition(List<Ship> ships) {
        ships = ships;
    }

    public List<Ship> getShips() {
        return ships;
    }

    public void setShips(List<Ship> ships) {
        ships = ships;
    }
}
