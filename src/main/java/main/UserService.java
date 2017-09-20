package main;

import users.User;

public interface UserService {


    void addUser(User user);

    User getByLogin(String login);

    User getByEmail(String email);
    
    User changeUser(User user);
}