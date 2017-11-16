package seabattle.msgsystem;

import javax.validation.constraints.NotNull;

@SuppressWarnings("WeakerAccess")
public abstract class MessageHandler<T extends Message>  {
    @NotNull
    private final Class<T> clazz;

    public MessageHandler(@NotNull Class<T> clazz) {
        this.clazz = clazz;
    }

    public void handleMessage(@NotNull Message message, Long id) {
        handle(clazz.cast(message), id);
    }

    public abstract void handle(T cast, Long id);
}
