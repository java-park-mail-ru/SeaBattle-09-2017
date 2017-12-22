package seabattle.game.field;

import seabattle.game.ship.Ship;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@SuppressWarnings({"unused", "WeakerAccess"})
public final class Field {

    private static final Integer FIELD_SIZE = 10;
    private List<List<CellStatus>> cells = new ArrayList<>();

    public Field() {
        for (int i = 0; i < FIELD_SIZE; i++) {
            List<CellStatus> line = new ArrayList<>(Collections.nCopies(FIELD_SIZE, CellStatus.FREE));
            cells.add(line);
        }
    }

    public Field(List<Ship> ships) {
        for (int i = 0; i < FIELD_SIZE; i++) {
            List<CellStatus> line = new ArrayList<>(Collections.nCopies(FIELD_SIZE, CellStatus.FREE));
            cells.add(line);
        }
        ships.forEach(ship -> ship.getCells().forEach(cell -> setCellStatus(cell, CellStatus.OCCUPIED)));
    }

    public Integer getFieldSize() {
        return FIELD_SIZE;
    }

    public CellStatus getCellStatus(Cell cell) {
        if (cellOutOfBounds(cell)) {
            throw new IllegalArgumentException("Given position is out of bounds!");
        }
        return cells.get(cell.getRowPos()).get(cell.getColPos());
    }

    public void setCellStatus(Cell cell, CellStatus status) {
        if (cellOutOfBounds(cell)) {
            throw new IllegalArgumentException("Given position is out of bounds!");
        }
        cells.get(cell.getRowPos()).set(cell.getColPos(), status);
    }

    private void blockFreeSafeNoExcept(Cell cell) {
        if (!cellOutOfBounds(cell)) {
            if (getCellStatus(cell) == CellStatus.FREE) {
                setCellStatus(cell, CellStatus.BLOCKED);
            }
        }
    }

