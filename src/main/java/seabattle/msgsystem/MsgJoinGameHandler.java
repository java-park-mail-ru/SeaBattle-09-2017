package seabattle.msgsystem;

import org.springframework.stereotype.Component;
import seabattle.game.gamesession.GameSessionService;
import seabattle.game.messages.MsgJoinGame;

import javax.annotation.PostConstruct;
import javax.validation.constraints.NotNull;

@Component
public class MsgJoinGameHandler extends MessageHandler<MsgJoinGame> {
    @NotNull
    private GameSessionService gameSessionService;

    @NotNull
    private MessageHandlerContainer messageHandlerContainer;

    public MsgJoinGameHandler(@NotNull GameSessionService gameSessionService,
                          @NotNull MessageHandlerContainer messageHandlerContainer) {
        super(MsgJoinGame.class);
        this.gameSessionService = gameSessionService;
        this.messageHandlerContainer = messageHandlerContainer;
    }

    @PostConstruct
    private void init() {
        messageHandlerContainer.registerHandler(MsgJoinGame.class, this);
    }

    @Override
    public void handle(MsgJoinGame cast, Long id) {
        if (cast.getPlayWithBot().equals(Boolean.TRUE)) {
            gameSessionService.setupGameWithBot(id);
        } else {
            gameSessionService.addWaitingPlayer(id);
        }
    }
}
