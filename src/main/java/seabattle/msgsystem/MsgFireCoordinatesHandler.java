package seabattle.msgsystem;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import seabattle.game.gamesession.GameService;
import seabattle.game.gamesession.GameSession;
import seabattle.game.gamesession.GameSessionService;
import seabattle.game.messages.MsgError;
import seabattle.game.messages.MsgFireCoordinates;
import seabattle.websocket.WebSocketService;

import javax.annotation.PostConstruct;
import javax.validation.constraints.NotNull;
import java.io.IOException;


@SuppressWarnings({"unused", "SpringAutowiredFieldsWarningInspection"})
@Component
public class MsgFireCoordinatesHandler extends MessageHandler<MsgFireCoordinates> {

    private static final Logger LOGGER = LoggerFactory.getLogger(MsgFireCoordinatesHandler.class);

    @NotNull
    private GameSessionService gameSessionService;

    @NotNull
    private MessageHandlerContainer messageHandlerContainer;

    @NotNull
    private GameService gameService;

    @Autowired
    private WebSocketService webSocketService;

    public MsgFireCoordinatesHandler(@NotNull GameSessionService gameSessionService,
                                     @NotNull MessageHandlerContainer messageHandlerContainer,
                                     @NotNull GameService gameService) {
        super(MsgFireCoordinates.class);
        this.gameSessionService = gameSessionService;
        this.messageHandlerContainer = messageHandlerContainer;
        this.gameService = gameService;
    }

    @PostConstruct
    private void init() {
        messageHandlerContainer.registerHandler(MsgFireCoordinates.class, this);
    }

    @Override
    public void handle(MsgFireCoordinates cast, Long id) {
        if (gameSessionService.isPlaying(id)) {
            GameSession gameSession = gameSessionService.getGameSession(id);
            if (gameSession.getAttackingPlayer().getPlayerId().equals(id)) {
                gameService.addTask(gameSession.getSessionId(),
                        new Thread(()-> gameSessionService.makeMove(gameSession, cast.getCoordinates())));
            } else {
                try {
                    webSocketService.sendMessage(id, new MsgError("It's not currently this player's move! "));
                } catch (IOException ex) {
                    LOGGER.warn("Unable to send message");
                }
            }
        } else {
            try {
                webSocketService.sendMessage(id, new MsgError("Player is not currently playing!"));
            } catch (IOException ex) {
                LOGGER.warn("Unable to send message");
            }
        }

    }
}
