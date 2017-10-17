package seabattle.views;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.validator.constraints.Email;
import org.springframework.validation.annotation.Validated;

@SuppressWarnings("unused")
@Validated
public final class UserView {

    @Email
    private String email;

    private String login;

    private String password;

    private Integer score;

    public UserView(@JsonProperty("email") String email, @JsonProperty("login") String login,
             @JsonProperty("password") String password, @JsonProperty("score") Integer score) {
        this.email = email;
        this.login = login;
        this.password = password;
        this.score = score;
    }

    @Override
    public String toString() {
        return "login = " + login + " email = " + email + " password = " + password + " score = " + score;
    }

    public String getEmail() {
        return email;
    }

    public  String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }

    public Integer getScore() {
        return score;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setScore(Integer score) {
        this.score = score;
    }
}