package seabattle.JdbcDAO;

import org.springframework.stereotype.Service;
import seabattle.DAO.UserService;
import seabattle.views.UserView;

import java.util.LinkedList;

@Service
public class UserServiceImpl implements UserService {

    private LinkedList<UserView> users = new LinkedList<>();

    @Override
    public void addUser(UserView user) {

        if (user.getLogin() == null || user.getPassword() == null || user.getEmail() == null) {
            throw new IllegalArgumentException("New user has incompete data!");
        }

        for (UserView dbUser : users) {
            if (dbUser.getLogin().equals(user.getLogin())) {
                throw new IllegalArgumentException("Login is already taken!");
            }
            if (dbUser.getEmail().equals(user.getEmail())) {
                throw new IllegalArgumentException("Email is already taken!");
            }
        }
        users.add(user);
    }

    @Override
    public UserView getByLogin(String login) {

        for (UserView dbUser : users) {
            if (dbUser.getLogin().equals(login)) {
                return dbUser;
            }
        }
        return null;
    }

    @Override
    public UserView getByEmail(String email) {

        for (UserView dbUser : users) {
            if (dbUser.getEmail().equals(email)) {
                return dbUser;
            }
        }
        return null;
    }

    @Override
    public UserView changeUser(UserView user) {

        for (UserView dbUser : users) {
            if (dbUser.getLogin().equals(user.getLogin())) {
                if (user.getEmail() != null) {
                    dbUser.setEmail(user.getEmail());
                }
                if (user.getPassword() != null) {
                    dbUser.setPassword(user.getPassword());
                }
                return dbUser;
            }
        }

        return null;
    }

}