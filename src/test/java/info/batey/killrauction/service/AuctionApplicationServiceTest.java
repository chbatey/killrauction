package info.batey.killrauction.service;

import com.datastax.driver.core.utils.UUIDs;
import info.batey.killrauction.domain.Auction;
import info.batey.killrauction.domain.BidVo;
import info.batey.killrauction.infrastruture.AuctionDao;
import info.batey.killrauction.observablespike.BidService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class AuctionApplicationServiceTest {

    private AuctionApplicationService underTest;

    @Captor
    private ArgumentCaptor<BidVo> capture;

    @Mock
    private AuctionDao auctionDao;

    @Mock
    private BidService bidService;

    @Before
    public void setUp() throws Exception {
        underTest = new AuctionApplicationService(auctionDao, bidService);
    }

    @Test
    public void getAuction() throws Exception {
        String auctionName = "blah";
        String owner = "Chris";
        Instant ends = Instant.ofEpochMilli(1);
        Optional<Auction> auction = Optional.of(new Auction(auctionName, owner, ends));
        given(auctionDao.getAuction(auctionName)).willReturn(auction);

        Optional<Auction> actualAuction = underTest.getAuction(auctionName);

        assertSame(auction, actualAuction);
    }

    @Test
    public void createAuction() throws Exception {
        String auctionName = "blah";
        String owner = "Chris B";
        Instant ends = Instant.ofEpochMilli(1);

        Auction auction = underTest.createAuction(auctionName, owner, ends);

        verify(auctionDao).createAuction(any(Auction.class));
        assertEquals(auctionName, auction.getName());
        assertEquals(owner, auction.getOwner());
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
        UUID timeBased = UUIDs.timeBased();
        given(auctionDao.placeBid(anyString(), anyString(), anyLong())).willReturn(timeBased);

        underTest.placeBid(auctionName, "user", 100l);

        verify(auctionDao).placeBid(auctionName, "user", 100l);
        verify(bidService).recordBid(eq(auctionName), capture.capture());
        BidVo actualBidVo = capture.getValue();
        assertEquals("user", actualBidVo.getUser());
        assertEquals(new Long(100l), actualBidVo.getAmount());
        assertEquals(UUIDs.unixTimestamp(timeBased), actualBidVo.getTime().longValue());
    }

    @Test
    public void getAllAuctionsFromDao() throws Exception {
        Auction auctionOne = mock(Auction.class);
        Auction auctionTwo = mock(Auction.class);
        List<Auction> expectedAuctions = Arrays.asList(auctionOne, auctionTwo);
        given(auctionDao.getAllAuctionsSparse()).willReturn(expectedAuctions);

        List<Auction> actualAuctions = underTest.getAuctions();

        assertSame(expectedAuctions, actualAuctions);
    }
}