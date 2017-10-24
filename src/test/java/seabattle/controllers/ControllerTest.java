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

@SuppressWarnings("ConstantConditions")
@Transactional
@SpringBootTest(webEnvironment = RANDOM_PORT)
@RunWith(SpringRunner.class)
public class ControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void successfulRegister() {
        final UserView newUser = new UserView("Bob@mail.ru", "Bob", "02103452", 0);
        final HttpEntity<UserView> httpEntity = new HttpEntity<>(newUser);
        final ResponseEntity<UserView> registerResponse = restTemplate.exchange("/api/users/",
                HttpMethod.POST, httpEntity, UserView.class);
        assertEquals(newUser.getEmail(), registerResponse.getBody().getEmail());
        assertEquals(newUser.getLogin(), registerResponse.getBody().getLogin());
        assertEquals(newUser.getScore(), registerResponse.getBody().getScore());
    }

    @Test
    public void userExistRegister() {
        final UserView newUser = new UserView("Odin@mail.ru", "Odin", "02103452", 0);
        final HttpEntity<UserView> httpEntity = new HttpEntity<>(newUser);
        final ResponseEntity<String> registerResponse = restTemplate.exchange("/api/users/",
                HttpMethod.POST, httpEntity, String.class);
        assertEquals("{\"status\":4,\"response\":\"User already exists!\"}", registerResponse.getBody());
    }

    @Test
    public void wrongEmailRegister() {
        final UserView newUser = new UserView("Odinz", "Odin", "02103452", 0);
        final HttpEntity<UserView> httpEntity = new HttpEntity<>(newUser);
        final ResponseEntity<String> registerResponse = restTemplate.exchange("/api/users/",
                HttpMethod.POST, httpEntity, String.class);
        assertEquals(HttpStatus.BAD_REQUEST, registerResponse.getStatusCode());
    }

    @Test
    public void successfulLogin() {
            login();
    }

    private List<String> login() {
        final AuthorisationView user = new AuthorisationView("Alik", "13213");
        final HttpEntity<AuthorisationView> httpEntity = new HttpEntity<>(user);
        final ResponseEntity<UserView> responseEntity = restTemplate.exchange("/api/login/",
                HttpMethod.POST, httpEntity, UserView.class);
        assertEquals(user.getLoginEmail(), responseEntity.getBody().getLogin());

        final List<String> cookies = responseEntity.getHeaders().get("Set-Cookie");
        assertNotNull(cookies);
        assertFalse(cookies.isEmpty());

        return cookies;
    }


    @Test
    public void info() {
        final List<String> cookies = login();
        final HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.put(HttpHeaders.COOKIE, cookies);
        final HttpEntity<Void> requestEntity = new HttpEntity<>(requestHeaders);

        final ResponseEntity<UserView> responseEntity = restTemplate.exchange("/api/info/",
                HttpMethod.GET, requestEntity, UserView.class);
        assertNotNull(responseEntity.getBody());
        assertEquals("Alik", responseEntity.getBody().getLogin());
    }



    @Test
    public void wrongLoginOrEmailLogin() {
        final AuthorisationView user = new AuthorisationView("bobi", "qwerty");
        final HttpEntity<AuthorisationView> httpEntity = new HttpEntity<>(user);
        final ResponseEntity<String> responseEntity = restTemplate.exchange("/api/login/",
                HttpMethod.POST, httpEntity, String.class);
        assertEquals("{\"status\":1,\"response\":\"Wrong login/email or password!\"}", responseEntity.getBody());
    }

    @Test
    public void successfulChangeUser() {
        final List<String> cookies = login();
        final HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.put(HttpHeaders.COOKIE, cookies);
        final UserView changeUser = new UserView("adaw@bb.com", "Alik", "qwerty", 6);
        final HttpEntity<UserView> httpEntity = new HttpEntity<>(changeUser, requestHeaders);
        final ResponseEntity<UserView> responseEntity = restTemplate.exchange("/api/users/Alik/",
                HttpMethod.POST, httpEntity, UserView.class);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(changeUser.getEmail(), responseEntity.getBody().getEmail());
        assertEquals(changeUser.getLogin(), responseEntity.getBody().getLogin());
    }

    @Test
    public void unsuccessfulChangeUser(){
        final List<String> cookies = login();
        final HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.put(HttpHeaders.COOKIE, cookies);
        final UserView changeUser = new UserView("bobi@bb.com", "bobi", "qwerty", 2);
        final HttpEntity<UserView> httpEntity = new HttpEntity<>(changeUser, requestHeaders);
        final ResponseEntity<String> responseEntity = restTemplate.exchange("/api/users/bobi/",
                HttpMethod.POST, httpEntity, String.class);
        assertEquals(HttpStatus.FORBIDDEN, responseEntity.getStatusCode());
    }

    @Test
    public void leaderboard(){
        final ResponseEntity<List<UserView>> responseEntity = restTemplate.exchange("/api/leaderboard/",
                HttpMethod.GET, null, new ParameterizedTypeReference<List<UserView>>() {});
        assertNotNull(responseEntity.getBody());
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }


}