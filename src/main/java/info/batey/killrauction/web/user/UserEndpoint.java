package info.batey.killrauction.web.user;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.ws.rs.core.MediaType;

@RestController
public class UserEndpoint {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserEndpoint.class);

    @RequestMapping(value = "/api/user", method = RequestMethod.POST, consumes = {MediaType.APPLICATION_JSON})
    @ResponseStatus(HttpStatus.CREATED)
    public String createUser(@RequestBody UserCreate userCreate) {
        LOGGER.debug("Received create user request {}", userCreate);
        return "Hello world";
    }
}
