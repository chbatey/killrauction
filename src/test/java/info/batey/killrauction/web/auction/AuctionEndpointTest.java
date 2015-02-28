package info.batey.killrauction.web.auction;

import info.batey.killrauction.domain.Auction;
import info.batey.killrauction.service.AuctionService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.time.Instant;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class AuctionEndpointTest {

    private AuctionEndpoint underTest;

    @Mock
    private AuctionService auctionService;

    @Before
    public void setUp() throws Exception {
        underTest = new AuctionEndpoint(auctionService);
    }

    @Test
    public void shouldSendAuctionToService() throws Exception {
        //given
        Auction auction = new Auction("name", Instant.now().toEpochMilli());
        //when
        underTest.create(auction);
        //then
        verify(auctionService).createAuction(auction);
    }

    @Test
    public void getAuction() throws Exception {
        //given
        Auction auction = new Auction("name", Instant.now().toEpochMilli());
        Optional<Auction> primedAuction = Optional.of(auction);
        given(auctionService.getAuction("name")).willReturn(primedAuction);
        //when
        Auction actualAuction = underTest.get("name");
        //then
        assertEquals(auction, actualAuction);
    }
}