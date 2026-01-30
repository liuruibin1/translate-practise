package com.xxx;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableDubbo
public class XXXGatewayServer {

    public static void main(String[] args) {
        SpringApplication.run(XXXGatewayServer.class, args);
    }

    //@Bean
    //public WebSocketHandler customWebSocketHandler() {
    //    return session -> {
    //        Flux<WebSocketMessage> serverPings = Flux
    //                .interval(Duration.ofSeconds(30))
    //                .map(time -> session.pingMessage(data -> data.wrap("Ping".getBytes())));
    //        return session.send(serverPings);
    //    };
    //}

}
