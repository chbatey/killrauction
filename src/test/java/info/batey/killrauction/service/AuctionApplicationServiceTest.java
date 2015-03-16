package info.batey.killrauction.service;

import info.batey.killrauction.domain.Auction;
import info.batey.killrauction.domain.BidVo;
import org.junit.Before;
import org.junit.Test;

import java.time.Instant;
import java.util.Arrays;
import java.util.Optional;

import static org.junit.Assert.*;

public class AuctionApplicationServiceTest {

    private AuctionApplicationService underTest;

    @Before
    public void setUp() throws Exception {
        underTest = new AuctionApplicationService();
    }

    @Test
    public void storeAndRetrieveAuction() throws Exception {
        String auctionName = "blah";
        Instant ends = Instant.ofEpochMilli(1);
        Auction auction = new Auction(auctionName, ends);

        Auction returnedFromCreate = underTest.createAuction(auctionName, ends);

        assertEquals(auction.getName(), returnedFromCreate.getName());
        assertEquals(auction.getEnds(), returnedFromCreate.getEnds());
        assertEquals(auction.getBids(), returnedFromCreate.getBids());

        Auction auctionFromGet = underTest.getAuction(auctionName).get();
        assertEquals(auction.getName(), auctionFromGet.getName());
        assertEquals(auction.getEnds(), auctionFromGet.getEnds());
        assertEquals(auction.getBids(), auctionFromGet.getBids());
    }

    @Test
    public void getAuctionThatDoesNotExist() throws Exception {
        assertEquals(Optional.<Auction>empty(), underTest.getAuction("any"));
    }

    @Test
    public void recordsBid() throws Exception {
        String auctionName = "blah";
        Instant ends = Instant.ofEpochMilli(1);
        underTest.createAuction(auctionName, ends);

        underTest.placeBid(auctionName, "user", 100l);
        Optional<Auction> auction = underTest.getAuction(auctionName);

        assertEquals(Arrays.asList(new BidVo("user", 100l)), auction.get().getBids());
    }
}