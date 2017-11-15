package seabattle.game.field;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotNull;

@SuppressWarnings("unused")
public final class Cell {
    private Integer rowPos;
    private Integer colPos;

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
}
