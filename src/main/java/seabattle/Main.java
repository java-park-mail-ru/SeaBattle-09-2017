package seabattle;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.handler.PerConnectionWebSocketHandler;
import seabattle.msgsystem.SocketHandler;
import seabattle.websocket.WebSocketConfig;



@SpringBootApplication
public class Main {


    public static void main(String[] args) {
        SpringApplication.run(new Class[]{WebSocketConfig.class, Main.class}, args);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public WebSocketHandler webSocketHandler() {
        return new PerConnectionWebSocketHandler(SocketHandler.class);
    }

}
