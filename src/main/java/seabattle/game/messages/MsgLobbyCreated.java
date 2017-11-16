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

}
