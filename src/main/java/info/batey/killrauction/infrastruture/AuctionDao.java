package info.batey.killrauction.infrastruture;

import com.datastax.driver.core.BoundStatement;
import com.datastax.driver.core.PreparedStatement;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.Session;
import com.datastax.driver.core.utils.UUIDs;
import info.batey.killrauction.domain.Auction;
import info.batey.killrauction.domain.BidVo;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class AuctionDao {

    private Session session;
    private PreparedStatement createAuction;
    private PreparedStatement getAuction;
    private PreparedStatement storeBid;

    @Inject
    public AuctionDao(Session session) {
        this.session = session;
    }

    @PostConstruct
    public void prepareStatements() {
        createAuction = session.prepare("insert INTO auctions (name, bid_amount , bid_time, ends ) VALUES ( ?, -1, ?, ?)");
        getAuction = session.prepare("select * from auctions where name = ?");
        storeBid = session.prepare("INSERT INTO auctions (name, bid_time , bid_amount , bid_user) VALUES ( ?, ?, ?, ?);");
    }

    public void createAuction(Auction auction) {
        BoundStatement bound = createAuction.bind(auction.getName(), UUIDs.timeBased(), auction.getEnds().toEpochMilli());
        session.execute(bound);
    }

    public void placeBid(String auctionName, String user, Long amount) {
        BoundStatement bound = storeBid.bind(auctionName, UUIDs.timeBased(), amount, user);
        session.execute(bound);
    }

    public Optional<Auction> getAuction(String auctionName) {
        BoundStatement bound = getAuction.bind(auctionName);
        List<Row> rows = session.execute(bound).all();
        AuctionBuilder collect = rows.stream().collect(AuctionBuilder::new, AuctionBuilder::accept, AuctionBuilder::combine);
        return Optional.of(new Auction(collect.name, Instant.ofEpochMilli(collect.end), collect.bids));
    }

    private static class AuctionBuilder {
        private String name;
        private long end;
        private List<BidVo> bids = new ArrayList<>();

        public void accept(Row row) {
            this.name = row.getString("name");
            this.end = row.getLong("ends");

            long bid_amount = row.getLong("bid_amount");
            if (bid_amount != -1) {
                this.bids.add(new BidVo(row.getString("bid_user"), bid_amount));
            }
        }

        public void combine(AuctionBuilder that) {
            assert(this.name.equals(that.name));
            assert(this.end == that.end);
            this.bids.addAll(that.bids);
        }
    }
}
