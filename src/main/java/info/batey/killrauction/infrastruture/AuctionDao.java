package info.batey.killrauction.infrastruture;

import com.datastax.driver.core.BoundStatement;
import com.datastax.driver.core.PreparedStatement;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.Session;
import com.datastax.driver.core.utils.UUIDs;
import info.batey.killrauction.domain.Auction;
import info.batey.killrauction.domain.BidVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class AuctionDao {

    private final static Logger LOGGER = LoggerFactory.getLogger(AuctionDao.class);

    private Session session;
    private PreparedStatement createAuction;
    private PreparedStatement getAuction;
    private PreparedStatement storeBid;
    private PreparedStatement getAllAuctionSparse;
    private PreparedStatement getAuctionBids;

    @Inject
    public AuctionDao(Session session) {
        this.session = session;
    }

    @PostConstruct
    public void prepareStatements() {
        createAuction = session.prepare("insert INTO auctions (name, owner, ends) VALUES (?, ?, ?)");
        getAuction = session.prepare("select * from auctions where name = ?");
        getAuctionBids = session.prepare("select * from auction_bids where name = ?");
        getAllAuctionSparse = session.prepare("select * from auctions");
        storeBid = session.prepare("INSERT INTO auction_bids (name, bid_time , bid_amount , bid_user) VALUES ( ?, ?, ?, ?);");
    }

    public void createAuction(Auction auction) {
        BoundStatement bound = createAuction.bind(auction.getName(), auction.getOwner(), auction.getEnds().toEpochMilli());
        session.execute(bound);
    }

    public void placeBid(String auctionName, String user, Long amount) {
        BoundStatement bound = storeBid.bind(auctionName, UUIDs.timeBased(), amount, user);
        session.execute(bound);
    }

    public List<Auction> getAllAuctionsSparse() {
        BoundStatement bound = getAllAuctionSparse.bind();
        return session.execute(bound).all().stream().map(row ->
                new Auction(row.getString("name"), row.getString("owner"), Instant.ofEpochMilli(row.getLong("ends"))))
                .collect(Collectors.toList());
    }

    //todo make these async
    public Optional<Auction> getAuction(String auctionName) {

        BoundStatement auctionBoundStatement = getAuction.bind(auctionName);
        Row auction = session.execute(auctionBoundStatement).one();

        LOGGER.debug("Getting auction information for auction {} rows {}", auctionName, auction);

        BoundStatement bidsBound = getAuctionBids.bind(auctionName);
        List<BidVo> bids = session.execute(bidsBound).all().stream().map(row ->
                new BidVo(row.getString("bid_user"),
                        row.getLong("bid_amount"))).collect(Collectors.toList());

        return Optional.of(new Auction(auction.getString("name"),
                Instant.ofEpochMilli(auction.getLong("ends")),
                bids,
                auction.getString("owner")));
    }
}
