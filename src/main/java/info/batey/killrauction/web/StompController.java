package info.batey.killrauction.web;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import info.batey.killrauction.domain.BidVo;
import info.batey.killrauction.stream.BidService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import javax.inject.Inject;
import java.io.IOException;
import java.util.List;

@Controller
public class StompController {

    private static final Logger LOGGER = LoggerFactory.getLogger(StompController.class);

    @Inject
    private BidService bidService;

    @Inject
    private SimpMessagingTemplate messagingTemplate;


    private ObjectMapper objectMapper = new ObjectMapper();

    @MessageMapping("/oldbids")
    public void handle(String bidRequestText) throws IOException {
        BidRequest bidRequest = objectMapper.readValue(bidRequestText, BidRequest.class);
        LOGGER.debug("Bid stream request for {}", bidRequest);
        List<BidVo> oldBids = bidService.subscribe(bidRequest.name);

        LOGGER.debug("Sending old auction bids from DB: {}", bidRequest);

        String destination = "/topic/" + bidRequest.name;
        LOGGER.debug("Sending messages to {}", destination);
        for (BidVo bidVo : oldBids) {
            messagingTemplate.convertAndSend(destination, objectMapper.writeValueAsString(bidVo));
        }
    }

    public static class BidRequest {
        @JsonProperty
        private String name;

        public BidRequest(String name) {
            this.name = name;
        }

        public BidRequest() {
        }

        @Override
        public String toString() {
            return "BidRequest{" +
                    "name='" + name + '\'' +
                    '}';
        }
    }
}
