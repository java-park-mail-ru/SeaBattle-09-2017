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