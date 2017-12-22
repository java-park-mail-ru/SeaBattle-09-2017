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

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (object == null || getClass() != object.getClass()) {
            return false;
        }

        MsgResultMove that = (MsgResultMove) object;

        if (cell != null) {
            if (!cell.equals(that.cell)) {
                return false;
            }
        } else {
            if (that.cell != null) {
                return false;
            }
        }
        if (cellStatus != that.cellStatus) {
            return false;
        }
        if (playerMove != null) {
            return playerMove.equals(that.playerMove);
        } else {
            return that.playerMove == null;
        }
    }

    @Override
    public int hashCode() {
        int result = 0;
        final int sizeInt = 31;
        if (cell != null) {
            result = cell.hashCode();
        }
        if (cellStatus != null) {
            result = sizeInt * result + cellStatus.hashCode();
        } else {
            result = sizeInt * result;
        }
        if (playerMove != null) {
            result = sizeInt * result + playerMove.hashCode();
        } else {
            result = sizeInt * result;
        }

        return result;
    }
}
