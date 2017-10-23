package seabattle.jdbcdao;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import seabattle.dao.UserService;
import seabattle.views.UserView;


import java.util.List;

import static org.junit.Assert.*;

@Transactional
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@RunWith(SpringRunner.class)
public class UserServiceTest {

    @Autowired
    private UserService userService;


    private UserView testUser;

    @Before
    public void setUp(){
        testUser = new UserView("yaho@bb.com", "yaho", "qwerty", 1);
    }


    @Test(expected = DuplicateKeyException.class)
    public void addUser() throws Exception {
        userService.addUser(testUser);

        final UserView newUser = new UserView("yaho@bb.com", "bob", "12345", 0);
        userService.addUser(newUser);

    }

    @Test
    public void getByLogin(){
        final UserView returnUser = userService.getByLoginOrEmail("yaho");
        assertNotNull(returnUser);
        assertEquals(returnUser.getEmail(), testUser.getEmail());
        assertEquals(returnUser.getLogin(), testUser.getLogin());
        assertEquals(returnUser.getPassword(), testUser.getPassword());
        assertEquals(returnUser.getScore(), testUser.getScore());

    }

    @Test
    public void getByEmail(){
        final UserView returnUser = userService.getByLoginOrEmail("yaho@bb.com");
        assertNotNull(returnUser);
        assertEquals(returnUser.getEmail(), testUser.getEmail());
        assertEquals(returnUser.getLogin(), testUser.getLogin());
        assertEquals(returnUser.getPassword(), testUser.getPassword());
        assertEquals(returnUser.getScore(), testUser.getScore());

    }

    @Test(expected = EmptyResultDataAccessException.class)
    public void getByNotExistUser(){
        userService.getByLoginOrEmail("bob");
        userService.getByLoginOrEmail("bob@gmail.com");
    }

    @Test
    public void changeUser(){
        final UserView changeUser = new UserView("adaw@bb.com", "Bred", "qwerty123", 2);
        final UserView returnUser  = userService.changeUser(changeUser);
        assertNotNull(returnUser);
        assertEquals(returnUser.getEmail(), changeUser.getEmail());
        assertEquals(returnUser.getLogin(), changeUser.getLogin());
        assertEquals(returnUser.getPassword(), changeUser.getPassword());
        assertEquals(returnUser.getScore(), changeUser.getScore());
    }


    @Test
    public void changeNotExistUser(){
        assertEquals(userService.changeUser(new UserView("bobi@bb.com", "bobi", "qwerty", 2)), null);
    }


    @Test
    public void getLeaderboard(){
        List<UserView> returnedUserList = userService.getLeaderboard();
        int i = returnedUserList.size();
        for (UserView userView: returnedUserList) {
            assertSame(userView.getScore(),--i);
        }
    }
}