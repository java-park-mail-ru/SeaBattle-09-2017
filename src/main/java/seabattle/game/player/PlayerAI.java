package seabattle.game.player;

import seabattle.game.field.CellStatus;
import seabattle.game.field.Field;
import seabattle.game.ship.Ship;
import seabattle.game.ship.ShipOrientation;

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
        final ShipOrientation[] orientations = {ShipOrientation.VERTICAL, ShipOrientation.HORIZONTAL};

        for (Integer shipLength = maxShipLength; shipLength > 0; --shipLength) {
            for (Integer numberOfShips = 0; numberOfShips <= (maxShipLength - shipLength); ++numberOfShips) {

                Integer freeCells = field.getFieldSize() * field.getFieldSize();
                Integer randomOrientation = ThreadLocalRandom.current().nextInt(0, 2);

                Ship ship = new Ship(0, 0, shipLength, orientations[randomOrientation]);

                /* block bottom right */
                Integer minBlockedRow;
                Integer minBlockedCol;
                if (ship.getOrientation() == ShipOrientation.HORIZONTAL) {
                    minBlockedRow = field.getFieldSize() - 1;
                    minBlockedCol = field.getFieldSize() + 1 - shipLength;
                } else {
                    minBlockedRow = field.getFieldSize() + 1 - shipLength;
                    minBlockedCol = field.getFieldSize() - 1;
                }

                for (Integer blockedRow = minBlockedRow; blockedRow < field.getFieldSize(); ++blockedRow) {
                    for (Integer blockedCol = minBlockedCol; blockedCol < field.getFieldSize(); ++blockedCol) {
                        if (field.getCellStatus(blockedRow, blockedCol) == CellStatus.FREE) {
                            field.setCellStatus(blockedRow, blockedCol, CellStatus.BLOCKED);
                            --freeCells;
                        }
                    }

                }
                /* block field which lead to ship collisions */
                for (Integer row = 0; row < field.getFieldSize(); ++row) {
                    for (Integer col = 0; col < field.getFieldSize(); ++col) {
                        if (field.getCellStatus(row, col) == CellStatus.OCCUPIED) {
                            --freeCells;
                            for (Integer distance = 2; distance < (shipLength + 2); ++distance) {
                                if (ship.getOrientation() == ShipOrientation.HORIZONTAL) {
                                    if (((row - 1) > 0)
                                            && (field.getCellStatus(row - 1, col) == CellStatus.FREE)) {
                                        field.setCellStatus(row - 1, col, CellStatus.BLOCKED);
                                        --freeCells;
                                    }
                                    if (((col - distance) > 0)
                                            && (field.getCellStatus(row, col - distance) == CellStatus.FREE)) {
                                        field.setCellStatus(row, col - distance, CellStatus.BLOCKED);
                                        --freeCells;
                                    }
                                } else {
                                    if (((row - distance) > 0)
                                            && (field.getCellStatus(row - distance, col) == CellStatus.FREE)) {
                                        field.setCellStatus(row - distance, col, CellStatus.BLOCKED);
                                        --freeCells;
                                    }
                                    if (((col - 1) > 0)
                                            && (field.getCellStatus(row, col - 1) == CellStatus.FREE)) {
                                        field.setCellStatus(row, col - 1, CellStatus.BLOCKED);
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
                        if (field.getCellStatus(row, col) == CellStatus.FREE) {
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
                        if (field.getCellStatus(row, col) == CellStatus.BLOCKED) {
                            field.setCellStatus(row, col, CellStatus.FREE);
                        }
                    }
                }
            }
        }
        return generatedShips;
    }
}
