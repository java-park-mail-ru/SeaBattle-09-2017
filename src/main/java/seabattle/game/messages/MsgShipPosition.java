package seabattle.game.messages;

import seabattle.game.ship.Ship;
import seabattle.msgsystem.Message;

import java.util.List;

@SuppressWarnings("unused")
public class MsgShipPosition extends Message {

    private List<Ship> ships;

    public List<Ship> getShips() {
        return this.ships;
    }

    public void setShips(List<Ship> ships) {
        this.ships = ships;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (object == null || getClass() != object.getClass()) {
            return false;
        }

        MsgShipPosition that = (MsgShipPosition) object;

        if (ships != null) {
            return ships.equals(that.ships);
        } else {
            return that.ships == null;
        }
    }

    @Override
    public int hashCode() {
        if (ships != null) {
            return ships.hashCode();
        } else {
            return 0;
        }
    }
}
