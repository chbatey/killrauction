package info.batey.killrauction.web.auction;

import info.batey.killrauction.domain.Auction;
import info.batey.killrauction.domain.AuctionBid;
import info.batey.killrauction.service.AuctionApplicationService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.security.Principal;
import java.time.Instant;
import java.util.Collections;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class AuctionEndpointTest {

    private AuctionEndpoint underTest;

    @Mock
    private AuctionApplicationService auctionApplicationService;

    @Before
    public void setUp() throws Exception {
        underTest = new AuctionEndpoint(auctionApplicationService);
    }

    @Test
    public void shouldSendAuctionToService() throws Exception {
        //given
        String name = "name";
        Instant now = Instant.now();
        long end = now.toEpochMilli();
        AuctionDto auctionDto = new AuctionDto(name, end, Collections.emptyList());
        //when
        underTest.create(auctionDto);
        //then
        verify(auctionApplicationService).createAuction(name, now);
    }

    @Test
    public void getAuction() throws Exception {
        //given
        String auctionName = "name";
        Instant now = Instant.now();
        AuctionDto expectedAuctionDto = new AuctionDto(auctionName, now.toEpochMilli(), Collections.emptyList());
        Optional<Auction> primedAuction = Optional.of(new Auction(auctionName, now));
        given(auctionApplicationService.getAuction(auctionName)).willReturn(primedAuction);
        //when
        AuctionDto actualAuctionDto = underTest.get(auctionName);
        //then
        assertEquals(expectedAuctionDto, actualAuctionDto);
    }

    @Test
    public void addsBidToAuction() throws Exception {
        //given
        String auctionName = "ipad";
        Principal user = () -> "user";
        AuctionBid auctionBid = new AuctionBid(auctionName, 100);
        //when
        underTest.placeBid(auctionName, auctionBid, user);
        //then
        verify(auctionApplicationService).placeBid(auctionName, user.getName(), auctionBid.getAmount());
    }
}