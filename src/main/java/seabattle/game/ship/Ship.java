package seabattle.game.ship;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import seabattle.game.field.Cell;
import seabattle.game.field.Field;

import java.util.ArrayList;

@SuppressWarnings("unused")
public final class Ship {
    private Integer rowPos;
    private Integer colPos;
    private Integer length;
    private Boolean isVertical;

    @JsonCreator
    public Ship(@JsonProperty("rowPos") Integer rowPos, @JsonProperty("colPos") Integer colPos,
                @JsonProperty("length") Integer length, @JsonProperty("isVertical") Boolean isVertical) {
        this.rowPos = rowPos;
        this.colPos = colPos;
        this.length = length;
        this.isVertical = isVertical;
    }

    public Integer getRowPos() {
        return rowPos;
    }

    public void setRowPos(Integer rowPos) {
        this.rowPos = rowPos;
    }

    public Integer getColPos() {
        return colPos;
    }

    public void setColPos(Integer colPos) {
        this.colPos = colPos;
    }

    public Integer getLength() {
        return length;
    }

    public void setLength(Integer length) {
        this.length = length;
    }

    public Boolean getIsVertical() {
        return isVertical;
    }

    public void setIsVertical(Boolean isVertical) {
        this.isVertical = isVertical;
    }

    public Boolean inShip(Cell cell) {
        if (isVertical == Boolean.TRUE) {
            return cell.getRowPos() < this.rowPos + this.length;
        }
        return cell.getColPos() < this.colPos + this.length;
    }


    public ArrayList<Cell> getCells() {
        ArrayList<Cell> result = new ArrayList<>();

        if (isVertical.equals(Boolean.TRUE)) {
            for (Integer i = 0; i < length; ++i) {
                result.add(Cell.of(rowPos + i, colPos));
            }
        } else {
            for (Integer i = 0; i < length; ++i) {
                result.add(Cell.of(rowPos, colPos + i));
            }
        }

        return result;
    }

    public ArrayList<Cell> getCellsAroundShip() {

        final Field field = new Field();
        ArrayList<Cell> result = new ArrayList<>();

        for (int i = rowPos - 1; i <= getLastCell().getRowPos() + 1; i++) {
            for (int j = colPos - 1; j <= getLastCell().getColPos() + 1; j++) {
                final Cell cell = new Cell(i, j);
                if (!field.cellOutOfBounds(cell)  && !getCells().contains(cell)) {
                    result.add(cell);
                }
            }
        }
        return result;
    }

    @JsonIgnoreProperties
    public Cell getLastCell() {
        if (isVertical) {
            return Cell.of(rowPos + length - 1, colPos);
        }
        return Cell.of(rowPos, colPos + length - 1);
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (object == null || getClass() != object.getClass()) {
            return false;
        }

        Ship ship = (Ship) object;

        if (rowPos != null) {
            if (!rowPos.equals(ship.rowPos)) {
                return false;
            }
        } else {
            if (ship.rowPos != null) {
                return false;
            }
        }
        if (colPos != null) {
            if (!colPos.equals(ship.colPos)) {
                return false;
            }
        } else {
            if (ship.colPos != null) {
                return false;
            }
        }
        if (length != null) {
            if (!length.equals(ship.length)) {
                return false;
            }
        } else {
            if (ship.length != null) {
                return false;
            }
        }

        if (isVertical != null) {
            return isVertical.equals(ship.isVertical);
        } else {
            return ship.isVertical == null;
        }
    }

    @Override
    public int hashCode() {
        int result = 0;
        final int sizeInt = 31;
        if (rowPos != null) {
            result = rowPos.hashCode();
        }
        if (colPos != null) {
            result = sizeInt * result + colPos.hashCode();
        } else {
            result = sizeInt * result;
        }
        if (length != null) {
            result = sizeInt * result + length.hashCode();
        } else {
            result = sizeInt * result;
        }
        if (isVertical != null) {
            result = sizeInt * result + isVertical.hashCode();
        } else {
            result = sizeInt * result;
        }

        return result;

    }
}

