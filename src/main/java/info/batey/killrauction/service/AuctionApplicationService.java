package info.batey.killrauction.service;

import info.batey.killrauction.domain.Auction;
import info.batey.killrauction.domain.BidVo;
import info.batey.killrauction.infrastruture.AuctionDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Component
public class AuctionApplicationService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AuctionApplicationService.class);

    private AuctionDao auctionDao;

    @Inject
    public AuctionApplicationService(AuctionDao auctionDao) {
        this.auctionDao = auctionDao;
    }

    public Auction createAuction(String auctionName, Instant endTime) {
        LOGGER.debug("Storing auction {}", auctionName);
        Auction auction = new Auction(auctionName, endTime);
        auctionDao.createAuction(auction);
        return auction;
    }

    public Optional<Auction> getAuction(String name) {
        LOGGER.debug("Retrieving auction {}", name);
        return auctionDao.getAuction(name);
    }

    public void placeBid(String auctionName, String user, Long auctionBid) {
        auctionDao.placeBid(auctionName, user, auctionBid);
    }
}
