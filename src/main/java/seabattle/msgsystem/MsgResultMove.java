package seabattle.msgsystem;

import seabattle.game.field.Cell;
import seabattle.game.field.CellStatus;



public class MsgResultMove extends Message {
    String username;
    Cell cell;
    CellStatus cellStatus;

    public MsgResultMove(String username, Cell cell, CellStatus cellStatus) {
        this.username = username;
        this.cell = cell;
        this.cellStatus = cellStatus;
    }


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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
