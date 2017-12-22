package seabattle.authorization.views;


public class LeaderboardView {
    private Integer position;

    private String login;

    private Integer score;

    public LeaderboardView(Integer position, String login, Integer score) {
        this.position = position;
        this.login = login;
        this.score = score;
    }

    public Integer getPosition() {
        return position;
    }

    public void setPosition(Integer position) {
        this.position = position;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }
}
