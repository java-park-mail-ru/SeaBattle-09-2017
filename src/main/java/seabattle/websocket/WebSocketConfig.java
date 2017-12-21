package seabattle.websocket;

import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.web.socket.server.support.HttpSessionHandshakeInterceptor;

import javax.validation.constraints.NotNull;

@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

    @NotNull
    private final WebSocketHandler webSocketHandler;

    public WebSocketConfig(@NotNull WebSocketHandler webSocketHandler) {
        this.webSocketHandler = webSocketHandler;
    }

    @Override
    public void registerWebSocketHandlers(@NotNull WebSocketHandlerRegistry registry) {
        registry.addHandler(webSocketHandler, "/game")
                .addInterceptors(new HttpSessionHandshakeInterceptor())
                .setAllowedOrigins("http://localhost:8080", "http://sea-battle-front.herokuapp.com",
                        "http://top-sea-battle.herokuapp.com", "https://sbattle.ru");
    }
}
