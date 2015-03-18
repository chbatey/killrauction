package info.batey.killrauction;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.converter.MessageConverter;
import org.springframework.messaging.handler.invocation.HandlerMethodArgumentResolver;
import org.springframework.messaging.handler.invocation.HandlerMethodReturnValueHandler;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.config.annotation.*;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

@Configuration
@EnableWebSocket
@EnableWebSocketMessageBroker
public class WebSocketConfig extends AbstractWebSocketMessageBrokerConfigurer implements WebSocketConfigurer {
    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(new BidUpdatesHandler(), "/api/bid-stream");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/portfolio").withSockJS();
    }


    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.setApplicationDestinationPrefixes("/app");
        config.enableSimpleBroker("/queue", "/topic");
    }

    public static class BidUpdatesHandler extends TextWebSocketHandler {

        private static final Logger LOGGER = LoggerFactory.getLogger(BidUpdatesHandler.class);
        private final ScheduledExecutorService ses = Executors.newScheduledThreadPool(1);
        private ScheduledFuture<?> hello;

        @Override
        protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
            LOGGER.info("Message {} {}", session, message);
            super.handleTextMessage(session, message);
        }

        @Override
        public void afterConnectionEstablished(final WebSocketSession session) throws Exception {
            LOGGER.info("Connection established {}", session);
            hello = ses.scheduleAtFixedRate((Runnable) () -> {
                int i = 0;
                try {
                    LOGGER.info("Sending message");
                    session.sendMessage(new TextMessage("Hello " + i++));
                } catch (IOException e) {
                    LOGGER.error("Exception sending message", e);
                }
            }, 1, 1, TimeUnit.SECONDS);
        }

        @Override
        public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
            LOGGER.info("{} {}", session, status);
            hello.cancel(true);
        }
    }
}
