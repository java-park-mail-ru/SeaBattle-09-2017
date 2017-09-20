package main;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotNull;

public final class AuthorisationModel {

    @NotNull
    private String loginEmail;

    @NotNull
    private String password;

    private AuthorisationModel(@JsonProperty("loginEmail") String loginEmail, @JsonProperty("password")
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
