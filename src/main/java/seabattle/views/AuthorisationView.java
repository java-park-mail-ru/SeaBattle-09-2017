package seabattle.views;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotNull;

public final class AuthorisationView {

    @NotNull
    private String loginEmail;

    @NotNull
    private String password;

    private AuthorisationView(@JsonProperty("loginEmail") String loginEmail, @JsonProperty("password")
            String password) {
        this.loginEmail = loginEmail;
        this.password = password;
    }

    @NotNull
    public String getLoginEmail() {
        return loginEmail;
    }

    @NotNull
    public String getPassword() {
        return password;
    }

}
