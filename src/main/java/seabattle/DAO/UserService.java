package seabattle.DAO;

import seabattle.views.UserView;

public interface UserService {


    void addUser(UserView user);

    UserView getByLogin(String login);

    UserView getByEmail(String email);
    
    UserView changeUser(UserView user);
}