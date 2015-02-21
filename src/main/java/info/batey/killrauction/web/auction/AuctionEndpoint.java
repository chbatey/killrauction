package info.batey.killrauction.web.auction;

import info.batey.killrauction.Application;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class AuctionEndpoint {

    @RequestMapping("/api/auction")
    @ResponseStatus(HttpStatus.CREATED)
    public String helloWorld() {
        return "Hello world";
    }
}
