package seabattle.game;


import javax.validation.constraints.NotNull;


public interface GameMechanics {
    void addUser(@NotNull Long userId);

    void deleteUser(@NotNull Long userId);

    boolean isUserAdded (@NotNull Long userId);

}
