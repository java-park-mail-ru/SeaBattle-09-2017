package seabattle.controllers;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import seabattle.views.AuthorisationView;
import seabattle.views.UserView;


import java.util.List;

import static org.junit.Assert.*;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@Transactional
@SpringBootTest(webEnvironment = RANDOM_PORT)
@RunWith(SpringRunner.class)
public class ControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;


    @SuppressWarnings("all")
    @Test
    public void successfulRegister() {
        final UserView newUser = new UserView("Bob@mail.ru", "Bob", "02103452", 0);
        final HttpEntity<UserView> httpEntity = new HttpEntity<>(newUser);
        final ResponseEntity<UserView> registerResponce = restTemplate.exchange("/api/users/",
                HttpMethod.POST, httpEntity, UserView.class);
        assertEquals(newUser.getEmail(), registerResponce.getBody().getEmail());
        assertEquals(newUser.getLogin(), registerResponce.getBody().getLogin());
        assertEquals(newUser.getScore(), registerResponce.getBody().getScore());
    }

    @Test
    public void userExistRegister() {
        final UserView newUser = new UserView("Odin@mail.ru", "Odin", "02103452", 0);
        final HttpEntity httpEntity = new HttpEntity<>(newUser);
        final ResponseEntity registerResponce = restTemplate.exchange("/api/users/",
                HttpMethod.POST, httpEntity, String.class);
        assertEquals("{\"status\":4,\"response\":\"User already exists!\"}", registerResponce.getBody());
    }

    @Test
    public void wrongEmailRegister() {
        final UserView newUser = new UserView("Odinz", "Odin", "02103452", 0);
        final HttpEntity httpEntity = new HttpEntity<>(newUser);
        final ResponseEntity registerResponce = restTemplate.exchange("/api/users/",
                HttpMethod.POST, httpEntity, String.class);
        assertEquals(HttpStatus.BAD_REQUEST, registerResponce.getStatusCode());
    }

    @Test
    public void successfulLogin() {
            login();
    }

    @SuppressWarnings("all")
    private List<String> login() {
        final AuthorisationView user = new AuthorisationView("yaho", "qwerty");
        final HttpEntity httpEntity = new HttpEntity<>(user);
        final ResponseEntity<UserView> responseEntity = restTemplate.exchange("/api/login/",
                HttpMethod.POST, httpEntity, UserView.class);
        assertEquals(user.getLoginEmail(), responseEntity.getBody().getLogin());

        final List<String> coockies = responseEntity.getHeaders().get("Set-Cookie");
        assertNotNull(coockies);
        assertFalse(coockies.isEmpty());

        return coockies;
    }


    @Test
    public void info() {
        final List<String> coockies = login();
        final HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.put(HttpHeaders.COOKIE, coockies);
        final HttpEntity<Void> requestEntity = new HttpEntity<>(requestHeaders);

        final ResponseEntity<UserView> responseEntity = restTemplate.exchange("/api/info/",
                HttpMethod.GET, requestEntity, UserView.class);
        assertNotNull(responseEntity.getBody());
        assertEquals("yaho", responseEntity.getBody().getLogin());
    }



    @Test
    public void wrongLoginOrEmailLogin() {
        final AuthorisationView user = new AuthorisationView("bobi", "qwerty");
        final HttpEntity httpEntity = new HttpEntity<>(user);
        final ResponseEntity responseEntity = restTemplate.exchange("/api/login/",
                HttpMethod.POST, httpEntity, String.class);
        assertEquals("{\"status\":1,\"response\":\"Wrong login/email or password!\"}", responseEntity.getBody());
    }

    @SuppressWarnings("all")
    @Test
    public void successfulChangeUser() {
        final List<String> coockies = login();
        final HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.put(HttpHeaders.COOKIE, coockies);
        final UserView changeUser = new UserView("adaw@bb.com", "yaho", "qwerty", 2);
        final HttpEntity httpEntity = new HttpEntity<>(changeUser, requestHeaders);
        final ResponseEntity<UserView> responseEntity = restTemplate.exchange("/api/users/yaho/",
                HttpMethod.POST, httpEntity, UserView.class);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(changeUser.getEmail(), responseEntity.getBody().getEmail());
        assertEquals(changeUser.getLogin(), responseEntity.getBody().getLogin());
    }

    @Test
    public void unsuccessfulChangeUser(){
        final List<String> coockies = login();
        final HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.put(HttpHeaders.COOKIE, coockies);
        final UserView changeUser = new UserView("bobi@bb.com", "bobi", "qwerty", 2);
        final HttpEntity httpEntity = new HttpEntity<>(changeUser, requestHeaders);
        final ResponseEntity responseEntity = restTemplate.exchange("/api/users/bobi/",
                HttpMethod.POST, httpEntity, String.class);
        assertEquals(HttpStatus.FORBIDDEN, responseEntity.getStatusCode());
    }

    @SuppressWarnings("all")
    @Test
    public void leaderboard(){
        final ResponseEntity<List<UserView>> responseEntity = restTemplate.exchange("/api/leaderboard/",
                HttpMethod.GET, null, new ParameterizedTypeReference<List<UserView>>() {});
        assertNotNull(responseEntity.getBody());
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }


}