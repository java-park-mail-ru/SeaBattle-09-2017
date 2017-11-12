package seabattle.game.gamesession;

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

    public CellStatus makeMove(Integer horizontalPos, Integer verticalPos) {
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

        CellStatus cell = playField.fire(horizontalPos, verticalPos);

        switch (cell) {
            case BLOCKED:
                switch (status) {
                    case MOVE_P1:
                        if (damagedPlayer.allShipsDead()) {
                            status = GameSessionStatus.WIN_P1;
                        } else {
                            status = GameSessionStatus.MOVE_P2;
                        }
                        break;
                    case MOVE_P2:
                        if (damagedPlayer.allShipsDead()) {
                            status = GameSessionStatus.WIN_P2;
                        } else {
                            status = GameSessionStatus.MOVE_P1;
                        }
                        break;
                    default:
                        throw new IllegalStateException("Illegal state!");
                }
                break;
            case DESTRUCTED:
                for (Ship ship : damagedPlayer.getAliveShips()) {
                    if (ship.inShip(horizontalPos, verticalPos) && playField.shipKilled(ship)) {
                        damagedPlayer.getAliveShips().remove(ship);
                        damagedPlayer.getDeadShips().add(ship);
                        playField.fillCellsAroundShip(ship);
                    }
                }
                return cell;
            default:
                throw new IllegalStateException("Illegal state!");
        }
        return cell;
    }

}
