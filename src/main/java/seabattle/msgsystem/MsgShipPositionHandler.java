package seabattle.msgsystem;

import org.springframework.stereotype.Component;
import seabattle.game.field.Field;
import seabattle.game.gamesession.GameSession;
import seabattle.game.gamesession.GameSessionService;
import seabattle.game.gamesession.GameSessionStatus;
import seabattle.game.messages.MsgShipPosition;
import seabattle.game.ship.ShipsValidator;

import javax.annotation.PostConstruct;
import javax.validation.constraints.NotNull;

@SuppressWarnings("unused")
@Component
public class MsgShipPositionHandler extends MessageHandler<MsgShipPosition> {

    @NotNull
    private GameSessionService gameSessionService;

    @NotNull
    private MessageHandlerContainer messageHandlerContainer;

    @NotNull
    private ShipsValidator shipsValidator;


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
        if (!shipsValidator.isValidShips(cast.getShips())) {
            throw new IllegalArgumentException("Ships isn't vaild!");
        }
        if (gameSessionService.isPlaying(id)) {
            GameSession gameSession = gameSessionService.getGameSession(id);
            if (gameSession.getStatus().equals(GameSessionStatus.SETUP)) {
                if (gameSession.getPlayer1Id().equals(id)) {
                    gameSession.getPlayer1().setShips(cast.getShips());
                    gameSession.setField1(new Field(cast.getShips()));
                } else if (gameSession.getPlayer2Id().equals(id)) {
                    gameSession.getPlayer2().setShips(cast.getShips());
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
