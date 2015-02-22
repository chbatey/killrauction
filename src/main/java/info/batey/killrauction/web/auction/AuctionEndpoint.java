package info.batey.killrauction.web.auction;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuctionEndpoint {

    @RequestMapping("/api/auction")
    @ResponseStatus(HttpStatus.CREATED)
    public String helloWorld() {
        return "Hello world";
    }
}
