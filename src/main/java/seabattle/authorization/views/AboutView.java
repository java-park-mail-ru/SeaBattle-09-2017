package seabattle.authorization.views;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotNull;

public class AboutView {
    @NotNull
    private String text;

    public AboutView(@JsonProperty("text") String text) {
        this.text = text;
    }

    @NotNull
    public String getAbout() {
        return text;
    }

    public void setAbout(String about) {
        this.text = about;
    }
}
