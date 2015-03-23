package info.batey.killrauction.domain;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Auction {
    private final String name;
    private final String owner;
    private final Instant ends;
    private final List<BidVo> bids = new ArrayList<>();

    public Auction(String name, String owner, Instant ends) {
        this.name = name;
        this.owner = owner;
        this.ends = ends;
    }

    public Auction(String name, Instant ends, List<BidVo> bids, String owner) {
        this.name = name;
        this.ends = ends;
        this.owner = owner;
        this.bids.addAll(bids);
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

    public String getOwner() {
        return owner;
    }

    @Override
    public String toString() {
        return "Auction{" +
                "name='" + name + '\'' +
                ", owner='" + owner + '\'' +
                ", ends=" + ends +
                ", bids=" + bids +
                '}';
    }
}
