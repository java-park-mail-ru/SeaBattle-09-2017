package main;

import users.User;

public interface UserService {

    //    добавление пользователя. Если логин или почта уже заняты - возвращается null.
    User addUser(User user);
    //    достать пользователя по логину
    User getByLogin(String login);
    //    достать пользователя по емейлу
    User getByEmail(String email);
    //    изменить пользователя с логином из user.login. Другие не null поля подставляются взамен старых.
    User changeUser(User user);
}