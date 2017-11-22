package seabattle.game.messages;

import com.fasterxml.jackson.annotation.JsonProperty;
import seabattle.game.ship.Ship;
import seabattle.msgsystem.Message;

import java.util.List;

@SuppressWarnings("unused")
public class MsgShipPosition extends Message {

    @JsonProperty("ships")
    private List<Ship> ships;

    public MsgShipPosition() {
    }

    public MsgShipPosition(@JsonProperty("ships") List<Ship> ships) {
        this.ships = ships;
    }

    public List<Ship> getShips() {
        return this.ships;
    }

    public void setShips(List<Ship> ships) {
        this.ships = ships;
    }
}
