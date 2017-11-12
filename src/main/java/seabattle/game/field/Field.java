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

    public Field() {
    }

    public Field(List<Ship> ships) {
        ships.forEach(ship -> {
            if (ship.getOrientation() == ShipOrientation.VERTICAL) {
                for (int i = 0; i < ship.getLength(); ++i) {
                    cells.get(ship.getRowPos()).set(ship.getColPos() + i, CellStatus.OCCUPIED);
                }
            } else {
                for (int i = 0; i < ship.getLength(); ++i) {
                    cells.get(ship.getRowPos() + i).set(ship.getColPos(), CellStatus.OCCUPIED);
                }
            }
        });
    }

    public Integer getFieldSize() {
        return fieldSize;
    }

    public CellStatus fire(Integer rowPos, Integer colPos) {
        if (rowPos < 0 || rowPos >= fieldSize || colPos < 0 || colPos >= fieldSize) {
            throw new IllegalArgumentException("Given position is out of bounds!");
        }
        switch (cells.get(rowPos).get(colPos)) {
            case FREE:
                cells.get(rowPos).set(colPos, CellStatus.BLOCKED);
                return CellStatus.BLOCKED;
            case OCCUPIED:
                cells.get(colPos).set(colPos, CellStatus.DESTRUCTED);
                return CellStatus.DESTRUCTED;
            case ON_FIRE:
                throw new IllegalArgumentException("Given position is already checked!");
            case DESTRUCTED:
                throw new IllegalArgumentException("Given position is already checked!");
            case BLOCKED:
                throw new IllegalArgumentException("Given position is already checked!");
            default:
                throw new RuntimeException("Internal problem!");
        }
    }

    public CellStatus getCellStatus(Integer rowPos, Integer colPos) {
        if (rowPos < 0 || rowPos >= fieldSize || colPos < 0 || colPos >= fieldSize) {
            throw new IllegalArgumentException("Given position is out of bounds!");
        }
        return cells.get(rowPos).get(colPos);
    }

    public void setCellStatus(Integer rowPos, Integer colPos, CellStatus status) {
        if (rowPos < 0 || rowPos >= fieldSize || colPos < 0 || colPos >= fieldSize) {
            throw new IllegalArgumentException("Given position is out of bounds!");
        }
        cells.get(rowPos).set(colPos, status);
    }

    public Boolean shipKilled(Ship ship) {
        if (ship.getOrientation() == ShipOrientation.VERTICAL) {
            for (Integer y = ship.getColPos(); y < ship.getLength(); ++y) {
                if (getCellStatus(ship.getRowPos(), y) != CellStatus.ON_FIRE) {
                    return false;
                }
            }
        } else {
            for (Integer x = ship.getRowPos(); x < ship.getLength(); ++x) {
                if (getCellStatus(x, ship.getColPos()) != CellStatus.ON_FIRE) {
                    return false;
                }
            }
        }

        return true;
    }

    public void fillCellsAroundShip(Ship ship) {
        Integer rowPosMin = 0;
        if (ship.getRowPos() != 0) {
            rowPosMin = ship.getRowPos() - 1;
        }
        Integer colPosMin = 0;
        if (ship.getColPos() != 0) {
            rowPosMin = ship.getColPos() - 1;
        }

        Integer rowPosMax;
        Integer colPosMax;
        if (ship.getOrientation() == ShipOrientation.VERTICAL) {
            if (ship.getRowPos() != (fieldSize - 1)) {
                rowPosMax = ship.getRowPos() + 2;
            } else {
                rowPosMax = fieldSize;
            }
            if ((ship.getColPos() + ship.getLength()) != fieldSize) {
                colPosMax = ship.getColPos() + ship.getLength() + 1;
            } else {
                colPosMax = fieldSize;
            }
        } else {
            if (ship.getColPos() != (fieldSize - 1)) {
                colPosMax = ship.getColPos() + 2;
            } else {
                colPosMax = fieldSize;
            }
            if ((ship.getRowPos() + ship.getLength()) != fieldSize) {
                rowPosMax = ship.getRowPos() + ship.getLength() + 1;
            } else {
                rowPosMax = fieldSize;
            }
        }

        for (Integer x = rowPosMin; x < rowPosMax; ++x) {
            for (Integer y = colPosMin; y < colPosMax; ++y) {
                if (cells.get(x).get(y) == CellStatus.FREE) {
                    cells.get(x).set(y, CellStatus.BLOCKED);
                } else if (cells.get(x).get(y) == CellStatus.ON_FIRE) {
                    cells.get(x).set(y, CellStatus.DESTRUCTED);
                }
            }
        }
    }

}
