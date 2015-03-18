package info.batey.killrauction.web;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

@Controller
public class StompController {
    @MessageMapping("/greeting")
    public String handle(String greeting) {
        return "[" + System.currentTimeMillis() + ": " + greeting;
    }
}
