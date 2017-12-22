package seabattle.game.gamesession;

import seabattle.game.field.Cell;
import seabattle.game.field.CellStatus;
import seabattle.game.field.Field;
import seabattle.game.player.Player;

import javax.validation.constraints.NotNull;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;

public class GameSessionImpl implements GameSession {
    private static final AtomicLong SESSION_ID_GENERATOR = new AtomicLong(0);

    @NotNull
    private final Long sessionId;

    @NotNull
    private final Player player1;
    @NotNull
    private final Player player2;

    private AtomicReference<SessionState> sessionStateAtomicReference;

    public GameSessionImpl(@NotNull Player player1, @NotNull Player player2) {
        this.sessionId = SESSION_ID_GENERATOR.getAndIncrement();
        this.player1 = player1;
        this.player2 = player2;
        final SessionState sessionState = new SessionSetup(this);
        this.sessionStateAtomicReference = new AtomicReference<>(sessionState);
    }

    @Override
    public Long getSessionId() {
        return sessionId;
    }

    @Override
    public Player getPlayer1() {
        return player1;
    }

    @Override
    public Long getPlayer1Id() {
        return player1.getPlayerId();
    }

    @Override
    public Player getPlayer2() {
        return player2;
    }

    @Override
    public Long getPlayer2Id() {
        return player2.getPlayerId();
    }


    @Override
    public Boolean bothFieldsAccepted() {
        return sessionStateAtomicReference.get().bothFieldsAccepted();
    }

    @Override
    public Player getWinner() throws IllegalStateException {
        return sessionStateAtomicReference.get().getWinner();
    }

    @Override
    public void setField1(@NotNull Field field) throws IllegalStateException {
        sessionStateAtomicReference.get().setField1(field);
    }

    @Override
    public void setField2(@NotNull Field field) throws IllegalStateException {
        sessionStateAtomicReference.get().setField2(field);
    }

    @Override
    public void setDamagedSide(@NotNull Player player) throws IllegalStateException {
        sessionStateAtomicReference.get().setDamagedSide(player);
    }

    @Override
    public Player getAttackingPlayer() {
        return sessionStateAtomicReference.get().getAttackingPlayer();
    }

    @Override
    public Player getDamagedPlayer() {
        return sessionStateAtomicReference.get().getDamagedPlayer();
    }

    @Override
    public Field getDamagedField() throws IllegalStateException {
        return sessionStateAtomicReference.get().getDamagedField();
    }

    @Override
    public void nextPhase() {
        SessionState sessionState = sessionStateAtomicReference.get();
        sessionState = sessionState.nextPhase();
        sessionStateAtomicReference.set(sessionState);
    }

    @Override
    public Field getField1() {
        return sessionStateAtomicReference.get().getField1();
    }

    @Override
    public Field getField2() {
        return sessionStateAtomicReference.get().getField2();
    }

    @Override
    public CellStatus makeMove(Cell cell) throws IllegalStateException {
        return sessionStateAtomicReference.get().makeMove(cell);
    }
}
