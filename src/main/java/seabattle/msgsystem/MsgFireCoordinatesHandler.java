package seabattle.msgsystem;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import seabattle.game.gamesession.GameSession;
import seabattle.game.gamesession.GameSessionService;
import seabattle.game.gamesession.GameSessionStatus;
import seabattle.game.messages.MsgError;
import seabattle.game.messages.MsgFireCoordinates;
import seabattle.websocket.WebSocketService;

import javax.annotation.PostConstruct;
import javax.validation.constraints.NotNull;
import java.io.IOException;

@SuppressWarnings("unused")
@Component
public class MsgFireCoordinatesHandler extends MessageHandler<MsgFireCoordinates> {

    private static final Logger LOGGER = LoggerFactory.getLogger(MsgFireCoordinatesHandler.class);

    @NotNull
    private GameSessionService gameSessionService;

    @NotNull
    private MessageHandlerContainer messageHandlerContainer;

    @Autowired
    private WebSocketService webSocketService;

    public MsgFireCoordinatesHandler(@NotNull GameSessionService gameSessionService,
                                     @NotNull MessageHandlerContainer messageHandlerContainer) {
        super(MsgFireCoordinates.class);
        this.gameSessionService = gameSessionService;
        this.messageHandlerContainer = messageHandlerContainer;
    }

    @PostConstruct
    private void init() {
        messageHandlerContainer.registerHandler(MsgFireCoordinates.class, this);
    }

    @Override
    public void handle(MsgFireCoordinates cast, Long id) {
        if (gameSessionService.isPlaying(id)) {
            GameSession gameSession = gameSessionService.getGameSession(id);
            if (gameSession.getPlayer1Id().equals(id)) {
                if (gameSession.getStatus().equals(GameSessionStatus.MOVE_P1)
                        || gameSession.getPlayer2Id().equals(id)) {
                    gameSession.makeMove(cast.getCoordinates());
                } else {
                    try {
                        webSocketService.sendMessage(id, new MsgError("It's not currently this player's move!"));
                    } catch (IOException ex) {
                        LOGGER.warn("Unnable to send message");
                    }

                    throw new IllegalStateException("It's not currently this player's move!");
                }
            } else {
                try {
                    webSocketService.sendMessage(id, new MsgError("Player is not currently playing!"));
                } catch (IOException ex) {
                    LOGGER.warn("Unnable to send message");
                }
                throw new IllegalArgumentException("Player is not currently playing!");
            }
        }
    }
}
