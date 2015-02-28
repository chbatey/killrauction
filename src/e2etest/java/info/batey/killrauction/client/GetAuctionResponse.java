package info.batey.killrauction.client;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class GetAuctionResponse {
    public final String name;
    private final long expires;

    @JsonCreator
    public GetAuctionResponse(@JsonProperty("name") String name, @JsonProperty("expires") long expires) {
        this.name = name;
        this.expires = expires;
    }

    public String getName() {
        return name;
    }

    public long getExpires() {
        return expires;
    }
}
