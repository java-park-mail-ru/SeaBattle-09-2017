package main;

import com.fasterxml.jackson.annotation.JsonProperty;
import users.User;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import response.ObjectResponse;



import javax.servlet.http.HttpSession;

@RestController
@CrossOrigin (origins = "localhost")
@RequestMapping(path = "/api")
public class Controller {


    private UserServiceImpl dbUsers = new UserServiceImpl();

    // вывод информации о текущем пользователе
    @RequestMapping(method = RequestMethod.GET, path = "info")
    public ResponseEntity info(HttpSession httpSession) {
        // получаем текущего пользователя из сессии, возвращаем его или ошибку
        final String currentUser = (String) httpSession.getAttribute("currentUser");
        if (currentUser == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ObjectResponse.ERROR_INFO);
        }
        return ResponseEntity.status(HttpStatus.OK).body("Current user login: " + currentUser);
    }

    //    аутентификация
    @RequestMapping(method = RequestMethod.POST, path = "login")
    public ResponseEntity login(@RequestBody AuthorisationModel loggingData, HttpSession httpSession) {
        User currentUser = dbUsers.getByLogin(loggingData.getLoginEmail());
        if (currentUser == null) {
            currentUser = dbUsers.getByEmail(loggingData.getLoginEmail());
        }
        if (currentUser != null && currentUser.getPassword().equals(loggingData.getPassword())) {
            httpSession.setAttribute("currentUser", currentUser.getLogin());
            return ResponseEntity.status(HttpStatus.OK).body(ObjectResponse.SUCCESS_LOGIN);
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ObjectResponse.ERROR_LOGIN);
    }

    //    разлогин
    @RequestMapping(method = RequestMethod.GET, path = "logout")
    public ResponseEntity logout(HttpSession httpSession) {
        httpSession.setAttribute("currentUser", null);
        return ResponseEntity.status(HttpStatus.OK).body(ObjectResponse.SUCCESS_LOGOUT);
    }


    //    регистрация
    @RequestMapping(method = RequestMethod.POST, path = "users")
    public ResponseEntity register(@RequestBody User registerData) {

        if (dbUsers.getByEmail(registerData.getEmail()) != null || dbUsers.getByLogin(registerData.getLogin()) != null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ObjectResponse.ERROR_USER_EXIST);
        }
        if (dbUsers.addUser(registerData) != null) {
            return ResponseEntity.status(HttpStatus.CREATED).body(ObjectResponse.SUCCESS_REGISTER);
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ObjectResponse.ERROR_REGISTER);
    }

    //    изменение пользователя
    @RequestMapping(method = RequestMethod.PATCH, path = "users/{changedUser}")
    public ResponseEntity change(@RequestBody User newData, @PathVariable(value = "changedUser") String changedUser,
                                 HttpSession httpSession) {
        if (httpSession.getAttribute("currentUser").equals(changedUser)) {
            if (dbUsers.changeUser(newData) != null) {
                return ResponseEntity.status(HttpStatus.OK).body(ObjectResponse.SUCCESS_USER_UPDATE);
            }
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ObjectResponse.ERROR_USER_UPDATE);
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ObjectResponse.ERROR_ACCESS);
    }


    @SuppressWarnings("unused")
    private static final class AuthorisationModel {

        private String loginEmail;
        private String password;

        private AuthorisationModel(@JsonProperty("loginEmail") String loginEmail, @JsonProperty("password")
                String password) {
            this.loginEmail = loginEmail;
            this.password = password;
        }

        public String getLoginEmail() {
            return loginEmail;
        }

        public String getPassword() {
            return password;
        }
    }
}
