package seabattle.game.gamesession;

import seabattle.game.field.Cell;
import seabattle.game.field.CellStatus;
import seabattle.game.field.Field;
import seabattle.game.player.Player;

import javax.validation.constraints.NotNull;

public class SessionSetup implements SessionState {
    private GameSession gameSession;

    private Player damagedPlayer;

    private Field damagedField;

    private Field field1;

    private Field field2;

    public SessionSetup(GameSession gameSession) {
        this.gameSession = gameSession;
        this.damagedPlayer = null;
        this.damagedField = null;
        this.field1 = null;
        this.field2 = null;
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
        if (player.equals(gameSession.getPlayer1())) {
            this.damagedField = field1;
            this.damagedPlayer = gameSession.getPlayer1();
        } else if (player.equals(gameSession.getPlayer2())) {
            this.damagedField = field2;
            this.damagedPlayer = gameSession.getPlayer2();
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
    public SessionState nextPhase() {
        if (field1 == null || field2 == null || gameSession.getPlayer1() == null || gameSession.getPlayer2() == null
                || damagedField == null || damagedPlayer == null) {
            return null;
        }
        SessionActive nextPhaseSession = new SessionActive(this);
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

    @Override
    public GameSession getGameSession() {
        return gameSession;
    }
}
