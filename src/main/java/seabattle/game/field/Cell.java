package seabattle.game.field;

@SuppressWarnings("unused")
public final class Cell {
    private Integer rowPos;
    private Integer colPos;

    public Cell(Integer rowPos, Integer colPos) {
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
}
