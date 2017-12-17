package seabattle.game.gamesession;

import seabattle.game.field.Cell;
import seabattle.game.field.CellStatus;
import seabattle.game.field.Field;
import seabattle.game.player.Player;

import javax.validation.constraints.NotNull;

public class GameSessionEndgame implements GameSession {

    @NotNull
    private Long sessionId;
    @NotNull
    private Player player1;
    @NotNull
    private Player player2;
    @NotNull
    private Player damagedPlayer;
    @NotNull
    private Field damagedField;
    @NotNull
    private Field field1;
    @NotNull
    private Field field2;

    GameSessionEndgame(@NotNull GameSessionActive sessionActive) {
        this.sessionId = sessionActive.getSessionId();
        this.player1 = sessionActive.getPlayer1();
        this.player2 = sessionActive.getPlayer2();
        this.damagedPlayer = sessionActive.getDamagedPlayer();
        this.damagedField = sessionActive.getDamagedField();
        this.field1 = sessionActive.getField1();
        this.field2 = sessionActive.getField2();
    }

    @NotNull
    @Override
    public Long getSessionId() {
        return sessionId;
    }

    @NotNull
    @Override
    public Player getPlayer1() {
        return player1;
    }

    @NotNull
    @Override
    public Long getPlayer1Id() {
        return player1.getPlayerId();
    }

    @Override
    public void setPlayer1(@NotNull Player player1) throws IllegalStateException {
        throw new IllegalStateException("Game is in active phase!");
    }

    @NotNull
    @Override
    public Player getPlayer2() {
        return player2;
    }

    @NotNull
    @Override
    public Long getPlayer2Id() {
        return player2.getPlayerId();
    }

    @Override
    public void setPlayer2(@NotNull Player player2) throws IllegalStateException {
        throw new IllegalStateException("Game is in active phase!");
    }

    @NotNull
    @Override
    public Boolean bothFieldsAccepted() {
        return Boolean.TRUE;
    }

    @NotNull
    @Override
    public Player getWinner() throws IllegalStateException {
        if (damagedPlayer.equals(player1)) {
            return player2;
        }
        return player1;
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
        if (damagedPlayer.equals(player1)) {
            return player2;
        }
        return player1;
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
    public GameSession nextPhase() {
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
}
