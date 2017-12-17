package seabattle.game.gamesession;

import seabattle.game.field.Cell;
import seabattle.game.field.CellStatus;
import seabattle.game.field.Field;
import seabattle.game.player.Player;
import javax.validation.constraints.NotNull;
import java.util.concurrent.atomic.AtomicLong;


public class GameSessionSetup implements GameSession {
    private static final AtomicLong SESSION_ID_GENERATOR = new AtomicLong(0);

    @NotNull
    private Long sessionId;

    @NotNull
    private Player player1;
    @NotNull
    private Player player2;

    private Player damagedPlayer = null;

    private Field damagedField = null;

    private Field field1 = null;

    private Field field2 = null;

    GameSessionSetup(@NotNull Player player1, @NotNull Player player2) {
        this.sessionId = SESSION_ID_GENERATOR.getAndIncrement();
        this.player1 = player1;
        this.player2 = player2;
    }

    @Override
    @NotNull
    public Long getSessionId() {
        return sessionId;
    }

    @Override
    @NotNull
    public Player getPlayer1() {
        return player1;
    }

    @NotNull
    public Long getPlayer1Id() {
        return player1.getPlayerId();
    }

    @NotNull
    public void setPlayer1(@NotNull Player player1) {
        this.player1 = player1;
    }

    @NotNull
    public Player getPlayer2() {
        return player2;
    }

    @NotNull
    public Long getPlayer2Id() {
        return player2.getPlayerId();
    }

    @Override
    public void  setPlayer2(@NotNull Player player2) {
        this.player2 = player2;
    }

    @Override
    public Boolean bothFieldsAccepted() {
        return field1 != null && field2 != null;
    }

    @Override
    public Player getWinner() {
        throw new IllegalStateException("Game is in setup mode!");
    }

    @Override
    public void setField1(@NotNull Field field) {
        this.field1 = field;
    }

    @Override
    public void setField2(@NotNull Field field) {
        this.field2 = field;
    }

    @Override
    public void setDamagedSide(@NotNull Player player) {
        if (player == player1) {
            this.damagedField = field1;
            this.damagedPlayer = player1;
        } else if (player == player2) {
            this.damagedField = field2;
            this.damagedPlayer = player2;
        } else {
            throw new IllegalArgumentException("Player not in current session!");
        }
    }

    @Override
    public Player getAttackingPlayer() throws IllegalStateException {
        throw new IllegalStateException("Game is in setup phase!");
    }

    @Override
    public Player getDamagedPlayer() {
        return damagedPlayer;
    }

    @Override
    public Field getDamagedField() {
        return damagedField;
    }

    @Override
    public GameSession nextPhase() {
        if (field1 == null || field2 == null || player1 == null || player2 == null
                || damagedField == null || damagedPlayer == null) {
            return null;
        }
        GameSessionActive nextPhaseSession = new GameSessionActive(this);
        return nextPhaseSession;
    }

    @Override
    public Field getField1() {
        return field1;
    }

    @Override
    public Field getField2() {
        return field2;
    }

    @Override
    public CellStatus makeMove(Cell cell) throws IllegalStateException {
        throw new IllegalStateException("Game is in setup phase!");
    }

}
