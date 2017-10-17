package seabattle.jdbcdao;

import org.junit.Before;
import org.junit.Test;
import seabattle.dao.UserService;
import seabattle.views.UserView;

import static org.junit.Assert.*;

public class UserServiceTest {

    private UserService userService;
    private UserView testUser;

    @Before
    public void setUp() throws Exception {
        userService = new UserServiceImpl();
        testUser = new UserView("yaho@bb.com", "yaho", "qwerty");
        userService.addUser(testUser);
    }

    @Test
    public void addUser() throws Exception {
        try {
            userService.addUser(testUser);
        } catch (IllegalArgumentException ex)
        {
            assertEquals(ex.getMessage(),"Login is already taken!");
        }

    }

    @Test
    public void getByLogin() throws Exception {
        UserView returnUser = userService.getByLogin("yaho");
        assertNotNull(returnUser);
        assertEquals(returnUser, testUser);

        returnUser = userService.getByLogin("bob");
        assertNull(returnUser);
    }

    @Test
    public void getByEmail() throws Exception {
        UserView returnUser = userService.getByEmail("yaho@bb.com");
        assertNotNull(returnUser);
        assertEquals(returnUser, testUser);

        returnUser = userService.getByEmail("bob@gmail.com");
        assertNull(returnUser);
    }

    @Test
    public void changeUser() throws Exception {
        UserView changeUser = new UserView("bob@bb.com", "bob", "qwerty");
        UserView returnUser  = userService.changeUser(changeUser);
        assertNull(returnUser);

        changeUser = new UserView("adaw@bb.com", "yaho", "qwerty");
        returnUser  = userService.changeUser(changeUser);
        assertNotNull(returnUser);
        assertEquals(returnUser, changeUser);

        changeUser = new UserView("yaho@bb.com", "yaho", "12345");
        returnUser  = userService.changeUser(changeUser);
        assertNotNull(returnUser);
        assertEquals(returnUser, changeUser);

        changeUser = new UserView("yaho@bb.com", "yaho", "12345");
        returnUser  = userService.changeUser(changeUser);
        assertNotNull(returnUser);
        assertEquals(returnUser, changeUser);

        changeUser = new UserView("adaw@bb.com", "yaho", "12345");
        returnUser  = userService.changeUser(changeUser);
        assertNotNull(returnUser);
        assertEquals(returnUser, changeUser);
    }

}