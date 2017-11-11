package seabattle.game.field;

import seabattle.game.ship.Ship;
import seabattle.game.ship.ShipOrientation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@SuppressWarnings("unused")
public final class Field {

    private final Integer fieldSize = 10;
    private List<List<CellStatus>> cells = new ArrayList<>(Collections.nCopies(fieldSize,
            new ArrayList<>(Collections.nCopies(fieldSize, CellStatus.FREE))));

    public Field(List<Ship> ships) {
        ships.forEach(ship -> {
            for (int i = 0; i < ship.getLength(); ++i) {
                cells.get(ship.getHorizontalPos()).set(ship.getVerticalPos(), CellStatus.OCCUPIED);
            }
        });
    }

    public CellStatus fire(Integer horizontalPos, Integer verticalPos) {
        if (horizontalPos < 0 || horizontalPos >= fieldSize || verticalPos < 0 || verticalPos >= fieldSize) {
            throw new IllegalArgumentException("Given position is out of bounds!");
        }
        switch (cells.get(horizontalPos).get(verticalPos)) {
            case FREE:
                cells.get(horizontalPos).set(verticalPos, CellStatus.BLOCKED);
                return CellStatus.BLOCKED;
            case OCCUPIED:
                cells.get(verticalPos).set(verticalPos, CellStatus.DESTRUCTED);
                return CellStatus.DESTRUCTED;
            case DESTRUCTED:
                throw new IllegalArgumentException("Given position is already checked!");
            case BLOCKED:
                throw new IllegalArgumentException("Given position is already checked!");
            default:
                throw new RuntimeException("Internal problem!");
        }
    }

    private CellStatus getCellStatus(Integer horizontalPos, Integer verticalPos) {
        if (horizontalPos < 0 || horizontalPos >= fieldSize || verticalPos < 0 || verticalPos >= fieldSize) {
            throw new IllegalArgumentException("Given position is out of bounds!");
        }
        return cells.get(horizontalPos).get(verticalPos);
    }

    public Boolean shipKilled(Ship ship) {
        if (ship.getOrientation() == ShipOrientation.VERTICAL) {
            for (Integer y = ship.getVerticalPos(); y < ship.getLength(); ++y) {
                if (getCellStatus(ship.getHorizontalPos(), y) != CellStatus.DESTRUCTED) {
                    return false;
                }
            }
        } else {
            for (Integer x = ship.getHorizontalPos(); x < ship.getLength(); ++x) {
                if (getCellStatus(x, ship.getVerticalPos()) != CellStatus.DESTRUCTED) {
                    return false;
                }
            }
        }

        return true;
    }

    public void fillCellsAroundShip(Ship ship) {
        Integer horizontalPosMin = 0;
        if (ship.getHorizontalPos() != 0) {
            horizontalPosMin = ship.getHorizontalPos() - 1;
        }
        Integer verticalPosMin = 0;
        if (ship.getVerticalPos() != 0) {
            horizontalPosMin = ship.getVerticalPos() - 1;
        }

        Integer horizontalPosMax;
        Integer verticalPosMax;
        if (ship.getOrientation() == ShipOrientation.VERTICAL) {
            if (ship.getHorizontalPos() != (fieldSize - 1)) {
                horizontalPosMax = ship.getHorizontalPos() + 2;
            } else {
                horizontalPosMax = fieldSize;
            }
            if ((ship.getVerticalPos() + ship.getLength()) != fieldSize) {
                verticalPosMax = ship.getVerticalPos() + ship.getLength() + 1;
            } else {
                verticalPosMax = fieldSize;
            }
        } else {
            if (ship.getVerticalPos() != (fieldSize - 1)) {
                verticalPosMax = ship.getVerticalPos() + 2;
            } else {
                verticalPosMax = fieldSize;
            }
            if ((ship.getHorizontalPos() + ship.getLength()) != fieldSize) {
                horizontalPosMax = ship.getHorizontalPos() + ship.getLength() + 1;
            } else {
                horizontalPosMax = fieldSize;
            }
        }

        for (Integer x = horizontalPosMin; x < horizontalPosMax; ++x) {
            for (Integer y = verticalPosMin; y < verticalPosMax; ++y) {
                if (cells.get(x).get(y) == CellStatus.FREE) {
                    cells.get(x).set(y, CellStatus.BLOCKED);
                }
            }
        }
    }
}
