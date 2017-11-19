package seabattle.game.messages;

import seabattle.msgsystem.Message;

@SuppressWarnings("unused")
public class MsgLobbyCreated extends Message {

    private String usernameEnemy;

    public MsgLobbyCreated(String usernameEnemy) {
        this.usernameEnemy = usernameEnemy;
    }

    public String getUsernameEnemy() {
        return usernameEnemy;
    }

    public void setUsernameEnemy(String usernameEnemy) {
        this.usernameEnemy = usernameEnemy;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }

        MsgLobbyCreated that = (MsgLobbyCreated) obj;

        if (usernameEnemy == null) {
            return that.usernameEnemy == null;
        }
        return usernameEnemy.equals(that.usernameEnemy);
    }

    @Override
    public int hashCode() {
        if (usernameEnemy == null) {
            return 0;
        }
        return usernameEnemy.hashCode();
    }
}
