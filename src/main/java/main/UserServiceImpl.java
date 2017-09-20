package main;

import org.springframework.stereotype.Service;
import users.User;

import java.util.LinkedList;

@Service
public class UserServiceImpl implements UserService {

    private LinkedList<User> users;

    UserServiceImpl() {
        users = new LinkedList<>();
    }

    @Override
    public User addUser(User user) {

        if (user.getLogin() == null || user.getPassword() == null || user.getEmail() == null) {
            return null;
        }

        for (User dbUser : users) {
            if (dbUser.getLogin().equals(user.getLogin()) || dbUser.getEmail().equals(user.getEmail())) {
                return null;
            }
        }
        users.add(user);

        return user;
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