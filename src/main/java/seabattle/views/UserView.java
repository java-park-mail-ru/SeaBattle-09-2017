package seabattle.views;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.validator.constraints.Email;
import org.springframework.lang.Nullable;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;

@SuppressWarnings("unused")
@Validated
public final class UserView {

    @NotNull
    @Email
    private String email;

    @NotNull
    private String login;

    @NotNull
    private String password;

    @Nullable
    private Integer score;

    UserView(@JsonProperty("email") String email, @JsonProperty("login") String login,
             @JsonProperty("password") String password, @JsonProperty("score") Integer score) {
        this.email = email;
        this.login = login;
        this.password = password;
        this.score = score;
    }

    @NotNull
    public String getEmail() {
        return email;
    }

    @NotNull
    public  String getLogin() {
        return login;
    }

    @NotNull
    public String getPassword() {
        return password;
    }

    @Nullable
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