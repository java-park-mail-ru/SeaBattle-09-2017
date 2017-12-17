package seabattle.game.gamesession;

import seabattle.game.field.Cell;
import seabattle.game.field.CellStatus;
import seabattle.game.field.Field;
import seabattle.game.player.Player;
import seabattle.game.ship.Ship;

import javax.validation.constraints.NotNull;

public class GameSessionActive implements GameSession {

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

    GameSessionActive(@NotNull GameSessionSetup sessionSetup) {
        this.sessionId = sessionSetup.getSessionId();
        this.player1 = sessionSetup.getPlayer1();
        this.player2 = sessionSetup.getPlayer2();
        this.damagedPlayer = sessionSetup.getDamagedPlayer();
        this.damagedField = sessionSetup.getDamagedField();
        this.field1 = sessionSetup.getField1();
        this.field2 = sessionSetup.getField2();
    }

    @Override
    @NotNull
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

    @Override
    public Player getWinner() throws IllegalStateException {
        throw new IllegalStateException("Game is in active phase!");
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
    public Player getDamagedPlayer() {
        return damagedPlayer;
    }

    @NotNull
    @Override
    public Field getDamagedField() {
        return damagedField;
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
    public GameSession nextPhase() {
        GameSessionEndgame sessionEndgame = new GameSessionEndgame(this);
        return sessionEndgame;
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
        switch (damagedField.fire(cell)) {
            case BLOCKED:
                switchDamagedPlayer();
                return CellStatus.BLOCKED;
            case ON_FIRE:
                for (Ship ship : damagedPlayer.getAliveShips()) {
                    if (ship.inShip(cell) && damagedField.shipKilled(ship)) {
                        damagedField.killShip(ship);
                        damagedPlayer.getAliveShips().remove(ship);
                        damagedPlayer.getDeadShips().add(ship);
                        return CellStatus.DESTRUCTED;
                    }
                }
                return CellStatus.ON_FIRE;
            default:
                throw new IllegalStateException("Error occured on cell status calculation");
        }
    }

    private void switchDamagedPlayer() {
        if (damagedPlayer.equals(player1)) {
            damagedPlayer = player2;
            damagedField = field2;
        } else {
            damagedPlayer = player1;
            damagedField = field1;
        }
    }
}
