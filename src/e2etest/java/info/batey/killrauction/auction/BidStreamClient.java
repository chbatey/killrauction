package info.batey.killrauction.auction;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import info.batey.killrauction.domain.BidVo;
import info.batey.killrauction.web.StompController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.converter.StringMessageConverter;
import org.springframework.messaging.simp.stomp.StompFrameHandler;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;
import org.springframework.web.socket.sockjs.client.SockJsClient;
import org.springframework.web.socket.sockjs.client.Transport;
import org.springframework.web.socket.sockjs.client.WebSocketTransport;

import java.io.IOException;
import java.lang.reflect.Type;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

public class BidStreamClient {

    private static final Logger LOGGER = LoggerFactory.getLogger(BidStreamClient.class);

    private BlockingQueue<BidVo> bids = new ArrayBlockingQueue<>(100);

    private ObjectMapper objectMapper = new ObjectMapper();

    public BidStreamClient(String auction) {
        List<Transport> transports = Arrays.asList(new WebSocketTransport(new StandardWebSocketClient()));
        SockJsClient sockJsClient = new SockJsClient(transports);
        WebSocketStompClient webSocketStompClient = new WebSocketStompClient(sockJsClient);
        webSocketStompClient.setMessageConverter(new StringMessageConverter());

        webSocketStompClient.connect("ws://localhost:8080/ws", new StompSessionHandlerAdapter() {
            @Override
            public void afterConnected(StompSession session, StompHeaders connectedHeaders) {

                session.subscribe("/topic/" + auction, new StompFrameHandler() {
                    @Override
                    public Type getPayloadType(StompHeaders headers) {
                        return String.class;
                    }

                    @Override
                    public void handleFrame(StompHeaders headers, Object payload) {
                        LOGGER.debug("Received message {}", payload);
                        try {
                            bids.add(objectMapper.readValue(payload.toString(), BidVo.class));
                        } catch (IOException e) {
                            LOGGER.warn("Unable to parse message as BidVo {}", payload);
                        }
                    }
                });

                StompController.BidRequest bidRequest = new StompController.BidRequest(auction);
                try {
                    session.send("/oldbids", new ObjectMapper().writeValueAsString(bidRequest));
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public List<BidVo> blockForMessages(int number, Duration timeout) {
        List<BidVo> toReturn = new ArrayList<>();
        for (int i = 0; i < number; i++) {
            try {
                BidVo poll = bids.poll(timeout.toMillis(), TimeUnit.MILLISECONDS);
                if (poll == null) {
                    return toReturn;
                } else {
                    toReturn.add(poll);
                }
            } catch (InterruptedException e) {
                LOGGER.warn("Interruped while waiting for bids, exiting", e);
                Thread.currentThread().interrupt();
            }
        }
        return toReturn;
    }
}
