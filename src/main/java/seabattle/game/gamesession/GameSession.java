package seabattle.game.gamesession;

import seabattle.game.field.Cell;
import seabattle.game.field.CellStatus;
import seabattle.game.field.Field;
import seabattle.game.player.Player;
import seabattle.game.ship.Ship;

import javax.validation.constraints.NotNull;
import java.util.concurrent.atomic.AtomicLong;


@SuppressWarnings({"FieldCanBeLocal", "unused"})
public class GameSession {
    private static final AtomicLong SESSION_ID_GENERATOR = new AtomicLong(0);

    @NotNull
    private GameSessionStatus status;
    @NotNull
    private Long sessionId;
    @NotNull
    private Player player1;
    @NotNull
    private Player player2;

    private Player damagedPlayer;
    private Field damagedField;

    private Field field1 = null;
    private Field field2 = null;

    @NotNull
    private GameSessionService gameSessionService;


    GameSession(@NotNull Player player1, @NotNull Player player2,
                @NotNull GameSessionService gameSessionService) {
        this.sessionId = SESSION_ID_GENERATOR.getAndIncrement();
        this.player1 = player1;
        this.player2 = player2;
        this.gameSessionService = gameSessionService;

        this.status = GameSessionStatus.SETUP;
    }

    @NotNull
    Player getPlayer1() {
        return player1;
    }

    public void setPlayer1(@NotNull Player player1) {
        this.player1 = player1;
    }

    @NotNull
    Player getPlayer2() {
        return player2;
    }

    public void  setPlayer2(@NotNull Player player2) {
        this.player2 = player2;
    }

    Boolean bothFieldsAccepted() {
        return field1 != null && field2 != null;
    }

    Player getWinner() {
        if (status == GameSessionStatus.WIN_P1) {
            return player1;
        } else if (status == GameSessionStatus.WIN_P2) {
            return player2;
        }
        throw new IllegalStateException("Game did not end!");
    }

    public void setField1(@NotNull Field field) {
        this.field1 = field;
    }

    public void setField2(@NotNull Field field) {
        this.field2 = field;
    }

    void setDamagedSide(@NotNull Player player) {
        if (player == player1) {
            this.damagedField = field1;
            this.damagedPlayer = player;
        } else if (player == player2) {
            this.damagedField = field2;
            this.damagedPlayer = player2;
        }
        throw new IllegalArgumentException("Player not in current session!");
    }

    public Boolean toGamePhase() {
        if (this.status != GameSessionStatus.SETUP || this.field1 == null || this.field2 == null) {
            return Boolean.FALSE;
        }

        if (this.damagedPlayer == player1) {
            this.status = GameSessionStatus.MOVE_P2;
        } else {
            this.status = GameSessionStatus.MOVE_P1;
        }

        return Boolean.TRUE;
    }

    public CellStatus makeMove(Cell cell) throws IllegalStateException {

        if (this.status != GameSessionStatus.MOVE_P1 && this.status != GameSessionStatus.MOVE_P2) {
            throw new IllegalStateException("Illegal state for move!");
        }

        switch (damagedField.fire(cell)) {
            case BLOCKED:
                switch (status) {
                    case MOVE_P1:
                        status = GameSessionStatus.MOVE_P2;
                        damagedField = field1;
                        damagedPlayer = player1;
                        return CellStatus.BLOCKED;
                    case MOVE_P2:
                        status = GameSessionStatus.MOVE_P1;
                        damagedField = field2;
                        damagedPlayer = player2;
                        return CellStatus.BLOCKED;
                    default:
                        throw new IllegalStateException("Illegal state!");
                }
            case ON_FIRE:
                for (Ship ship : damagedPlayer.getAliveShips()) {
                    if (ship.inShip(cell) && damagedField.shipKilled(ship)) {
                        damagedPlayer.getAliveShips().remove(ship);
                        damagedPlayer.getDeadShips().add(ship);
                        damagedField.killShip(ship);
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
