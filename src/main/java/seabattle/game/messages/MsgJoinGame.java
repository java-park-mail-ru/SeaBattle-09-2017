package seabattle.game.messages;

import com.fasterxml.jackson.annotation.JsonProperty;
import seabattle.msgsystem.Message;

import javax.validation.constraints.NotNull;

public class MsgJoinGame extends Message {

    @NotNull
    private Boolean playWithBot;

    public MsgJoinGame(@NotNull @JsonProperty("playWithBot") Boolean playWithBot) {
        this.playWithBot = playWithBot;
    }

    @NotNull
    public Boolean getPlayWithBot() {
        return playWithBot;
    }

    public void setPlayWithBot(@NotNull Boolean playWithBot) {
        this.playWithBot = playWithBot;
    }
}
