package seabattle.websocket;

import org.springframework.stereotype.Service;
import seabattle.msgsystem.Message;
import seabattle.msgsystem.MessageHandler;
import seabattle.msgsystem.MessageHandlersContainer;

import javax.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.Map;

@Service
public class GameMassageHandlerContainer implements MessageHandlersContainer {
    final Map<Class<?>, MessageHandler<?>> handlerMap = new HashMap<>();

    @Override
    public void handle(@NotNull Message message, Long id) {
        final MessageHandler<?> messageHandler = handlerMap.get(message.getClass());
        messageHandler.handleMessage(message, id);
    }

    @Override
    public <T extends Message> void registerHandler(@NotNull Class<T> clazz, MessageHandler<T> handler) {
        handlerMap.put(clazz, handler);
    }
}
