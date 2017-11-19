package seabattle.game.messages;

import seabattle.game.field.Cell;
import seabattle.game.field.CellStatus;
import seabattle.msgsystem.Message;

@SuppressWarnings("unused")
public class MsgResultMove extends Message {
    private Cell cell;
    private CellStatus cellStatus;

    public MsgResultMove(Cell cell, CellStatus cellStatus) {
        this.cell = cell;
        this.cellStatus = cellStatus;
    }

    public Cell getCell() {
        return cell;
    }

    public void setCell(Cell cell) {
        this.cell = cell;
    }

    public CellStatus getCellStatus() {
        return cellStatus;
    }

    public void setCellStatus(CellStatus cellStatus) {
        this.cellStatus = cellStatus;
    }
}
