package seabattle.game.messages;

import seabattle.game.field.Cell;
import seabattle.game.field.CellStatus;
import seabattle.msgsystem.Message;

@SuppressWarnings("unused")
public class MsgResultMove extends Message {
    private Long id;
    private Cell cell;
    private CellStatus cellStatus;

    public MsgResultMove(Long id, Cell cell, CellStatus cellStatus) {
        this.id = id;
        this.cell = cell;
        this.cellStatus = cellStatus;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
