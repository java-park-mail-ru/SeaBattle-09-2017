package seabattle.game.field;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotNull;

@SuppressWarnings("unused")
public final class Cell {
    private Integer rowPos;
    private Integer colPos;

    @JsonCreator
    public Cell(@JsonProperty("rowPos") Integer rowPos, @JsonProperty("colPos") Integer colPos) {
        this.rowPos = rowPos;
        this.colPos = colPos;
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

    @Override
    public String toString() {
        return '{'
                + "rowPos=" + rowPos
                + ", colPos=" + colPos
                + '}';
    }

    @NotNull
    public static Cell of(Integer rowPos, Integer colPos) {
        return new Cell(rowPos, colPos);
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (object == null || getClass() != object.getClass()) {
            return false;
        }

        final Cell cell = (Cell) object;

        if (rowPos != null) {
            if (!rowPos.equals(cell.rowPos)) {
                return false;
            }
        } else {
            if (cell.rowPos != null) {
                return false;
            }
        }


        if (colPos != null) {
           return (colPos.equals(cell.colPos));
        } else {
           return (cell.colPos == null);
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

        return result;
    }
}
