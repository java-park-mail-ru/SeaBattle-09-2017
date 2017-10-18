package seabattle.controllers;

import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.jdbc.JdbcTestUtils;
import seabattle.views.UserView;


import static org.junit.Assert.*;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SqlGroup({
        @Sql("/db/sheme_test_db.sql"),
        @Sql("/db/test_user.sql"),
})
@SpringBootTest(webEnvironment = RANDOM_PORT)
@RunWith(SpringRunner.class)
public class ControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private JdbcTemplate template;

    @After
    public void tearDown() {
        JdbcTestUtils.dropTables(template, "users");
    }

    @SuppressWarnings("all")
    @Test
    public void successfulRegister(){
        final UserView newUser = new UserView("Bob@mail.ru", "Bob", "02103452", 0);
        final HttpEntity<UserView> httpEntity = new HttpEntity<>(newUser);
        final ResponseEntity<UserView> registerResponce =  restTemplate.exchange("/api/users/",
                HttpMethod.POST, httpEntity, UserView.class);
        assertEquals(newUser.getEmail(), registerResponce.getBody().getEmail());
        assertEquals(newUser.getLogin(), registerResponce.getBody().getLogin());
        assertEquals(newUser.getScore(), registerResponce.getBody().getScore());
    }

    @Test
    public void userExistRegister(){
        final UserView newUser = new UserView("Odin@mail.ru", "Odin", "02103452", 0);
        final HttpEntity httpEntity = new HttpEntity<>(newUser);
        final ResponseEntity registerResponce =  restTemplate.exchange("/api/users/",
                HttpMethod.POST, httpEntity, String.class);
        assertEquals("{\"status\":4,\"response\":\"User already exists!\"}", registerResponce.getBody());
    }

    @Test
    public void wrongEmailRegister(){
        final UserView newUser = new UserView("Odinz", "Odin", "02103452", 0);
        final HttpEntity httpEntity = new HttpEntity<>(newUser);
        final ResponseEntity registerResponce =  restTemplate.exchange("/api/users/",
                HttpMethod.POST, httpEntity, String.class);
        assertEquals(HttpStatus.BAD_REQUEST, registerResponce.getStatusCode());
    }

}