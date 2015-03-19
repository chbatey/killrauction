package info.batey.killrauction.web;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class StompController {

    @MessageMapping("/greeting")
    @SendTo("/topic/greetings")
    public String handle(String greeting) {
        return "[" + System.currentTimeMillis() + ": " + greeting;
    }
}
