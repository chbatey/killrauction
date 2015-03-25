package info.batey.killrauction.service;

import com.datastax.driver.core.utils.UUIDs;
import info.batey.killrauction.domain.Auction;
import info.batey.killrauction.domain.BidVo;
import info.batey.killrauction.infrastruture.AuctionDao;
import info.batey.killrauction.observablespike.BidService;
import org.joda.time.DateTimeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import java.time.Instant;
import java.time.temporal.TemporalAccessor;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

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

    public Auction createAuction(String auctionName, String owner, Instant endTime) {
        LOGGER.debug("Storing auction {}", auctionName);
        Auction auction = new Auction(auctionName, owner, endTime);
        bidService.addAuction(auction);
        auctionDao.createAuction(auction);
        return auction;
    }

    public Optional<Auction> getAuction(String name) {
        LOGGER.debug("Retrieving auction {}", name);
        return auctionDao.getAuction(name);
    }

    public void placeBid(String auctionName, String user, Long auctionBid) {
        UUID uuid = auctionDao.placeBid(auctionName, user, auctionBid);
        //todo: convert from 1952 to 1970 epoch
        bidService.recordBid(auctionName, new BidVo(user, auctionBid, Instant.ofEpochMilli(UUIDs.unixTimestamp(uuid))));
    }

    public List<Auction> getAuctions() {
        return auctionDao.getAllAuctionsSparse();
    }
}
