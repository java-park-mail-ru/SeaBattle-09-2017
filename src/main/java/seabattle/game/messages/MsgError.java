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

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (object == null || getClass() != object.getClass()) {
            return false;
        }

        MsgError msgError = (MsgError) object;

        if (error != null) {
            return (error.equals(msgError.error));
        } else {
            return msgError.error == null;
        }


    }

    @Override
    public int hashCode() {
        if (error != null) {
            return error.hashCode();
        } else {
            return 0;
        }
    }
}
