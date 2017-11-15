package seabattle.msgsystem;

import seabattle.game.ship.Ship;

import java.util.ArrayList;
import java.util.List;

public class MsgShipPosition extends Message {
    private List<Ship> ships;

    public MsgShipPosition() {
    }

    public MsgShipPosition(List<Ship> ships) {
        this.ships = ships;
    }

    public List<Ship> getShips() {
        return this.ships;
    }

    public void setShips(List<Ship> ships) {
        this.ships = ships;
    }
}
