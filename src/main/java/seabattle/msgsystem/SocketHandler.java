package seabattle.msgsystem;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import seabattle.authorization.service.UserService;
import seabattle.authorization.views.UserView;
import seabattle.game.gamesession.GameSessionService;
import seabattle.game.messages.MsgError;
import seabattle.game.player.UserPlayer;
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
                         @NotNull GameSessionService gameSessionService,
                         @NotNull WebSocketService webSocketService,
                         ObjectMapper objectMapper) {
        this.messageHandlerContainer = messageHandlerContainer;
        this.userService = userService;
        this.gameSessionService = gameSessionService;
        this.webSocketService = webSocketService;
        this.objectMapper = objectMapper;
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession webSocketSession) {
        final String login = (String) webSocketSession.getAttributes().get("currentUser");
        final UserPlayer connectedPlayer = new UserPlayer();
        if (login != null) {
            final UserView userView = userService.getByLoginOrEmail(login);

            if (userView == null) {
                try {
                    webSocketSession.close();
                } catch (IOException ignore) {
                    LOGGER.warn("Can't close session");
                }
            } else {
                connectedPlayer.setUser(userView);
                connectedPlayer.setUsername(userView.getLogin());
                connectedPlayer.setScore(userView.getScore());
            }
        }

        webSocketService.registerUser(connectedPlayer, webSocketSession);
        gameSessionService.addPlayer(connectedPlayer);
    }

    @Override
    protected void handleTextMessage(WebSocketSession webSocketSession, TextMessage message) {
        if (!webSocketSession.isOpen()) {
            return;
        }
        if (!webSocketService.isConnected(webSocketSession.getId())) {
            try {
                webSocketSession.sendMessage(new TextMessage(objectMapper.writeValueAsString(
                        new MsgError("Non-in-game WebSocketSession "))));
                webSocketSession.close();
            } catch (IOException ignore) {
                LOGGER.warn("Can't close session");
            }
        } else {
            handleMessage(webSocketService.getUserId(webSocketSession.getId()), message);
        }
    }

    private void handleMessage(Long userId, TextMessage text) {
        final Message message;
        try {
            message = objectMapper.readValue(text.getPayload(), Message.class);
        } catch (IOException ex) {
            try {
                webSocketService.sendMessage(userId, new MsgError("Wrong json format at game response"));
            } catch (IOException sendEx) {
                LOGGER.warn("Unable to send message");
            }
            LOGGER.error("wrong json format at game response", ex);
            return;
        }
        try {
            messageHandlerContainer.handle(message, userId);
        } catch (HandleException e) {
            try {
                webSocketService.sendMessage(userId, new MsgError("Can't handle message of type "
                        + message.getClass().getName() + " with content: " + text));
            } catch (IOException sendEx) {
                LOGGER.warn("Unable to send message");
            }
            LOGGER.error("Can't handle message of type " + message.getClass().getName() + " with content: " + text, e);
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession webSocketSession, CloseStatus status) throws Exception {
        gameSessionService.deleteWaitingPlayer(webSocketService.removeUser(webSocketSession.getId()));
    }
}
