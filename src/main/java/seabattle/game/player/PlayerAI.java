package seabattle.game.player;

import seabattle.game.field.Cell;
import seabattle.game.field.CellStatus;
import seabattle.game.field.Field;
import seabattle.game.ship.Ship;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@SuppressWarnings("unused")
public  final class PlayerAI extends Player {

    public PlayerAI(Field field) {
        super(generateShips());
    }

    private static List<Ship> generateShips() {

        Field field = new Field();

        List<Ship> generatedShips = new ArrayList<>();
        final Integer maxShipLength = 4;
        final Boolean[] vertical = {Boolean.TRUE, Boolean.FALSE};

        for (Integer shipLength = maxShipLength; shipLength > 0; --shipLength) {
            for (Integer numberOfShips = 0; numberOfShips <= (maxShipLength - shipLength); ++numberOfShips) {

                Integer freeCells = field.getFieldSize() * field.getFieldSize();
                Integer randomOrientation = ThreadLocalRandom.current().nextInt(0, 2);

                Ship ship = new Ship(0, 0, shipLength, vertical[randomOrientation]);

                /* block bottom right */
                Integer minBlockedRow;
                Integer minBlockedCol;
                if (ship.getIsVertical() == Boolean.FALSE) {
                    minBlockedRow = field.getFieldSize() - 1;
                    minBlockedCol = field.getFieldSize() + 1 - shipLength;
                } else {
                    minBlockedRow = field.getFieldSize() + 1 - shipLength;
                    minBlockedCol = field.getFieldSize() - 1;
                }

                for (Integer blockedRow = minBlockedRow; blockedRow < field.getFieldSize(); ++blockedRow) {
                    for (Integer blockedCol = minBlockedCol; blockedCol < field.getFieldSize(); ++blockedCol) {
                        if (field.getCellStatus(Cell.of(blockedRow, blockedCol)) == CellStatus.FREE) {
                            field.setCellStatus(Cell.of(blockedRow, blockedCol), CellStatus.BLOCKED);
                            --freeCells;
                        }
                    }

                }
                /* block field which lead to ship collisions */
                for (Integer row = 0; row < field.getFieldSize(); ++row) {
                    for (Integer col = 0; col < field.getFieldSize(); ++col) {
                        if (field.getCellStatus(Cell.of(row, col)) == CellStatus.OCCUPIED) {
                            --freeCells;
                            for (Integer distance = 2; distance < (shipLength + 2); ++distance) {
                                if (ship.getIsVertical() == Boolean.FALSE) {
                                    if (((row - 1) > 0)
                                            && (field.getCellStatus(Cell.of(row - 1, col)) == CellStatus.FREE)) {
                                        field.setCellStatus(Cell.of(row - 1, col), CellStatus.BLOCKED);
                                        --freeCells;
                                    }
                                    if (((col - distance) > 0)
                                            && (field.getCellStatus(Cell.of(row, col - distance)) == CellStatus.FREE)) {
                                        field.setCellStatus(Cell.of(row, col - distance), CellStatus.BLOCKED);
                                        --freeCells;
                                    }
                                } else {
                                    if (((row - distance) > 0)
                                            && (field.getCellStatus(Cell.of(row - distance, col)) == CellStatus.FREE)) {
                                        field.setCellStatus(Cell.of(row - distance, col), CellStatus.BLOCKED);
                                        --freeCells;
                                    }
                                    if (((col - 1) > 0)
                                            && (field.getCellStatus(Cell.of(row, col - 1)) == CellStatus.FREE)) {
                                        field.setCellStatus(Cell.of(row, col - 1), CellStatus.BLOCKED);
                                        --freeCells;
                                    }
                                }

                            }
                        }
                    }
                }

                /* generation */
                Integer placementIndex = ThreadLocalRandom.current().nextInt(0, freeCells);
                for (Integer row = 0; row < field.getFieldSize() && placementIndex > 0; ++row) {
                    for (Integer col = 0; col < field.getFieldSize() && placementIndex > 0; ++col) {
                        if (field.getCellStatus(Cell.of(row, col)) == CellStatus.FREE) {
                            --placementIndex;
                            if (placementIndex == 0) {
                                ship.setRowPos(row);
                                ship.setColPos(col);
                                generatedShips.add(ship);
                            }
                        }
                    }
                }


                /* prepare matrix for next iteration */
                for (Integer row = 0; row < field.getFieldSize(); ++row) {
                    for (Integer col = 0; col < field.getFieldSize(); ++col) {
                        if (field.getCellStatus(Cell.of(row, col)) == CellStatus.BLOCKED) {
                            field.setCellStatus(Cell.of(row, col), CellStatus.FREE);
                        }
                    }
                }
            }
        }
        return generatedShips;
    }

