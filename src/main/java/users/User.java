package users;

import com.fasterxml.jackson.annotation.JsonProperty;
import javax.validation.constraints.NotNull;

@SuppressWarnings("unused")
public final class User {

    @NotNull
    private String email;

    @NotNull
    private String login;

    @NotNull
    private String password;

    User(@JsonProperty("email") String email, @JsonProperty("login") String login,
         @JsonProperty("password") String password) {
        this.email = email;
        this.login = login;
        this.password = password;
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

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}