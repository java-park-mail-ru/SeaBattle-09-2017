package seabattle.msgsystem;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import seabattle.authorization.service.UserService;
import seabattle.authorization.views.UserView;
import seabattle.game.gamesession.GameSessionService;
import seabattle.game.player.Player;
import seabattle.websocket.WebSocketService;
import org.springframework.web.socket.*;


import javax.validation.constraints.NotNull;
import java.io.IOException;


public class SocketHandler extends TextWebSocketHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(MessageHandlerContainerImpl.class);

    private final MessageHandlerContainer messageHandlerContainer;

    private final UserService userService;

    private final GameSessionService gameSessionService;

    private final WebSocketService webSocketService;

    private final ObjectMapper objectMapper;

    public SocketHandler(@NotNull MessageHandlerContainer messageHandlerContainer,
                         @NotNull UserService userService,
                         @NotNull GameSessionService gamseSessionService,
                         @NotNull WebSocketService webSocketService,
                         ObjectMapper objectMapper) {
        this.messageHandlerContainer = messageHandlerContainer;
        this.userService = userService;
        this.gameSessionService = gamseSessionService;
        this.webSocketService = webSocketService;
        this.objectMapper = objectMapper;
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession webSocketSession) {
        final String login = (String) webSocketSession.getAttributes().get("userLogin");
        if (login == null) {
            try {
                webSocketSession.close();
            } catch (IOException ignore) {
                LOGGER.warn("Can't close session");
            }
        } else {
            Player connectedPlayer = new Player();
            UserView userView = userService.getByLoginOrEmail(login);

            if (userView != null) {
                connectedPlayer.setUser(userView);
            }
            webSocketService.registerUser(connectedPlayer.getPlayerId(), webSocketSession);
            gameSessionService.addWaitingPlayer(connectedPlayer);

        }
    }

    @Override
    protected void handleTextMessage(WebSocketSession webSocketSession, TextMessage message) {
        if (!webSocketSession.isOpen()) {
            return;
        }
        final Long userId = (Long) webSocketSession.getAttributes().get("userId");
        if (userId == null) {
            try {
                webSocketSession.close();
            } catch (IOException ignore) {
                LOGGER.warn("Can't close session");
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
