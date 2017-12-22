package seabattle.msgsystem;

import org.springframework.stereotype.Component;
import seabattle.game.field.Field;
import seabattle.game.gamesession.GameSession;
import seabattle.game.gamesession.GameSessionService;
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
                                  @NotNull MessageHandlerContainer messageHandlerContainer,
                                  @NotNull ShipsValidator shipsValidator) {
        super(MsgShipPosition.class);
        this.gameSessionService = gameSessionService;
        this.messageHandlerContainer = messageHandlerContainer;
        this.shipsValidator = shipsValidator;
    }

    @PostConstruct
    private void init() {
        messageHandlerContainer.registerHandler(MsgShipPosition.class, this);
    }

    @Override
    public void handle(MsgShipPosition cast, Long id) {
        if (!shipsValidator.isValidShips(cast.getShips())) {
            throw new IllegalArgumentException("Ships isn't valid!");
        }
        if (gameSessionService.isPlaying(id)) {
            GameSession gameSession = gameSessionService.getGameSession(id);
            if (gameSession.getPlayer1Id().equals(id)) {
                gameSession.setField1(new Field(cast.getShips()));
                gameSession.getPlayer1().setShips(cast.getShips());
            } else if (gameSession.getPlayer2Id().equals(id)) {
                gameSession.setField2(new Field(cast.getShips()));
                gameSession.getPlayer2().setShips(cast.getShips());
            } else {
                throw new IllegalArgumentException("Player is not in this session!");
            }
            gameSessionService.tryStartGame(gameSession);
        } else {
            throw new IllegalArgumentException("Player is not currently playing!");
        }
    }

}
