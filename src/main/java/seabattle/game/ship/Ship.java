package seabattle.game.ship;

@SuppressWarnings("unused")
public final class Ship {
    private Integer rowPos;
    private Integer colPos;
    private Integer length;
    private ShipOrientation orientation;

    public Ship(final Integer rowPos, final Integer colPos, final Integer length,
                final ShipOrientation orientation) {
        this.rowPos = rowPos;
        this.colPos = colPos;
        this.length = length;
        this.orientation = orientation;
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

    public ShipOrientation getOrientation() {
        return orientation;
    }

    public void setOrientation(ShipOrientation orientation) {
        this.orientation = orientation;
    }

    public Boolean inShip(Integer horPos, Integer verPos) {
        if (this.orientation == ShipOrientation.HORIZONTAL) {
            return horPos < this.rowPos + this.length;
        }
        return verPos < this.colPos + this.length;
    }
}

