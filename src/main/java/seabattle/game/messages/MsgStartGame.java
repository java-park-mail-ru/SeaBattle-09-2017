package seabattle.game.messages;

import seabattle.msgsystem.Message;

public class MsgStartGame extends Message {

    private String usernameEnemy;
    private Boolean first;

    public MsgStartGame(String usernameEnemy, Boolean first) {
        this.usernameEnemy = usernameEnemy;
        this.first = first;
    }

    public String getUsernameEnemy() {
        return usernameEnemy;
    }

    public void setUsernameEnemy(String usernameEnemy) {
        this.usernameEnemy = usernameEnemy;
    }

    public Boolean isFirst() {
        return first;
    }

    public void setFirst(Boolean first) {
        this.first = first;
    }
}
