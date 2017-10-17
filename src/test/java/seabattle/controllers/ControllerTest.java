package seabattle.controllers;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import seabattle.views.UserView;


import static org.junit.Assert.*;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SpringBootTest(webEnvironment = RANDOM_PORT)
@RunWith(SpringRunner.class)
public class ControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;


    @SuppressWarnings("all")
    @Test
    public void successfulRegister(){
        UserView newUser = new UserView("Bob@mail.ru", "Bob", "02103452", 0);
        HttpEntity<UserView> httpEntity = new HttpEntity(newUser);
        ResponseEntity registerResponce =  restTemplate.exchange("/api/users/",
                HttpMethod.POST, httpEntity, UserView.class);
        newUser.setPassword(null);
        assertEquals(newUser, registerResponce.getBody());
    }

    @SuppressWarnings("all")
    @Test
    public void userExistRegister(){
        UserView newUser = new UserView("Odin@mail.ru", "Odin", "02103452", 0);
        HttpEntity httpEntity = new HttpEntity(newUser);
        ResponseEntity registerResponce =  restTemplate.exchange("/api/users/",
                HttpMethod.POST, httpEntity, String.class);
        assertEquals("{\"status\":4,\"response\":\"User already exists!\"}", registerResponce.getBody());
    }

    @SuppressWarnings("all")
    @Test
    public void wrongEmailRegister(){
        UserView newUser = new UserView("Odinz", "Odin", "02103452", 0);
        HttpEntity httpEntity = new HttpEntity(newUser);
        ResponseEntity registerResponce =  restTemplate.exchange("/api/users/",
                HttpMethod.POST, httpEntity, String.class);
        assertEquals(HttpStatus.BAD_REQUEST, registerResponce.getStatusCode());
    }

}