package info.batey.killrauction;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.AbstractWebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig extends AbstractWebSocketMessageBrokerConfigurer {

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws").withSockJS();
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.setApplicationDestinationPrefixes("/api");
        config.enableSimpleBroker("/queue", "/topic");
    }

//    @Override
//    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
//        registry.addHandler(new BidUpdatesHandler(), "/api/bid-stream");
//    }

//    public static class BidUpdatesHandler extends TextWebSocketHandler {
//
//        private static final Logger LOGGER = LoggerFactory.getLogger(BidUpdatesHandler.class);
//        private final ScheduledExecutorService ses = Executors.newScheduledThreadPool(1);
//        private ScheduledFuture<?> hello;
//
//        @Override
//        protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
//            LOGGER.info("Message {} {}", session, message);
//            super.handleTextMessage(session, message);
//        }
//
//        @Override
//        public void afterConnectionEstablished(final WebSocketSession session) throws Exception {
//            LOGGER.info("Connection established {}", session);
//            hello = ses.scheduleAtFixedRate((Runnable) () -> {
//                int i = 0;
//                try {
//                    LOGGER.info("Sending message");
//                    session.sendMessage(new TextMessage("Hello " + i++));
//                } catch (IOException e) {
//                    LOGGER.error("Exception sending message", e);
//                }
//            }, 1, 1, TimeUnit.SECONDS);
//        }
//
//        @Override
//        public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
//            LOGGER.info("{} {}", session, status);
//            hello.cancel(true);
//        }
//    }
}
