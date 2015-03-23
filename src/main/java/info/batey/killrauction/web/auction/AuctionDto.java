package info.batey.killrauction.web.auction;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import info.batey.killrauction.domain.BidVo;

import java.util.List;

public class AuctionDto {

    private final String name;
    private final long expires;
    private final List<BidVo> bids;
    private final String owner;

    @JsonCreator
    public AuctionDto(@JsonProperty("name") String name,
                      @JsonProperty("owner") String owner,
                      @JsonProperty("end") long end,
                      @JsonProperty("bids") List<BidVo> bids) {
        this.name = name;
        this.owner = owner;
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

    public String getOwner() {
        return owner;
    }

    @Override
    public String toString() {
        return "AuctionDto{" +
                "name='" + name + '\'' +
                ", expires=" + expires +
                ", bids=" + bids +
                ", owner='" + owner + '\'' +
                '}';
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AuctionDto that = (AuctionDto) o;

        if (expires != that.expires) return false;
        if (bids != null ? !bids.equals(that.bids) : that.bids != null) return false;
        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        if (owner != null ? !owner.equals(that.owner) : that.owner != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (int) (expires ^ (expires >>> 32));
        result = 31 * result + (bids != null ? bids.hashCode() : 0);
        result = 31 * result + (owner != null ? owner.hashCode() : 0);
        return result;
    }
}
