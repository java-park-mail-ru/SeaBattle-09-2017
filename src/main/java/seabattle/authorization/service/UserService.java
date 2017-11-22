package seabattle.authorization.service;

import seabattle.authorization.views.UserView;

import java.util.List;

public interface UserService {

    void addUser(UserView user);

    UserView getByLoginOrEmail(String loginOrEmail);
    
    UserView changeUser(UserView user);

    List<UserView> getLeaderboard(Integer limit);

    UserView setScore(UserView user);
}