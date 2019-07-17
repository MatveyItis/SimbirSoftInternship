package ru.itis.maletskov.internship.controller;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.util.HtmlUtils;
import ru.itis.maletskov.internship.model.Greeting;
import ru.itis.maletskov.internship.model.Message;

@Controller
public class GreetingController {
    @MessageMapping("/chat")
    @SendTo("/topic/messages")
    public Greeting greeting(Message message) throws Exception {
        Thread.sleep(200); // simulated delay
        return new Greeting(HtmlUtils.htmlEscape(message.getText() + "!"));
    }

    @GetMapping("/chat")
    public String chatPage() {
        return "chat";
    }
}
