package info.batey.killrauction.domain;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Auction {
    private final String name;
    private final Instant ends;
    private final List<BidVo> bids = new ArrayList<>();

    public Auction(String name, Instant ends) {
        this.name = name;
        this.ends = ends;
    }

    public String getName() {
        return name;
    }

    public Instant getEnds() {
        return ends;
    }

    public List<BidVo> getBids() {
        return Collections.unmodifiableList(bids);
    }

    @Override
    public String toString() {
        return "Auction{" +
                "name='" + name + '\'' +
                ", ends=" + ends +
                ", bids=" + bids +
                '}';
    }

    public void bid(BidVo auctionBidVo) {
        this.bids.add(auctionBidVo);
    }
}
