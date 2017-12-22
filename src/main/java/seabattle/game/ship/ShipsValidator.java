package seabattle.game.ship;

import org.springframework.stereotype.Service;
import seabattle.game.field.Cell;
import seabattle.game.field.CellStatus;
import seabattle.game.field.Field;

import java.util.List;

@Service
public class ShipsValidator {
    private static final Integer FIELD_SIZE = 10;
    private static final Integer FOUR_DECKER_COUNT = 1;
    private static final Integer THREE_DECKER_COUNT = 2;
    private static final Integer TWO_DECKER_COUNT = 3;
    private static final Integer ONE_DECKER_COUNT = 4;

    @SuppressWarnings("OverlyComplexMethod")
    public boolean isValidShips(List<Ship> ships) {
        final Field field = new Field();
        Integer fourDeckerCount = 0;
        Integer threeDeckerCount = 0;
        Integer twoDeckerCount = 0;
        Integer oneDeckerCount = 0;
        final Integer fourDecker = 4;
        final Integer threeDecker = 3;
        final Integer twoDecker = 2;
        final Integer oneDecker = 1;
        for (Ship ship: ships) {
            if (ship.getLength().equals(oneDecker)) {
                oneDeckerCount++;
                if (oneDeckerCount > ONE_DECKER_COUNT) {
                    return false;
                }
            } else if (ship.getLength().equals(twoDecker)) {
                twoDeckerCount++;
                if (threeDeckerCount > TWO_DECKER_COUNT) {
                    return false;
                }
            } else if (ship.getLength().equals(threeDecker)) {
                threeDeckerCount++;
                if (threeDeckerCount > THREE_DECKER_COUNT) {
                    return false;
                }
            } else if (ship.getLength().equals(fourDecker)) {
                fourDeckerCount++;
                if (fourDeckerCount > FOUR_DECKER_COUNT) {
                    return false;
                }
            } else {
                return false;
            }

            for (Cell cell : ship.getCells()) {
                if (field.cellOutOfBounds(cell)) {
                    return false;
                }
                field.setCellStatus(cell, CellStatus.OCCUPIED);
            }

            for (Cell cell : ship.getCellsAroundShip()) {
                if (field.getCellStatus(cell) == CellStatus.OCCUPIED) {
                    return false;
                }
            }

        }

        if (!oneDeckerCount.equals(ONE_DECKER_COUNT) || !twoDeckerCount.equals(TWO_DECKER_COUNT)) {
            return false;
        }
        return (threeDeckerCount.equals(THREE_DECKER_COUNT) && fourDeckerCount.equals(FOUR_DECKER_COUNT));
    }

}
