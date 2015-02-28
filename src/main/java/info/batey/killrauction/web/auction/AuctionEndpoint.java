package info.batey.killrauction.web.auction;

import info.batey.killrauction.domain.Auction;
import info.batey.killrauction.service.AuctionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;

@RestController
public class AuctionEndpoint {

    private static final Logger LOGGER = LoggerFactory.getLogger(AuctionEndpoint.class);

    private AuctionService auctionService;

    @Inject
    public AuctionEndpoint(AuctionService auctionService) {
        this.auctionService = auctionService;
    }

    @RequestMapping(value = "/api/auction", method = {RequestMethod.POST})
    @ResponseStatus(HttpStatus.CREATED)
    public void create(@RequestBody Auction auction) {
        LOGGER.debug("Incoming auction create {}", auction);
        auctionService.createAuction(auction);
    }

    @RequestMapping(value = "/api/auction/{auctionName}", method = {RequestMethod.GET}, produces = {MediaType.APPLICATION_JSON_VALUE})
    @ResponseStatus(HttpStatus.CREATED)
    public Auction get(@PathVariable String auctionName) {
        return auctionService.getAuction(auctionName).orElseThrow(() -> new RuntimeException("oh dear"));
    }
}
