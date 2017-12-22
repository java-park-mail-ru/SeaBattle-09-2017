package seabattle.game.messages;

import seabattle.msgsystem.Message;

@SuppressWarnings("unused")
public class MsgYouInQueue extends Message {
    private String nickname;

    public MsgYouInQueue(String nickname) {
        this.nickname = nickname;
    }


    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (object == null || getClass() != object.getClass()) {
            return false;
        }

        MsgYouInQueue that = (MsgYouInQueue) object;

        if (nickname != null) {
            return nickname.equals(that.nickname);
        } else {
            return that.nickname == null;
        }
    }

    @Override
    public int hashCode() {
        if (nickname != null) {
            return nickname.hashCode();
        } else {
            return 0;
        }
    }
}
