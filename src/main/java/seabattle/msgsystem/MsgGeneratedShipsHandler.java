package seabattle.msgsystem;

import org.springframework.stereotype.Component;
import seabattle.game.gamesession.GameSessionService;
import seabattle.game.messages.MsgGeneratedShips;

import javax.annotation.PostConstruct;
import javax.validation.constraints.NotNull;

@Component
public class MsgGeneratedShipsHandler extends MessageHandler<MsgGeneratedShips> {
    @NotNull
    private GameSessionService gameSessionService;

    @NotNull
    private MessageHandlerContainer messageHandlerContainer;

    public MsgGeneratedShipsHandler(@NotNull GameSessionService gameSessionService,
                          @NotNull MessageHandlerContainer messageHandlerContainer) {
        super(MsgGeneratedShips.class);
        this.gameSessionService = gameSessionService;
        this.messageHandlerContainer = messageHandlerContainer;
    }

    @PostConstruct
    private void init() {
        messageHandlerContainer.registerHandler(MsgGeneratedShips.class, this);
    }

    @Override
    public void handle(MsgGeneratedShips cast, Long id) {
        gameSessionService.sendGeneratedShips(id);
    }
}
