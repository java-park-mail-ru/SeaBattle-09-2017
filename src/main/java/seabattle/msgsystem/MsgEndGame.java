package seabattle.msgsystem;

public class MsgEndGame extends Message {
    String resultGame;
    Integer score;

    public MsgEndGame(String resultGame, Integer score) {
        this.resultGame = resultGame;
        this.score = score;
    }

    public String getResultGame() {
        return resultGame;
    }

    public void setResultGame(String resultGame) {
        this.resultGame = resultGame;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }
}
