package seabattle.game.gamesession;

import seabattle.game.field.Cell;
import seabattle.game.field.CellStatus;
import seabattle.game.field.Field;
import seabattle.game.player.Player;

import javax.validation.constraints.NotNull;

public class SessionEndGame implements SessionState {

    @NotNull
    private GameSession gameSession;
    @NotNull
    private Player damagedPlayer;
    @NotNull
    private Field damagedField;
    @NotNull
    private Field field1;
    @NotNull
    private Field field2;

    public SessionEndGame(@NotNull SessionActive sessionActive) {
        this.gameSession = sessionActive.getGameSession();
        this.damagedPlayer = sessionActive.getDamagedPlayer();
        this.damagedField = sessionActive.getDamagedField();
        this.field1 = sessionActive.getField1();
        this.field2 = sessionActive.getField2();
    }

    @NotNull
    @Override
    public Boolean bothFieldsAccepted() {
        return Boolean.TRUE;
    }

    @NotNull
    @Override
    public Player getWinner() throws IllegalStateException {
        if (damagedPlayer.equals(gameSession.getPlayer1())) {
            return gameSession.getPlayer2();
        }
        return gameSession.getPlayer1();
    }

    @Override
    public void setField1(@NotNull Field field) throws IllegalStateException {
        throw new IllegalStateException("Game is in active phase!");
    }

    @Override
    public void setField2(@NotNull Field field) throws IllegalStateException {
        throw new IllegalStateException("Game is in active phase!");
    }

    @Override
    public void setDamagedSide(@NotNull Player player) throws IllegalStateException {
        throw new IllegalStateException("Game is in active phase!");
    }

    @NotNull
    @Override
    public Player getAttackingPlayer() {
        if (damagedPlayer.equals(gameSession.getPlayer1())) {
            return gameSession.getPlayer2();
        }
        return gameSession.getPlayer1();
    }

    @NotNull
    @Override
    public Player getDamagedPlayer() {
        return damagedPlayer;
    }

    @NotNull
    @Override
    public Field getDamagedField() {
        return damagedField;
    }

    @Override
    public SessionState nextPhase() {
        return null;
    }

    @NotNull
    @Override
    public Field getField1() {
        return field1;
    }

    @NotNull
    @Override
    public Field getField2() {
        return field2;
    }

    @NotNull
    @Override
    public CellStatus makeMove(Cell cell) throws IllegalStateException {
        throw new IllegalStateException("Game has already ended!");
    }

    @Override
    public GameSession getGameSession() {
        return gameSession;
    }
}
