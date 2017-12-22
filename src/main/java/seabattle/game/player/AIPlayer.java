package seabattle.game.player;

import seabattle.authorization.views.UserView;
import seabattle.game.field.Cell;
import seabattle.game.field.CellStatus;
import seabattle.game.field.Field;
import seabattle.game.ship.Ship;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@SuppressWarnings({"unused", "FieldCanBeLocal"})
public  final class AIPlayer implements Player {

    private Long playerId = null;

    @NotNull
    private String username = "Mysterious stranger";

    private Integer score = null;

    private List<Ship> aliveShips = generateShips();
    private List<Ship> deadShips = new ArrayList<>();

    @Override
    public UserView getUser() {
        return null;
    }

    @Override
    public void setUser(UserView user) {

    }

    @Override
    public Integer getScore() {
        return null;
    }

    @Override
    public void setScore(Integer score) {
        this.score = score;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public void setShips(List<Ship> ships) {
        aliveShips = ships;
    }

    @Override
    public List<Ship> getAliveShips() {
        return aliveShips;
    }

    @Override
    public List<Ship> getDeadShips() {
        return deadShips;
    }

    @Override
    public Boolean allShipsDead() {
        return aliveShips.isEmpty();
    }

    @Override
    public Long getPlayerId() {
        return playerId;
    }

    public List<Ship> generateShips() {

        Field field = new Field();

        List<Ship> generatedShips = new ArrayList<>();
        final Integer maxShipLength = 4;
        final Boolean[] vertical = {Boolean.TRUE, Boolean.FALSE};

        for (Integer shipLength = maxShipLength; shipLength > 0; --shipLength) {
            for (Integer numberOfShips = 0; numberOfShips <= (maxShipLength - shipLength); ++numberOfShips) {
                Integer randomOrientation = ThreadLocalRandom.current().nextInt(0, 2);
                Ship ship = new Ship(0, 0, shipLength, vertical[randomOrientation]);

                field.blockBorderForPlacement(ship);
                field.preventCollisionsOnPlacement(ship);
                Integer freeCells = field.countFreeCells();

                /* generation */
                Integer placementIndex = ThreadLocalRandom.current().nextInt(1, freeCells + 1);
                Cell taken = new Cell(0, 0);

                for (Integer row = 0; row < field.getFieldSize() && placementIndex > 0; ++row) {
                    for (Integer col = 0; col < field.getFieldSize() && placementIndex > 0; ++col) {
                        if (field.getCellStatus(Cell.of(row, col)).equals(CellStatus.FREE)) {
                            --placementIndex;
                            if (placementIndex == 0) {
                                taken.setRowPos(row);
                                taken.setColPos(col);
                            }
                        }
                    }
                }
                ship.setRowPos(taken.getRowPos());
                ship.setColPos(taken.getColPos());
                generatedShips.add(ship);

                for (Cell cell : ship.getCells()) {
                    field.setCellStatus(cell, CellStatus.OCCUPIED);
                }

                field.freeBlockedCells();
            }
        }
        return generatedShips;
    }

    @SuppressWarnings("Duplicates")
    public Cell makeDecision(final Field field) {

        Integer freeCells = 0;
        final Integer maxDistance = 3;
        final Integer minDistance = 1;

        for (Integer row =  0; row < field.getFieldSize(); ++row) {
            for (Integer col = 0; col < field.getFieldSize(); ++col) {
                CellStatus currentCellStatus = field.getCellStatus(Cell.of(row, col));
                if (currentCellStatus.equals(CellStatus.ON_FIRE)) {
                    List<Cell> possibleCells = field.possibleCells(Cell.of(row, col));
                    Integer randomPosition = ThreadLocalRandom.current().nextInt(0, possibleCells.size());
                    return possibleCells.get(randomPosition);
                }
                freeCells += possibleCellAddition(currentCellStatus);
            }
        }

        Integer randomPosition = ThreadLocalRandom.current().nextInt(1, freeCells + 1);

        for (Integer row =  0; row < field.getFieldSize(); ++row) {
            for (Integer col = 0; col < field.getFieldSize(); ++col) {
                CellStatus currentCellStatus = field.getCellStatus(Cell.of(row, col));
                if (currentCellStatus.equals(CellStatus.FREE) || currentCellStatus.equals(CellStatus.OCCUPIED)) {
                    --randomPosition;
                    if (randomPosition == 0) {
                        return new Cell(row, col);
                    }
                }
            }
        }
        throw new RuntimeException("Wrong field structure!");
    }

    private Integer possibleCellAddition(CellStatus cellStatus) {
        if (cellStatus.equals(CellStatus.FREE) || cellStatus.equals(CellStatus.OCCUPIED)) {
            return 1;
        }
        return 0;
    }

}
