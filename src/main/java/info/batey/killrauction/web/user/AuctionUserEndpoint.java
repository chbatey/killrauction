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
    public void createUser(@RequestBody UserCreate userCreate) throws UserExistsException {
        LOGGER.debug("Received create user request {}", userCreate);
        if (!auctionUserDao.createUser(userCreate)) throw new UserExistsException();
    }

    @RequestMapping(value = "/blah", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.CREATED)
    @Timed
    public void blah() throws UserExistsException {
        LOGGER.debug("wah bah");


//        throw new RuntimeException("bhaha");
    }

    // todo not a fan of exceptions, look for alternative once have wifi
    @ResponseStatus(HttpStatus.PRECONDITION_FAILED)
    public class UserExistsException extends Exception {

    }
}
