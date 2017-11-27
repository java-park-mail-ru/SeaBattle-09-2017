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

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (object == null || getClass() != object.getClass()) {
            return false;
        }

        MsgShipIsDestroyed that = (MsgShipIsDestroyed) object;

        if (destroyedShip != null) {
            if (!destroyedShip.equals(that.destroyedShip)) {
                return false;
            }
        } else {
            if (that.destroyedShip != null) {
                return false;
            }
        }
        if (usernameOwner != null) {
            return usernameOwner.equals(that.usernameOwner);
        } else {
            return that.usernameOwner == null;
        }
    }

    @Override
    public int hashCode() {
        int result = 0;
        final int sizeInt = 31;
        if (destroyedShip != null) {
            result = destroyedShip.hashCode();
        }
        if (usernameOwner != null) {
            result = sizeInt * result + usernameOwner.hashCode();
        } else {
            result = sizeInt * result;
        }

        return result;
    }
}
