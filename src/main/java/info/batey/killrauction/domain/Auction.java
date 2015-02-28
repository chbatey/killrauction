package info.batey.killrauction.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.Instant;

public class Auction {

    private final String name;
    private final Instant expires;

    @JsonCreator
    public Auction(@JsonProperty("name") String name, @JsonProperty("end") long end) {
        this.name = name;
        this.expires = Instant.ofEpochMilli(end);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Auction auction = (Auction) o;

        if (expires != null ? !expires.equals(auction.expires) : auction.expires != null) return false;
        if (name != null ? !name.equals(auction.name) : auction.name != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (expires != null ? expires.hashCode() : 0);
        return result;
    }

    public String getName() {
        return name;
    }
}
