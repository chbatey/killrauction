package info.batey.killrauction.web.auction;

import info.batey.killrauction.domain.AuctionBid;
import info.batey.killrauction.service.AuctionApplicationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.security.Principal;
import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class AuctionEndpoint {

    private static final Logger LOGGER = LoggerFactory.getLogger(AuctionEndpoint.class);

    private AuctionApplicationService auctionApplicationService;

    @Inject
    public AuctionEndpoint(AuctionApplicationService auctionApplicationService) {
        this.auctionApplicationService = auctionApplicationService;
    }

    @RequestMapping(value = "/api/auction/{auctionName}/bid", method = {RequestMethod.POST})
    @ResponseStatus(HttpStatus.CREATED)
    public void placeBid(@PathVariable String auctionName, @RequestBody AuctionBid auctionBid, Principal principal) {
        LOGGER.debug("Incoming auction bid {} for auction {}", auctionBid, auctionName);
        auctionApplicationService.placeBid(auctionName, principal.getName(), auctionBid.getAmount());
    }


    @RequestMapping(value = "/api/auction", method = {RequestMethod.POST})
    @ResponseStatus(HttpStatus.CREATED)
    public void create(@RequestBody AuctionDto auctionDto) {
        LOGGER.debug("Incoming auction create {}", auctionDto);
        auctionApplicationService.createAuction(auctionDto.getName(), Instant.ofEpochMilli(auctionDto.getExpires()));
    }

    @RequestMapping(value = "/api/auction", method = {RequestMethod.GET})
    @ResponseStatus(HttpStatus.OK)
    public List<AuctionDto> getAllAuction() {
        return auctionApplicationService.getAuctions().stream()
                .map(auction -> new AuctionDto(auction.getName(), auction.getEnds().toEpochMilli(), Collections.emptyList()))
                .collect(Collectors.toList());
    }

    @RequestMapping(value = "/api/auction/{auctionName}", method = {RequestMethod.GET}, produces = {MediaType.APPLICATION_JSON_VALUE})
    @ResponseStatus(HttpStatus.OK)
    public AuctionDto get(@PathVariable String auctionName) {
        return auctionApplicationService.getAuction(auctionName)
                .map(auction -> new AuctionDto(auction.getName(), auction.getEnds().toEpochMilli(), auction.getBids()))
                .orElseThrow(() -> new RuntimeException("oh dear"));
    }
}
