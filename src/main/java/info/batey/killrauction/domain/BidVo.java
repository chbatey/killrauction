package info.batey.killrauction.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class BidVo {
    //todo: turn to vo
    private final String user;
    private final Long amount;

    @JsonCreator
    public BidVo(@JsonProperty("user") String user, @JsonProperty("amount") Long amount) {
        this.user = user;
        this.amount = amount;
    }

    public String getUser() {
        return user;
    }

    public Long getAmount() {
        return amount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BidVo bidVo = (BidVo) o;

        if (amount != null ? !amount.equals(bidVo.amount) : bidVo.amount != null) return false;
        if (user != null ? !user.equals(bidVo.user) : bidVo.user != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = user != null ? user.hashCode() : 0;
        result = 31 * result + (amount != null ? amount.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "BidVo{" +
                "user='" + user + '\'' +
                ", amount=" + amount +
                '}';
    }
}
