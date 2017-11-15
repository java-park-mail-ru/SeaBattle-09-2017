package seabattle.game.gamesession;

import seabattle.game.field.Cell;
import seabattle.game.field.CellStatus;
import seabattle.game.field.Field;
import seabattle.game.player.Player;
import seabattle.game.ship.Ship;

@SuppressWarnings("unused")
public class GameSession {
    private GameSessionStatus status;
    private Player player1;
    private Player player2;
    private Field field1;
    private Field field2;

    public GameSession(Player player1, Player player2) {
        this.status = GameSessionStatus.SETUP;
        this.player1 = player1;
        this.player2 = player2;
    }

    public CellStatus makeMove(Cell cell) {
        Field playField;
        Player damagedPlayer;

        switch (status) {
            case MOVE_P1:
                playField = field2;
                damagedPlayer = player2;
                break;
            case MOVE_P2:
                playField = field1;
                damagedPlayer = player1;
                break;
            default:
                throw new IllegalStateException("Illegal state of game session!");
        }

        switch (playField.fire(cell)) {
            case BLOCKED:
                switch (status) {
                    case MOVE_P1:
                        status = GameSessionStatus.MOVE_P2;
                        return CellStatus.BLOCKED;
                    case MOVE_P2:
                        status = GameSessionStatus.MOVE_P1;
                        return CellStatus.BLOCKED;
                    default:
                        throw new IllegalStateException("Illegal state!");
                }
            case ON_FIRE:
                for (Ship ship : damagedPlayer.getAliveShips()) {
                    if (ship.inShip(cell) && playField.shipKilled(ship)) {
                        damagedPlayer.getAliveShips().remove(ship);
                        damagedPlayer.getDeadShips().add(ship);
                        playField.killShip(ship);
                        if (damagedPlayer.allShipsDead()) {
                            switch (status) {
                                case MOVE_P1:
                                    status = GameSessionStatus.WIN_P1;
                                    break;
                                case MOVE_P2:
                                    status = GameSessionStatus.WIN_P2;
                                    break;
                                default:
                                    throw new IllegalStateException("Illegal state!");
                            }
                        }
                        return CellStatus.DESTRUCTED;
                    }
                }
                return CellStatus.ON_FIRE;
            default:
                throw new IllegalStateException("Illegal state!");
        }
    }
}
