package seabattle.game;


import javax.validation.constraints.NotNull;


public interface GameMechanics {
    void addUser(@NotNull Long userId);

    void deleteUser(@NotNull Long userId);

    Boolean isUserAdded (@NotNull Long userId);

}