    public CellStatus fire(Cell cell) {
        switch (getCellStatus(cell)) {
            case FREE:
                setCellStatus(cell, CellStatus.BLOCKED);
                return CellStatus.BLOCKED;
            case OCCUPIED:
                setCellStatus(cell, CellStatus.ON_FIRE);
                return CellStatus.ON_FIRE;
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


    public Boolean shipKilled(Ship ship) {
        for (Cell cell : ship.getCells()) {
            if (getCellStatus(cell) != CellStatus.ON_FIRE) {
                return Boolean.FALSE;
            }
        }
        return Boolean.TRUE;
    }

    public void killShip(Ship ship) {
        ship.getCellsAroundShip().forEach(cell -> setCellStatus(cell, CellStatus.BLOCKED));
        ship.getCells().forEach(cell -> setCellStatus(cell, CellStatus.DESTRUCTED));
    }

    public Boolean cellOutOfBounds(Cell cell) {
        final Boolean checkRow = (cell.getRowPos() < 0) || (cell.getRowPos() >= FIELD_SIZE);
        final Boolean checkCol = (cell.getColPos() < 0) || (cell.getColPos() >= FIELD_SIZE);
        return checkCol || checkRow;
    }

    public void blockBorderForPlacement(final Ship ship) {
        Integer minBlockedRow;
        Integer minBlockedCol;
        if (ship.getIsVertical().equals(Boolean.FALSE)) {
            minBlockedRow = 0;
            minBlockedCol = FIELD_SIZE - ship.getLength() + 1;
        } else {
            minBlockedRow = getFieldSize() - ship.getLength() + 1;
            minBlockedCol = 0;
        }

        for (Integer blockedRow = minBlockedRow; blockedRow < getFieldSize(); ++blockedRow) {
            for (Integer blockedCol = minBlockedCol; blockedCol < FIELD_SIZE; ++blockedCol) {
                if (getCellStatus(Cell.of(blockedRow, blockedCol)).equals(CellStatus.FREE)) {
                    setCellStatus(Cell.of(blockedRow, blockedCol), CellStatus.BLOCKED);
                }
            }
        }
    }

    public void preventCollisionsOnPlacement(final Ship ship) {
        for (Integer row = 0; row < FIELD_SIZE; ++row) {
            for (Integer col = 0; col < FIELD_SIZE; ++col) {
                if (getCellStatus(Cell.of(row, col)) == CellStatus.OCCUPIED) {
                    Integer minBlockedRow = row - 1;
                    if (ship.getIsVertical().equals(Boolean.TRUE)) {
                        minBlockedRow -= ship.getLength() - 1;
                    }
                    if (minBlockedRow < 0) {
                        minBlockedRow = 0;
                    }
                    Integer maxBlockedRow = row + 1;
                    if (maxBlockedRow >= FIELD_SIZE) {
                        maxBlockedRow = FIELD_SIZE - 1;
                    }

                    Integer minBlockedCol = col - 1;
                    if (ship.getIsVertical().equals(Boolean.FALSE)) {
                        minBlockedCol -= ship.getLength() - 1;
                    }
                    if (minBlockedCol < 0) {
                        minBlockedCol = 0;
                    }
                    Integer maxBlockedCol = col + 1;
                    if (maxBlockedCol >= FIELD_SIZE) {
                        maxBlockedCol = FIELD_SIZE - 1;
                    }

                    for (Integer idxRow = minBlockedRow; idxRow <= maxBlockedRow; ++idxRow) {
                        for (Integer idxCol = minBlockedCol; idxCol <= maxBlockedCol; ++idxCol) {
                            if (getCellStatus(Cell.of(idxRow, idxCol)).equals(CellStatus.FREE)) {
                                setCellStatus(Cell.of(idxRow, idxCol), CellStatus.BLOCKED);
                            }
                        }
                    }
                }
            }
        }
    }

    public Integer countFreeCells() {
        Integer count = 0;
        for (List<CellStatus> row : cells) {
            for (CellStatus cellStatus : row) {
                if (cellStatus.equals(CellStatus.FREE)) {
                    ++count;
                }
            }
        }
        return count;
    }

    public void freeBlockedCells() {
        for (Integer row = 0; row < FIELD_SIZE; ++row) {
            for (Integer col = 0; col < FIELD_SIZE; ++col) {
                if (getCellStatus(Cell.of(row, col)).equals(CellStatus.BLOCKED)) {
                    setCellStatus(Cell.of(row, col), CellStatus.FREE);
                }
            }
        }
    }

    public List<Cell> possibleCells(final Cell cell) {
        List<Cell> result = new ArrayList<>();

        final Integer minDistance = 1;
        final Integer maxDistance = 4;

        Boolean checkVertical = true;
        Boolean checkHorizontal = true;

        Boolean shipIsHorizontal = shipIsHorizontal(cell.getRowPos(), cell.getColPos());
        if (shipIsHorizontal != null) {
            if (shipIsHorizontal.equals(Boolean.TRUE)) {
                checkVertical = false;
            } else {
                checkHorizontal = false;
            }
        }
        if (checkVertical) {
            for (Integer distance = minDistance; distance < maxDistance; ++distance) {
                Cell currentCell = Cell.of(cell.getRowPos() + distance, cell.getColPos());
                if (currentCell.getRowPos() >= FIELD_SIZE) {
                    break;
                }
                CellStatus cellStatus = getCellStatus(currentCell);
                if (cellStatus.equals(CellStatus.BLOCKED)) {
                    break;
                }
                if (!cellStatus.equals(CellStatus.ON_FIRE)) {
                    result.add(currentCell);
                    break;
                }
            }
            for (Integer distance = minDistance; distance < maxDistance; ++distance) {
                Cell currentCell = Cell.of(cell.getRowPos() - distance, cell.getColPos());
                if (currentCell.getRowPos() < 0) {
                    break;
                }
                CellStatus cellStatus = getCellStatus(currentCell);
                if (cellStatus.equals(CellStatus.BLOCKED)) {
                    break;
                }
                if (!cellStatus.equals(CellStatus.ON_FIRE)) {
                    result.add(currentCell);
                    break;
                }
            }
        }
        if (checkHorizontal) {
            for (Integer distance = minDistance; distance < maxDistance; ++distance) {
                Cell currentCell = Cell.of(cell.getRowPos(), cell.getColPos() - distance);
                if (currentCell.getColPos() < 0) {
                    break;
                }
                CellStatus cellStatus = getCellStatus(currentCell);
                if (cellStatus.equals(CellStatus.BLOCKED)) {
                    break;
                }
                if (!cellStatus.equals(CellStatus.ON_FIRE)) {
                    result.add(currentCell);
                    break;
                }
            }
            for (Integer distance = minDistance; distance < maxDistance; ++distance) {
                Cell currentCell = Cell.of(cell.getRowPos(), cell.getColPos() + distance);
                if (currentCell.getColPos() >= FIELD_SIZE) {
                    break;
                }
                CellStatus cellStatus = getCellStatus(currentCell);
                if (cellStatus.equals(CellStatus.BLOCKED)) {
                    break;
                }
                if (!cellStatus.equals(CellStatus.ON_FIRE)) {
                    result.add(currentCell);
                    break;
                }
            }
        }

        return result;
    }

    /**
     * created by MikeGus on 12.11.17
     * @param row: row position
     * @param col: col position
     * @return true if horizontal, false if vertical, null if undefined
     */
    public Boolean shipIsHorizontal(final Integer row, final Integer col) {

        Boolean isHorizontal = null;
        if (row > 0) {
            if (getCellStatus(Cell.of(row - 1, col)).equals(CellStatus.ON_FIRE)) {
                return false;
            }
        }
        if (row < FIELD_SIZE - 1) {
            if (getCellStatus(Cell.of(row + 1, col)).equals(CellStatus.ON_FIRE)) {
                return false;
            }
        }
        if (col > 0) {
            if (getCellStatus(Cell.of(row, col - 1)).equals(CellStatus.ON_FIRE)) {
                return true;
            }
        }
        if (col < FIELD_SIZE - 1) {
            if (getCellStatus(Cell.of(row, col + 1)).equals(CellStatus.ON_FIRE)) {
                return true;
            }
        }

        return null;
    }
}
