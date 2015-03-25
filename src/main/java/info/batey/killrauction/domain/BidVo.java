package info.batey.killrauction.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.Instant;

public class BidVo {

    private final String user;
    private final Long amount;
    private final Instant time;

    @JsonCreator
    public BidVo(@JsonProperty("user") String user,
                 @JsonProperty("amount") Long amount,
                 @JsonProperty("time") Instant time) {
        this.user = user;
        this.amount = amount;
        this.time = time;
    }

    public String getUser() {
        return user;
    }

    public Long getAmount() {
        return amount;
    }

    public Instant getDate() {
        return time;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BidVo bidVo = (BidVo) o;

        if (amount != null ? !amount.equals(bidVo.amount) : bidVo.amount != null) return false;
        if (time != null ? !time.equals(bidVo.time) : bidVo.time != null) return false;
        if (user != null ? !user.equals(bidVo.user) : bidVo.user != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = user != null ? user.hashCode() : 0;
        result = 31 * result + (amount != null ? amount.hashCode() : 0);
        result = 31 * result + (time != null ? time.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "BidVo{" +
                "user='" + user + '\'' +
                ", amount=" + amount +
                ", time=" + time +
                '}';
    }
}
