package seabattle.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.validation.annotation.Validated;
import seabattle.dao.UserService;
import seabattle.jdbcdao.JdbcUserService;
import seabattle.views.AuthorisationView;
import seabattle.views.UserView;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import seabattle.views.ResponseView;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

@RestController
//@CrossOrigin (origins = "https://sea-battle-front.herokuapp.com")
@RequestMapping(path = "/api")
@Validated
public class Controller {

//    private UserService dbUsers = new JdbcUserService(new JdbcTemplate());

    @Autowired
    private JdbcUserService dbUsers;

    private static final String CURRENT_USER_KEY = "currentUser";

    @RequestMapping(method = RequestMethod.GET, path = "info")
    public ResponseEntity info(HttpSession httpSession) {
        final String currentUser = (String) httpSession.getAttribute(CURRENT_USER_KEY);
        if (currentUser == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseView.ERROR_INFO);
        }
        return ResponseEntity.status(HttpStatus.OK).body("Current user login: " + currentUser);
    }

    @RequestMapping(method = RequestMethod.POST, path = "login")
    public ResponseEntity login(@Valid @RequestBody AuthorisationView loggingData, HttpSession httpSession) {
        UserView currentUser;
        try {
             currentUser = dbUsers.getByLoginOrEmail(loggingData.getLoginEmail());
        } catch (DataAccessException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseView.ERROR_LOGIN);
        }

        if (currentUser.getPassword().equals(loggingData.getPassword())) {
            httpSession.setAttribute(CURRENT_USER_KEY, currentUser.getLogin());
            return ResponseEntity.status(HttpStatus.OK).body(ResponseView.SUCCESS_LOGIN);
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseView.ERROR_LOGIN);
    }

    @RequestMapping(method = RequestMethod.GET, path = "logout")
    public ResponseEntity logout(HttpSession httpSession) {
        httpSession.setAttribute(CURRENT_USER_KEY, null);
        return ResponseEntity.status(HttpStatus.OK).body(ResponseView.SUCCESS_LOGOUT);
    }

    @RequestMapping(method = RequestMethod.POST, path = "users")
    public ResponseEntity register(@Valid @RequestBody UserView registerData) {
        try {
            dbUsers.addUser(registerData);
        } catch (DuplicateKeyException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseView.ERROR_REGISTER);
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(ResponseView.SUCCESS_REGISTER);
    }

    @RequestMapping(method = RequestMethod.PATCH, path = "users/{changedUser}")
    public ResponseEntity change(@Valid @RequestBody UserView newData, @PathVariable(value = "changedUser") String changedUser,
                                 HttpSession httpSession) {
        if (httpSession.getAttribute(CURRENT_USER_KEY).equals(changedUser)) {
            if (dbUsers.changeUser(newData) != null) {
                return ResponseEntity.status(HttpStatus.OK).body(ResponseView.SUCCESS_USER_UPDATE);
            }
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseView.ERROR_USER_UPDATE);
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ResponseView.ERROR_ACCESS);
    }

}
