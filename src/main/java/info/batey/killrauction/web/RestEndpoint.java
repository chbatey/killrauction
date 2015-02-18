package info.batey.killrauction.web;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RestEndpoint {
    @RequestMapping("/hello")
    public String helloWorld() {
        return "Hello world";
    }
}
