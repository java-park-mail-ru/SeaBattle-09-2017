package seabattle.msgsystem;

import seabattle.game.field.Field;
import seabattle.game.gamesession.GameSession;
import seabattle.game.gamesession.GameSessionService;
import seabattle.game.gamesession.GameSessionStatus;
import seabattle.game.messages.MsgShipPosition;

import javax.annotation.PostConstruct;
import javax.validation.constraints.NotNull;

@SuppressWarnings("unused")
public class MsgShipPositionHandler extends MessageHandler<MsgShipPosition> {

    @NotNull
    private GameSessionService gameSessionService;

    @NotNull
    private MessageHandlerContainer messageHandlerContainer;

    public MsgShipPositionHandler(@NotNull GameSessionService gameSessionService,
                                  @NotNull MessageHandlerContainer messageHandlerContainer) {
        super(MsgShipPosition.class);
        this.gameSessionService = gameSessionService;
        this.messageHandlerContainer = messageHandlerContainer;
    }

    @PostConstruct
    private void init() {
        messageHandlerContainer.registerHandler(MsgShipPosition.class, this);
    }

    @Override
    public void handle(MsgShipPosition cast, Long id) {
        if (gameSessionService.isPlaying(id)) {
            GameSession gameSession = gameSessionService.getGameSession(id);
            if (gameSession.getStatus().equals(GameSessionStatus.SETUP)) {
                if (gameSession.getPlayer1Id().equals(id)) {
                    gameSession.setField1(new Field(cast.getShips()));
                } else if (gameSession.getPlayer2Id().equals(id)) {
                    gameSession.setField2(new Field(cast.getShips()));
                } else {
                    throw new IllegalArgumentException("Player is not in this session!");
                }
            } else {
                throw new IllegalStateException("Game is not in SETUP state!");
            }
            gameSessionService.tryStartGame(gameSession);
        } else {
            throw new IllegalArgumentException("Player is not currently playing!");
        }
    }
}
