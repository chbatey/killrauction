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
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.core.IsCollectionContaining.hasItems;
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
        String owner = "owner";
        Instant now = Instant.now();
        long end = now.toEpochMilli();
        Principal user = () -> owner;
        AuctionDto auctionDto = new AuctionDto(name, null, end, Collections.emptyList());
        //when
        underTest.create(auctionDto, user);
        //then
        verify(auctionApplicationService).createAuction(name, owner, now);
    }

    @Test
    public void getAuction() throws Exception {
        //given
        String auctionName = "name";
        Instant now = Instant.now();
        String owner = "Chris";
        AuctionDto expectedAuctionDto = new AuctionDto(auctionName, owner, now.toEpochMilli(), Collections.emptyList());
        Optional<Auction> primedAuction = Optional.of(new Auction(auctionName, owner, now));
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

    @Test
    public void getAllAuctions() throws Exception {
        //given
        Instant now = Instant.now();
        given(auctionApplicationService.getAuctions()).willReturn(Arrays.asList(
            new Auction("one", "owner1", now),
            new Auction("two", "owner2", now)
        ));

        //when
        List<AuctionDto> actualAuctions = underTest.getAllAuction();

        //then
        assertThat(actualAuctions, hasItems(
                new AuctionDto("one", "owner1", now.toEpochMilli(), Collections.emptyList()),
                new AuctionDto("two", "owner2", now.toEpochMilli(), Collections.emptyList())
        ));
    }
}