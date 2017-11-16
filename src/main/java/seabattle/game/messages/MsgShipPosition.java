package seabattle.game.messages;

import seabattle.game.ship.Ship;
import seabattle.msgsystem.Message;

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
