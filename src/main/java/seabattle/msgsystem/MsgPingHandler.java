package seabattle.msgsystem;

import org.springframework.stereotype.Component;
import seabattle.game.gamesession.GameSessionService;
import seabattle.game.messages.MsgPing;

import javax.annotation.PostConstruct;
import javax.validation.constraints.NotNull;

@SuppressWarnings("unused")
@Component
public class MsgPingHandler extends MessageHandler<MsgPing> {

    @NotNull
    private GameSessionService gameSessionService;

    @NotNull
    private MessageHandlerContainer messageHandlerContainer;

    public MsgPingHandler(@NotNull GameSessionService gameSessionService,
                                  @NotNull MessageHandlerContainer messageHandlerContainer) {
        super(MsgPing.class);
        this.gameSessionService = gameSessionService;
        this.messageHandlerContainer = messageHandlerContainer;
    }

    @PostConstruct
    private void init() {
        messageHandlerContainer.registerHandler(MsgPing.class, this);
    }

    @Override
    public void handle(MsgPing cast, Long id) {
        gameSessionService.sendPingMessage(id);
    }
}
