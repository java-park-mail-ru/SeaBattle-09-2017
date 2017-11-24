package seabattle.game.messages;

import seabattle.game.ship.Ship;
import seabattle.msgsystem.Message;

public class MsgShipIsDestroyed extends Message {
    private Ship destroyedShip;
    private String usernameOwner;

    public MsgShipIsDestroyed(Ship destroyedShip, String usernameOwner) {
        this.destroyedShip = destroyedShip;
        this.usernameOwner = usernameOwner;
    }

    public Ship getDestroyedShip() {
        return destroyedShip;
    }

    public void setDestroyedShip(Ship destroyedShip) {
        this.destroyedShip = destroyedShip;
    }

    public String getUsernameOwner() {
        return usernameOwner;
    }

    public void setUsernameOwner(String usernameOwner) {
        this.usernameOwner = usernameOwner;
    }
}
