package seabattle.msgsystem;

import seabattle.game.gamesession.GameSession;
import seabattle.game.gamesession.GameSessionService;
import seabattle.game.gamesession.GameSessionStatus;
import seabattle.game.messages.MsgFireCoordinates;

import javax.annotation.PostConstruct;
import javax.validation.constraints.NotNull;

@SuppressWarnings("unused")
public class MsgFireCoordinatesHandler extends MessageHandler<MsgFireCoordinates> {

    @NotNull
    private GameSessionService gameSessionService;

    @NotNull
    private MessageHandlerContainer messageHandlerContainer;

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
                    throw new IllegalStateException("It's not currently this player's move!");
                }
            }
        } else {
            throw new IllegalArgumentException("Player is not currently playing!");
        }
    }
}
