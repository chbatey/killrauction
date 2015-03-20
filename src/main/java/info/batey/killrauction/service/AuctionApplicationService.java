package info.batey.killrauction.service;

import info.batey.killrauction.domain.Auction;
import info.batey.killrauction.domain.BidVo;
import info.batey.killrauction.infrastruture.AuctionDao;
import info.batey.killrauction.observablespike.BidService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Component
public class AuctionApplicationService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AuctionApplicationService.class);

    private AuctionDao auctionDao;

    private BidService bidService;

    @Inject
    public AuctionApplicationService(AuctionDao auctionDao, BidService bidService) {
        this.auctionDao = auctionDao;
        this.bidService = bidService;
    }

    public Auction createAuction(String auctionName, Instant endTime) {
        LOGGER.debug("Storing auction {}", auctionName);
        Auction auction = new Auction(auctionName, endTime);
        bidService.addAuction(auction);
        auctionDao.createAuction(auction);
        return auction;
    }

    public Optional<Auction> getAuction(String name) {
        LOGGER.debug("Retrieving auction {}", name);
        return auctionDao.getAuction(name);
    }

    public void placeBid(String auctionName, String user, Long auctionBid) {
        auctionDao.placeBid(auctionName, user, auctionBid);
        bidService.recordBid(auctionName, new BidVo(user, auctionBid));
    }

    public List<Auction> getAuctions() {
        return auctionDao.getAllAuctionsSparse();
    }
}
