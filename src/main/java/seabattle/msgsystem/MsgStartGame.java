package seabattle.msgsystem;

public class MsgStartGame extends Message {
    private String usernameEnemy;

    public MsgStartGame(String usernameEnemy) {
        this.usernameEnemy = usernameEnemy;
    }

    public String getUsernameEnemy() {
        return usernameEnemy;
    }

    public void setUsernameEnemy(String usernameEnemy) {
        this.usernameEnemy = usernameEnemy;
    }
}
