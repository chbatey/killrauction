package info.batey.killrauction.service;

import info.batey.killrauction.domain.Auction;
import info.batey.killrauction.domain.BidVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Component
public class AuctionApplicationService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AuctionApplicationService.class);

    private Map<String, Auction> auctions = new HashMap<>();

    public Auction createAuction(String auctionName, Instant endTime) {
        LOGGER.debug("Storing auction {}", auctionName);
        Auction auction = new Auction(auctionName, endTime);
        auctions.put(auctionName, auction);
        return auction;
    }

    public Optional<Auction> getAuction(String name) {
        Optional<Auction> auction = Optional.ofNullable(auctions.get(name));
        LOGGER.debug("Retrieving auction {}", auction);
        return auction;
    }

    public void placeBid(String auctionName, String user, Long auctionBid) {
        auctions.get(auctionName).bid(new BidVo(user, auctionBid));
    }
}
