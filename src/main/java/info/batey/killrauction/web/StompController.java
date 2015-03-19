package info.batey.killrauction.web;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import info.batey.killrauction.domain.BidVo;
import info.batey.killrauction.observablespike.BidService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import rx.Observable;
import rx.functions.Action1;

import javax.inject.Inject;

@Controller
public class StompController {

    private static final Logger LOGGER = LoggerFactory.getLogger(StompController.class);

    @Inject
    private BidService bidService;

    @Inject
    private SimpMessagingTemplate messagingTemplate;

    private ObjectMapper objectMapper = new ObjectMapper();

    @MessageMapping("/hello")
    public void handle(BidRequest bidRequest) {
        LOGGER.debug("Bid stream request for {}", bidRequest);
        Action1<BidVo> onNext = bidVo -> {
            LOGGER.debug("Sending bid {}", bidVo);
            try {
                messagingTemplate.convertAndSend("/topic/greetings", objectMapper.writeValueAsString(bidVo));
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        };
        Observable<BidVo> subscriptionToAuction = bidService.getSubscriptionToAuction(bidRequest.name, onNext);
        subscriptionToAuction.subscribe(onNext);

    }

    private static class BidRequest {
        @JsonProperty
        private String name;

        @Override
        public String toString() {
            return "BidRequest{" +
                    "name='" + name + '\'' +
                    '}';
        }
    }
}
