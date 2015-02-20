package info.batey.killrauction.web;

import info.batey.killrauction.Application;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class RestEndpoint {

    @Autowired(required = true)
    private Application.Blah something;

    @RequestMapping("/api/auction")
    public String helloWorld() {
        return "Hello world";
    }
}
