package seabattle.msgsystem;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import seabattle.authorization.service.UserService;
import seabattle.websocket.WebSocketService;
import org.springframework.web.socket.*;


import javax.validation.constraints.NotNull;
import java.io.IOException;


public class SocketHandler extends TextWebSocketHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(MessageHandlerContainerImpl.class);

    private final MessageHandlerContainer messageHandlerContainer;

    private final WebSocketService webSocketService;

    private final ObjectMapper objectMapper;

    public SocketHandler(@NotNull MessageHandlerContainer messageHandlerContainer, @NotNull WebSocketService webSocketService, ObjectMapper objectMapper) {
        this.messageHandlerContainer = messageHandlerContainer;
        this.webSocketService = webSocketService;
        this.objectMapper = objectMapper;
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession webSocketSession) {
        final Long id = (Long) webSocketSession.getAttributes().get("userId");
        if (id == null) {
            try {
                webSocketSession.close();
            } catch (IOException ignore) {
            }
        } else {
            webSocketService.registerUser(id, webSocketSession);
        }
    }

    @Override
    protected void handleTextMessage(WebSocketSession webSocketSession, TextMessage message) {
        if (!webSocketSession.isOpen()) {
            return;
        }
        final Long userId = (Long) webSocketSession.getAttributes().get("userId");
        if (userId == null ) {
            try {
                webSocketSession.close();
            } catch (IOException ignore) {
            }
        } else {
            handleMessage(userId, message);
        }
    }

    private void handleMessage(Long userId, TextMessage text) {
        final Message message;
        try {
            message = objectMapper.readValue(text.getPayload(), Message.class);
        } catch (IOException ex) {
            LOGGER.error("wrong json format at game response", ex);
            return;
        }
        try {
            messageHandlerContainer.handle(message, userId);
        } catch (HandleException e) {
            LOGGER.error("Can't handle message of type " + message.getClass().getName() + " with content: " + text, e);
        }
    }

}
