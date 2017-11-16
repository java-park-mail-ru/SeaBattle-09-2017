package seabattle.game.messages;

import seabattle.msgsystem.Message;

public class MsgEndGame extends Message {
    private String resultGame;
    private Integer score;

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
