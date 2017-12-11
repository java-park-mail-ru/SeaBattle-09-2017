package seabattle.authorization.views;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotNull;

public class AboutView {
    @NotNull
    private String about;

    public AboutView(@JsonProperty("about") String about) {
        this.about = about;
    }

    @NotNull
    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }
}
