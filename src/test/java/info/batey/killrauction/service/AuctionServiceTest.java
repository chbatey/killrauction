package info.batey.killrauction.service;

import info.batey.killrauction.domain.Auction;
import org.junit.Before;
import org.junit.Test;

import java.util.Optional;

import static org.junit.Assert.*;

public class AuctionServiceTest {

    private AuctionService underTest;

    @Before
    public void setUp() throws Exception {
        underTest = new AuctionService();
    }

    @Test
    public void storeAndRetrieveAuction() throws Exception {
        String auctionName = "blah";
        Auction auction = new Auction(auctionName, 1);
        Auction returnedFromCreate = underTest.createAuction(auction);
        assertEquals(auction, returnedFromCreate);
        assertEquals(Optional.of(auction), underTest.getAuction(auctionName));
    }

    @Test
    public void getAuctionThatDoesNotExist() throws Exception {
        assertEquals(Optional.empty(), underTest.getAuction("any"));
    }
}