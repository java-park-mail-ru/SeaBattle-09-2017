package main;

import org.springframework.stereotype.Service;
import users.User;

import java.util.LinkedList;

@Service
public class UserServiceImpl implements UserService {

    private LinkedList<User> users = new LinkedList<>();

    @Override
    public void addUser(User user) {

        if (user.getLogin() == null || user.getPassword() == null || user.getEmail() == null) {
            throw new IllegalArgumentException("New user has incompete data!");
        }

        for (User dbUser : users) {
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
    public User getByLogin(String login) {

        for (User dbUser : users) {
            if (dbUser.getLogin().equals(login)) {
                return dbUser;
            }
        }
        return null;
    }

    @Override
    public User getByEmail(String email) {

        for (User dbUser : users) {
            if (dbUser.getEmail().equals(email)) {
                return dbUser;
            }
        }
        return null;
    }

    @Override
    public User changeUser(User user) {

        for (User dbUser : users) {
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