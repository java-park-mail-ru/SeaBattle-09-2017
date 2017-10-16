package seabattle.dao;

import seabattle.views.UserView;

public interface UserService {

    void addUser(UserView user);

    UserView getByLoginOrEmail(String loginOrEmail);
    
    UserView changeUser(UserView user);
}