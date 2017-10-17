package seabattle.views;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.validator.constraints.Email;
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

    public UserView(@JsonProperty("email") String email, @JsonProperty("login") String login,
             @JsonProperty("password") String password) {
        this.email = email;
        this.login = login;
        this.password = password;
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

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}