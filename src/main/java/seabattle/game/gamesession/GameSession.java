package seabattle.game.gamesession;

import seabattle.game.field.Cell;
import seabattle.game.field.CellStatus;
import seabattle.game.field.Field;
import seabattle.game.player.Player;

import javax.validation.constraints.NotNull;

public interface GameSession {

    @NotNull
    Long getSessionId();

    @NotNull
    Player getPlayer1();

    @NotNull
    Long getPlayer1Id();

    void setPlayer1(@NotNull Player player1) throws IllegalStateException;

    @NotNull
    Player getPlayer2();

    @NotNull
    Long getPlayer2Id();

    void setPlayer2(@NotNull Player player2) throws IllegalStateException;

    Boolean bothFieldsAccepted();

    Player getWinner() throws IllegalStateException;

    void setField1(@NotNull Field field) throws IllegalStateException;

    void setField2(@NotNull Field field) throws IllegalStateException;

    void setDamagedSide(@NotNull Player player) throws IllegalStateException;

    Player getAttackingPlayer();

    Player getDamagedPlayer();

    Field getDamagedField() throws IllegalStateException;

    GameSession nextPhase();

    Field getField1();

    Field getField2();

    CellStatus makeMove(Cell cell) throws IllegalStateException;
}
