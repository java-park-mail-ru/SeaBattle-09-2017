package seabattle.game.messages;

import seabattle.msgsystem.Message;

@SuppressWarnings("unused")
public class MsgEndGame extends Message {

    private Boolean won;
    private Integer score;

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public MsgEndGame(Boolean won, Integer score) {
        this.won = won;
        this.score = score;
    }

    public Boolean getWon() {
        return won;
    }

    public void setWon(Boolean won) {
        this.won = won;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (object == null || getClass() != object.getClass()) {
            return false;
        }

        MsgEndGame that = (MsgEndGame) object;


        if (won != null) {
            if (!won.equals(that.won)) {
                return false;
            }
        } else {
            if (that.won != null) {
                return false;
            }
        }
        if (score != null) {
            return score.equals(that.score);
        } else {
            return that.score == null;
        }


    }

    @Override
    public int hashCode() {
        int result = 0;
        final int sizeInt = 31;
        if (won != null) {
            result = won.hashCode();
        }
        if (score != null) {
            result = sizeInt * result + score.hashCode();
        } else {
            result = sizeInt * result;
        }

        return result;
    }
}
