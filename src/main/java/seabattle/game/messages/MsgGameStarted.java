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

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (object == null || getClass() != object.getClass()) {
            return false;
        }

        MsgGameStarted that = (MsgGameStarted) object;

        if (first != null) {
            return first.equals(that.first);
        } else {
            return that.first == null;
        }

    }

    @Override
    public int hashCode() {
        if (first != null) {
            return first.hashCode();
        } else {
            return 0;
        }
    }
}
