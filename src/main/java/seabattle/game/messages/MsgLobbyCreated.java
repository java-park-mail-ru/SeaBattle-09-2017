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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MsgLobbyCreated that = (MsgLobbyCreated) o;

        return usernameEnemy != null ? usernameEnemy.equals(that.usernameEnemy) : that.usernameEnemy == null;
    }

    @Override
    public int hashCode() {
        return usernameEnemy != null ? usernameEnemy.hashCode() : 0;
    }
}