    @SuppressWarnings("Duplicates")
    public Cell makeDecision(final Field field) {

        Integer freeCells = 0;
        final Integer maxDistance = 3;

        for (Integer row =  0; row < field.getFieldSize(); ++row) {
            for (Integer col = 0; col < field.getFieldSize(); ++col) {
                CellStatus currentCellStatus = field.getCellStatus(Cell.of(row, col));
                switch (currentCellStatus) {
                    case ON_FIRE:
                        Integer checkRow = row;
                        Integer checkCol = col;

                        Boolean isHorizontal = shipIsHorizontal(field, row, col);
                        if (isHorizontal != Boolean.FALSE) {
                            for (Integer distance = 0; distance < maxDistance; ++distance) {
                                CellStatus cellStatus = field.getCellStatus(Cell.of(row, col - distance));
                                if (cellStatus != CellStatus.ON_FIRE) {
                                    if (cellStatus == CellStatus.BLOCKED) {
                                        break;
                                    }
                                } else {
                                    return new Cell(row, col - distance);
                                }
                            }
                            for (Integer distance = 0; distance < maxDistance; ++distance) {
                                CellStatus cellStatus = field.getCellStatus(Cell.of(row, col + distance));
                                if (cellStatus != CellStatus.ON_FIRE) {
                                    if (cellStatus == CellStatus.BLOCKED) {
                                        break;
                                    }
                                } else {
                                    return new Cell(row, col - distance);
                                }
                            }
                        } else {
                            for (Integer distance = 0; distance < maxDistance; ++distance) {
                                CellStatus cellStatus = field.getCellStatus(Cell.of(row - distance, col));
                                if (cellStatus != CellStatus.ON_FIRE) {
                                    if (cellStatus == CellStatus.BLOCKED) {
                                        break;
                                    }
                                } else {
                                    return new Cell(row - distance, col);
                                }
                            }
                            for (Integer distance = 0; distance < maxDistance; ++distance) {
                                CellStatus cellStatus = field.getCellStatus(Cell.of(row + distance, col));
                                if (cellStatus != CellStatus.ON_FIRE) {
                                    if (cellStatus == CellStatus.BLOCKED) {
                                        break;
                                    }
                                } else {
                                    return new Cell(row + distance, col);
                                }
                            }
                        }
                        break;
                    case FREE:
                        ++freeCells;
                        break;
                    case OCCUPIED:
                        ++freeCells;
                        break;
                    default:
                        break;
                }
            }
        }

        Integer randomPosition = ThreadLocalRandom.current().nextInt(0, freeCells);

        for (Integer row =  0; row < field.getFieldSize(); ++row) {
            for (Integer col = 0; col < field.getFieldSize(); ++col) {
                CellStatus currentCellStatus = field.getCellStatus(Cell.of(row, col));
                --freeCells;
                if (freeCells == 0) {
                    if (currentCellStatus == CellStatus.FREE || currentCellStatus == CellStatus.OCCUPIED) {
                        return new Cell(row, col);
                    }
                }
            }
        }
        throw new RuntimeException("Wrong field structure!");
    }

    /**
     * created by MikeGus on 12.11.17
     * @param field: game field
     * @param row: row position
     * @param col: col position
     * @return true if horizontal, false if vertical, null if undefined
     */
    private Boolean shipIsHorizontal(final Field field, final Integer row, final Integer col) {

        Boolean isHorizontal = null;
        if (row > 0) {
            if (field.getCellStatus(Cell.of(row - 1, col)) == CellStatus.ON_FIRE) {
                return true;
            }
        }
        if (row < field.getFieldSize() - 1) {
            if (field.getCellStatus(Cell.of(row + 1, col)) == CellStatus.ON_FIRE) {
                return true;
            }
        }
        if (col > 0) {
            if (field.getCellStatus(Cell.of(row, col - 1)) == CellStatus.ON_FIRE) {
                return false;
            }
        }
        if (col < field.getFieldSize() - 1) {
            return false;
        }

        return null;
    }
}
