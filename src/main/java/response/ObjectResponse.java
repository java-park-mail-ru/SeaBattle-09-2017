package response;

public class ObjectResponse {
    private Integer status;
    private String response;

    public ObjectResponse(Integer status, String response) {
        this.response = response;
        this.status = status;
    }

    public static final ObjectResponse ERROR_INFO = new ObjectResponse(0, "You are not currently logged in!");
    public static final ObjectResponse SUCCESS_LOGIN = new ObjectResponse(1, "You are successfully logged in!");
    public static final ObjectResponse ERROR_LOGIN = new ObjectResponse(2, "Wrong data!");
    public static final ObjectResponse SUCCESS_LOGOUT = new ObjectResponse(3, "You successfully logged out!");
    public static final ObjectResponse ERROR_USER_EXIST = new ObjectResponse(4, "User already exists!");
    public static final ObjectResponse SUCCESS_REGISTER = new ObjectResponse(5, "You are now registered!");
    public static final ObjectResponse ERROR_REGISTER = new ObjectResponse(6, "Wrong data!");
    public static final ObjectResponse SUCCESS_USER_UPDATE = new ObjectResponse(7, "Data is successfully updated!");
    public static final ObjectResponse ERROR_USER_UPDATE = new ObjectResponse(8, "New data is corrupted!");
    public static final ObjectResponse ERROR_ACCESS = new ObjectResponse(9, "Access error");

    @SuppressWarnings("unused")
    public Integer getStatus() {
        return status;
    }

    @SuppressWarnings("unused")
    public String getResponse() {
        return response;
    }

}
