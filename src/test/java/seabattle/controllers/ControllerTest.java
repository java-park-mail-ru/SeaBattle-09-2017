package seabattle.controllers;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import seabattle.views.UserView;
import org.springframework.jdbc.core.JdbcTemplate;


import static org.junit.Assert.*;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SpringBootTest(webEnvironment = RANDOM_PORT)
@RunWith(SpringRunner.class)
public class ControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private JdbcTemplate template;

    @Before
    public void setUp() throws Exception {
        String sqlquery = "CREATE TABLE IF NOT EXISTS users\n" +
                "(\n" +
                "  email text NOT NULL,\n" +
                "  login text NOT NULL,\n" +
                "  password text NOT NULL,\n" +
                "  score integer NOT NULL DEFAULT 0,\n" +
                "  CONSTRAINT unique_login PRIMARY KEY (login),\n" +
                "  CONSTRAINT unique_email UNIQUE (email)\n" +
                ");\n";

        template.execute(sqlquery);


    }

    @Test
    public void successful_register() throws Exception {
        UserView newUser = new UserView("Odin@mail.ru", "Odin", "02103452");
        HttpEntity httpEntity = new HttpEntity(newUser);
        ResponseEntity registerResponce =  restTemplate.exchange("/api/users/",
                HttpMethod.POST, httpEntity, String.class);
        assertEquals("{\"status\":5,\"response\":\"You are now registered!\"}", registerResponce.getBody());
    }

    @Test
    public void user_exist_register() throws Exception{
        UserView newUser = new UserView("Odin@mail.ru", "Odin", "02103452");
        HttpEntity httpEntity = new HttpEntity(newUser);
        ResponseEntity registerResponce =  restTemplate.exchange("/api/users/",
                HttpMethod.POST, httpEntity, String.class);
        assertEquals("{\"status\":4,\"response\":\"UserView already exists!\"}", registerResponce.getBody());
    }


    @Test
    public void wrong_user_register() throws Exception{
        UserView newUser = new UserView("Odinz", "Odin", "02103452");
        HttpEntity httpEntity = new HttpEntity(newUser);
        ResponseEntity registerResponce =  restTemplate.exchange("/api/users/",
                HttpMethod.POST, httpEntity, String.class);
        assertEquals("{\"status\":6,\"response\":\"Wrong data!\"}", registerResponce.getBody());
    }


}