package seabattle.game.messages;

import seabattle.game.field.Cell;
import seabattle.game.field.CellStatus;
import seabattle.msgsystem.Message;

@SuppressWarnings("unused")
public class MsgResultMove extends Message {
    private Cell cell;
    private CellStatus cellStatus;
    private String playerMove;

    public MsgResultMove(Cell cell, CellStatus cellStatus, String playerMove) {
        this.cell = cell;
        this.cellStatus = cellStatus;
        this.playerMove = playerMove;
    }

    public String getPlayerMove() {
        return playerMove;
    }

    public void setPlayerMove(String playerMove) {
        this.playerMove = playerMove;
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
