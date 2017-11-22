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
}