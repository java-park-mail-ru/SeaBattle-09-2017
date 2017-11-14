package seabattle.game.ship;

import com.fasterxml.jackson.annotation.JsonProperty;

@SuppressWarnings("unused")
public final class Ship {
    private Integer rowPos;
    private Integer colPos;
    private Integer length;
    private Boolean isVertical;

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

    public Boolean inShip(Integer rowPos, Integer colPos) {
        if (isVertical == Boolean.TRUE) {
            return rowPos < this.rowPos + this.length;
        }
        return colPos < this.colPos + this.length;
    }
}

