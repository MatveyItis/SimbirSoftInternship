package ru.itis.maletskov.internship.configuration;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;
import ru.itis.maletskov.internship.model.Message;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

@Component
public class WebSocketChatEventListener {
    /*@Autowired
    private SimpMessageSendingOperations messagingTemplate;

    @EventListener
    public void handleWebSocketConnectListener(SessionConnectedEvent event) {
        System.out.println("Received new websocket message at " + (new Date(event.getTimestamp())).toString());
    }

    @EventListener
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
        String username = (String) headerAccessor.getSessionAttributes().get("username");
        if (username != null) {
            Message chatMessage = new Message();
            chatMessage.setSender(username);
            chatMessage.setText("text from messageBroker");
            messagingTemplate.convertAndSend("/topic/messages", chatMessage);
        }
    }*/
}
