package seabattle.msgsystem;

import seabattle.game.field.CellStatus;

import java.awt.Point;

public class MsgResultMove extends Message {
    String username;
    Point cellPosition;
    CellStatus cellStatus;

    public MsgResultMove(String username, Point cellPosition, CellStatus cellStatus) {
        this.username = username;
        this.cellPosition = cellPosition;
        this.cellStatus = cellStatus;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Point getCellPosition() {
        return cellPosition;
    }

    public void setCellPosition(Point cellPosition) {
        this.cellPosition = cellPosition;
    }

    public CellStatus getCellStatus() {
        return cellStatus;
    }

    public void setCellStatus(CellStatus cellStatus) {
        this.cellStatus = cellStatus;
    }
}
