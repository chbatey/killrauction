package info.batey.killrauction.service;

import info.batey.killrauction.domain.Auction;
import info.batey.killrauction.infrastruture.AuctionDao;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class AuctionApplicationServiceTest {

    private AuctionApplicationService underTest;

    @Mock
    private AuctionDao auctionDao;

    @Before
    public void setUp() throws Exception {
        underTest = new AuctionApplicationService(auctionDao, bidService);
    }

    @Test
    public void getAuction() throws Exception {
        String auctionName = "blah";
        Instant ends = Instant.ofEpochMilli(1);
        Optional<Auction> auction = Optional.of(new Auction(auctionName, ends));
        given(auctionDao.getAuction(auctionName)).willReturn(auction);

        Optional<Auction> actualAuction = underTest.getAuction(auctionName);

        assertSame(auction, actualAuction);
    }

    @Test
    public void createAuction() throws Exception {
        String auctionName = "blah";
        Instant ends = Instant.ofEpochMilli(1);

        Auction auction = underTest.createAuction(auctionName, ends);

        verify(auctionDao).createAuction(any(Auction.class));
        assertEquals(auctionName, auction.getName());
        assertEquals(ends, auction.getEnds());
    }

    @Test
    public void getAuctionThatDoesNotExist() throws Exception {
        given(auctionDao.getAuction(anyString())).willReturn(Optional.empty());
        assertEquals(Optional.<Auction>empty(), underTest.getAuction("any"));
    }

    @Test
    public void recordsBid() throws Exception {
        String auctionName = "blah";

        underTest.placeBid(auctionName, "user", 100l);

        verify(auctionDao).placeBid(auctionName, "user", 100l);
    }

    @Test
    public void getAllAuctionsFromDao() throws Exception {
        Auction auctionOne = mock(Auction.class);
        Auction auctionTwo = mock(Auction.class);
        List<Auction> expectedAuctions = Arrays.asList(auctionOne, auctionTwo);
        given(auctionDao.getAllAuctionsSparse()).willReturn(expectedAuctions);

        List<Auction> actualAuactions = underTest.getAuctions();

        assertSame(expectedAuctions, actualAuactions);
    }
}