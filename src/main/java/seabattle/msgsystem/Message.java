package seabattle.msgsystem;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import seabattle.game.messages.MsgEndGame;
import seabattle.game.messages.MsgResultMove;
import seabattle.game.messages.MsgShipPosition;
import seabattle.game.messages.MsgLobbyCreated;

@JsonTypeInfo(use= JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "class")
@JsonSubTypes({
        @JsonSubTypes.Type(MsgShipPosition.class),
        @JsonSubTypes.Type(MsgLobbyCreated.class),
        @JsonSubTypes.Type(MsgResultMove.class),
        @JsonSubTypes.Type(MsgEndGame.class),
})
public abstract class Message {
}