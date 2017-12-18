package seabattle.game.messages;

import seabattle.msgsystem.Message;

public class MsgJoinGame extends Message {
    private Boolean playWithBot;

    public MsgJoinGame(Boolean playWithBot) {
        this.playWithBot = playWithBot;
    }

    public Boolean getPlayWithBot() {
        return playWithBot;
    }

    public void setPlayWithBot(Boolean playWithBot) {
        this.playWithBot = playWithBot;
    }
}
