package seabattle.game.ship;

@SuppressWarnings("unused")
public final class Ship {
    private Integer horizontalPos;
    private Integer verticalPos;
    private Integer length;
    private ShipOrientation orientation;

    public Ship(final Integer horizontalPos, final Integer verticalPos, final Integer length,
                final ShipOrientation orientation) {
        this.horizontalPos = horizontalPos;
        this.verticalPos = verticalPos;
        this.length = length;
        this.orientation = orientation;
    }

    public Integer getHorizontalPos() {
        return horizontalPos;
    }

    public void setHorizontalPos(Integer horizontalPos) {
        this.horizontalPos = horizontalPos;
    }

    public Integer getVerticalPos() {
        return verticalPos;
    }

    public void setVerticalPos(Integer verticalPos) {
        this.verticalPos = verticalPos;
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
            return horPos < this.horizontalPos + this.length;
        }
        return verPos < this.verticalPos + this.length;
    }
}

