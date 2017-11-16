package seabattle.game.messages;

import seabattle.msgsystem.Message;

import javax.validation.constraints.NotNull;

@SuppressWarnings("unused")
public class MsgGameStarted extends Message {

    @NotNull
    private Boolean first;

    public MsgGameStarted(@NotNull Boolean first) {
        this.first = first;
    }

    public Boolean getFirst() {
        return first;
    }

    public void setFirst(Boolean first) {
        this.first = first;
    }
}
