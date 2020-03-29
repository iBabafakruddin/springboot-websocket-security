package com.baba.demo.config;

import java.security.Principal;
import java.util.Map;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.server.support.DefaultHandshakeHandler;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {
    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.enableSimpleBroker("/topic","/chat");
        config.setApplicationDestinationPrefixes("/app");
        //config.setUserDestinationPrefix("/user");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        //registry.addEndpoint("/chat-websocket");
        registry.addEndpoint("/chat-websocket")
                .setHandshakeHandler(new UserHandshakeHandler())
                .setAllowedOrigins("*")
                .withSockJS();
    }


    private class UserHandshakeHandler extends DefaultHandshakeHandler{

        @Override
        protected Principal determineUser(ServerHttpRequest request, WebSocketHandler wsHandler,
                                          Map<String, Object> attributes) {
            int radomOf10 = Double.valueOf(""+Math.random()*10).intValue();
            String user = radomOf10%2 == 0 ? "Baba" : "User1";
            if (radomOf10 == 6 || radomOf10 == 8 || radomOf10 == 3 || radomOf10 == 1) {
                user = "User2";
            }
            return new UsernamePasswordAuthenticationToken(user, null);
        }

    }

}