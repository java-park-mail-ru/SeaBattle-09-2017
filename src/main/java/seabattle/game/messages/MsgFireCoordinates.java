package seabattle.game.messages;

import seabattle.game.field.Cell;
import seabattle.msgsystem.Message;

import javax.validation.constraints.NotNull;

public class MsgFireCoordinates extends Message {

    @NotNull
    private Cell coordinates;

    public Cell getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(@NotNull Cell cell) {
        this.coordinates = cell;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (object == null || getClass() != object.getClass()) {
            return false;
        }

        MsgFireCoordinates that = (MsgFireCoordinates) object;

        if (coordinates != null) {
            return coordinates.equals(that.coordinates);
        } else {
            return that.coordinates == null;
        }

    }

    @Override
    public int hashCode() {
        if (coordinates != null) {
            return coordinates.hashCode();
        } else {
            return 0;
        }
    }
}