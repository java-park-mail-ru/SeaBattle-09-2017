package seabattle.msgsystem;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(use= JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "class")
@JsonSubTypes({
        @JsonSubTypes.Type(MsgShipPosition.class),
        @JsonSubTypes.Type(MsgStartGame.class),
        @JsonSubTypes.Type(MsgResultMove.class),
        @JsonSubTypes.Type(MsgEndGame.class),
})
public abstract class Message {
}