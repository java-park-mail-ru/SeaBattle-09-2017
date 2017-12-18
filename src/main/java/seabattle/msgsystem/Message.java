package seabattle.msgsystem;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import seabattle.game.messages.*;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "class")
@JsonSubTypes({
        @JsonSubTypes.Type(MsgShipPosition.class),
        @JsonSubTypes.Type(MsgLobbyCreated.class),
        @JsonSubTypes.Type(MsgResultMove.class),
        @JsonSubTypes.Type(MsgEndGame.class),
        @JsonSubTypes.Type(MsgGameStarted.class),
        @JsonSubTypes.Type(MsgYouInQueue.class),
        @JsonSubTypes.Type(MsgFireCoordinates.class),
        @JsonSubTypes.Type(MsgError.class),
        @JsonSubTypes.Type(MsgShipIsDestroyed.class),
        @JsonSubTypes.Type(MsgPing.class),
        @JsonSubTypes.Type(MsgGeneratedShips.class),
        @JsonSubTypes.Type(MsgJoinGame.class)
})

public abstract class Message {
}