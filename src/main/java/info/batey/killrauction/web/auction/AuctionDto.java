package info.batey.killrauction.web.auction;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import info.batey.killrauction.domain.BidVo;

import java.util.List;

public class AuctionDto {

    private final String name;
    private final long expires;
    private final List<BidVo> bids;

    @JsonCreator
    public AuctionDto(@JsonProperty("name") String name, @JsonProperty("end") long end, @JsonProperty("bids") List<BidVo> bids) {
        this.name = name;
        this.bids = bids;
        this.expires = end;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AuctionDto that = (AuctionDto) o;

        if (expires != that.expires) return false;
        if (bids != null ? !bids.equals(that.bids) : that.bids != null) return false;
        if (name != null ? !name.equals(that.name) : that.name != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (int) (expires ^ (expires >>> 32));
        result = 31 * result + (bids != null ? bids.hashCode() : 0);
        return result;
    }
}
