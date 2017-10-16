package seabattle.views;

import com.fasterxml.jackson.annotation.JsonFormat;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum ResponseView {
    ERROR_INFO(0, "You are not currently logged in!"),
    SUCCESS_LOGIN(1, "You are successfully logged in!"),
    ERROR_LOGIN(2, "Wrong data!"),
    SUCCESS_LOGOUT(3, "You successfully logged out!"),
    ERROR_USER_EXIST(4, "UserView already exists!"),
    SUCCESS_REGISTER(5, "You are now registered!"),
    ERROR_REGISTER(6, "Wrong data!"),
    SUCCESS_USER_UPDATE(7, "Data is successfully updated!"),
    ERROR_USER_UPDATE(8, "New data is corrupted!"),
    ERROR_ACCESS(9, "Access error");

    private final Integer status;
    private final String response;

    ResponseView(Integer status, String response) {
        this.status = status;
        this.response = response;
    }


    @SuppressWarnings("unused")
    public Integer getStatus() {
        return status;
    }

    @SuppressWarnings("unused")
    public String getResponse() {
        return response;
    }

}
