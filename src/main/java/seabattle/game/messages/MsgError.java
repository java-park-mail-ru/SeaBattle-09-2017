package seabattle.game.messages;

import seabattle.msgsystem.Message;

public class MsgError extends Message {

    private String error;

    public MsgError(String error) {
        this.error = error;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}
