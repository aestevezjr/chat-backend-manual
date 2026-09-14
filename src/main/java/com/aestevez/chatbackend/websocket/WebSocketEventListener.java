package com.aestevez.chatbackend.websocket;

import java.time.LocalDateTime;

import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import com.aestevez.chatbackend.dtos.MessageDto;
import com.aestevez.chatbackend.dtos.MessageType;
import com.aestevez.chatbackend.dtos.TypingDto;
import com.aestevez.chatbackend.services.ConnectedUsersService;

@Component 
public class WebSocketEventListener {
    private final ConnectedUsersService connectedUsersService;
    private final SimpMessagingTemplate messagingTemplate;

    public WebSocketEventListener(ConnectedUsersService connectedUsersService, SimpMessagingTemplate messagingTemplate) {
        this.connectedUsersService = connectedUsersService;
        this.messagingTemplate = messagingTemplate;
    }

     @EventListener 
    public void handleDisconnect(SessionDisconnectEvent event) {
        SimpMessageHeaderAccessor headerAccessor = SimpMessageHeaderAccessor.wrap(event.getMessage());
        String username = (String) headerAccessor.getSessionAttributes().get("username");

        if (username == null) {
            return;
        }

        connectedUsersService.removeUser(username);
        messagingTemplate.convertAndSend("/topic/users", connectedUsersService.getConnectedUsers());

        MessageDto  leaveMessage = new MessageDto(null, username, username + " se desconectó del chat", LocalDateTime.now(), MessageType.LEAVE);
        messagingTemplate.convertAndSend("/topic/messages", leaveMessage);
        messagingTemplate.convertAndSend("/topic/typing", new TypingDto(username, false));
    }
}
