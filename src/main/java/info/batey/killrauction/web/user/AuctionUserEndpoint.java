package info.batey.killrauction.web.user;

import com.codahale.metrics.annotation.Timed;
import info.batey.killrauction.infrastruture.AuctionUserDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;

@RestController
public class AuctionUserEndpoint {

    private static final Logger LOGGER = LoggerFactory.getLogger(AuctionUserEndpoint.class);
    private AuctionUserDao auctionUserDao;

    @Inject
    public AuctionUserEndpoint(AuctionUserDao auctionUserDao) {
        this.auctionUserDao = auctionUserDao;
    }

    @RequestMapping(value = "/api/user", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    @Timed
    public void createUser(@RequestBody UserCreate userCreate) {
        LOGGER.debug("Received create user request {}", userCreate);
        auctionUserDao.createUser(userCreate);
    }
}
