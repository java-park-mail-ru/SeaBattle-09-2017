package seabattle.msgsystem;

import seabattle.msgsystem.Message;
import seabattle.msgsystem.MessageHandler;

import javax.validation.constraints.NotNull;

public interface MessageHandlersContainer {
    void handle(@NotNull Message message, @NotNull Long id);

    <T extends Message> void registerHandler(@NotNull Class<T> clazz, MessageHandler<T> handler);
}
