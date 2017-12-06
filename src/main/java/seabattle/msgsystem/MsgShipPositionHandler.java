package seabattle.msgsystem;

import org.springframework.stereotype.Component;
import seabattle.game.field.Cell;
import seabattle.game.field.Field;
import seabattle.game.gamesession.GameSession;
import seabattle.game.gamesession.GameSessionService;
import seabattle.game.gamesession.GameSessionStatus;
import seabattle.game.messages.MsgShipPosition;
import seabattle.game.ship.Ship;

import javax.annotation.PostConstruct;
import javax.validation.constraints.NotNull;
import java.util.List;

@SuppressWarnings("unused")
@Component
public class MsgShipPositionHandler extends MessageHandler<MsgShipPosition> {

    @NotNull
    private GameSessionService gameSessionService;

    @NotNull
    private MessageHandlerContainer messageHandlerContainer;

    private static final Integer FIELD_SIZE = 10;
    private static final Integer FOUR_DECKER_COUNT = 1;
    private static final Integer THREE_DECKER_COUNT = 2;
    private static final Integer TWO_DECKER_COUNT = 3;
    private static final Integer ONE_DECKER_COUNT = 4;

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
        if (!isValidShips(cast.getShips())) {
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

    @SuppressWarnings("OverlyComplexMethod")
    private boolean isValidShips(List<Ship> ships) {
        Integer fourDeckerCount = 0;
        Integer threeDeckerCount = 0;
        Integer twoDeckerCount = 0;
        Integer oneDeckerCount = 0;
        final Integer fourDecker = 4;
        final Integer threeDecker = 3;
        final Integer twoDecker = 2;
        final Integer oneDecker = 1;
        for (Ship ship: ships) {
            if (ship.getLength().equals(oneDecker)) {
                oneDeckerCount++;
                if (oneDeckerCount > ONE_DECKER_COUNT) {
                    return false;
                }
            } else if (ship.getLength().equals(twoDecker)) {
                twoDeckerCount++;
                if (threeDeckerCount > TWO_DECKER_COUNT) {
                    return false;
                }
            } else if (ship.getLength().equals(threeDecker)) {
                threeDeckerCount++;
                if (threeDeckerCount > THREE_DECKER_COUNT) {
                    return false;
                }
            } else if (ship.getLength().equals(fourDecker)) {
                fourDeckerCount++;
                if (fourDeckerCount > FOUR_DECKER_COUNT) {
                    return false;
                }
            } else {
                return false;
            }
            for (Cell cell : ship.getCells()) {
                final Boolean checkRow = (cell.getRowPos() < 0) || (cell.getRowPos() >= FIELD_SIZE);
                final Boolean checkCol = (cell.getColPos() < 0) || (cell.getColPos() >= FIELD_SIZE);
                if (checkCol || checkRow) {
                    return false;
                }
            }
        }

        if (!oneDeckerCount.equals(ONE_DECKER_COUNT) || !twoDeckerCount.equals(TWO_DECKER_COUNT)) {
            return false;
        }
        return (threeDeckerCount.equals(THREE_DECKER_COUNT) && fourDeckerCount.equals(FOUR_DECKER_COUNT));
    }
}
