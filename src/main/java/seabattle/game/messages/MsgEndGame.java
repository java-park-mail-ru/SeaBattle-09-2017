package seabattle.game.messages;

import seabattle.msgsystem.Message;

import javax.validation.constraints.NotNull;

public class MsgEndGame extends Message {

    private Boolean won;

    public MsgEndGame(Boolean won) {
        this.won = won;
    }

    public Boolean getWon() {
        return won;
    }

    public void setWon(Boolean won) {
        this.won = won;
    }
}
