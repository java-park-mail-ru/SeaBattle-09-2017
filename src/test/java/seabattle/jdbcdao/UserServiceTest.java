package seabattle.jdbcdao;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.test.context.junit4.SpringRunner;
import seabattle.dao.UserService;
import seabattle.views.UserView;


import static org.junit.Assert.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@RunWith(SpringRunner.class)
public class UserServiceTest {

    @Autowired
    private UserService userService;


    private UserView testUser;

    @Before
    public void setUp(){
        testUser = new UserView("yaho@bb.com", "yaho", "qwerty", 0);
    }


    @SuppressWarnings("all")
    @Test(expected = DuplicateKeyException.class)
    public void addUser() throws Exception {
        userService.addUser(testUser);

        UserView newUser = new UserView("yaho@bb.com", "bob", "12345", 0);
        userService.addUser(newUser);

    }

    @Test
    public void getByLogin(){
        UserView returnUser = userService.getByLoginOrEmail("yaho");
        assertNotNull(returnUser);
        assertEquals(returnUser, testUser);

        returnUser = userService.getByLoginOrEmail("bob");
        assertNull(returnUser);
    }

    @Test
    public void getByEmail(){
        UserView returnUser = userService.getByLoginOrEmail("yaho@bb.com");
        assertNotNull(returnUser);
        assertEquals(returnUser, testUser);

        returnUser = userService.getByLoginOrEmail("bob@gmail.com");
        assertNull(returnUser);
    }

    @Test
    public void changeUser(){
        UserView changeUser = new UserView("bobi@bb.com", "bobi", "qwerty", 2);
        UserView returnUser  = userService.changeUser(changeUser);
        assertNull(returnUser);

        changeUser = new UserView("adaw@bb.com", "Bred", "qwerty123", 2);
        returnUser  = userService.changeUser(changeUser);
        assertNotNull(returnUser);
        assertEquals(changeUser, returnUser);

    }



}