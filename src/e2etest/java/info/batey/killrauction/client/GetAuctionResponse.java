package info.batey.killrauction.client;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import info.batey.killrauction.domain.BidVo;

import java.util.List;

public class GetAuctionResponse {
    private final String name;
    private final long expires;
    private final List<BidVo> bids;

    @JsonCreator
    public GetAuctionResponse(@JsonProperty("name") String name, @JsonProperty("expires") long expires, @JsonProperty("bids") List<BidVo> bids) {
        this.name = name;
        this.expires = expires;
        this.bids = bids;
    }

    public String getName() {
        return name;
    }

    public long getExpires() {
        return expires;
    }

    public List<BidVo> getBids() {
        return bids;
    }
}
