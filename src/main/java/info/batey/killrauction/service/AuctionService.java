package info.batey.killrauction.service;

import info.batey.killrauction.domain.Auction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Component
public class AuctionService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AuctionService.class);

    private Map<String, Auction> auctions = new HashMap<>();

    public Auction createAuction(Auction auction) {
        LOGGER.debug("Storing auction {}", auction);
        auctions.put(auction.getName(), auction);
        return auction;
    }

    public Optional<Auction> getAuction(String name) {
        Optional<Auction> auction = Optional.ofNullable(auctions.get(name));
        LOGGER.debug("Retrieving auction {}", auction);
        return auction;
    }
}
