package com.aestevez.chatbackend.websocket;

import java.time.LocalDateTime;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import com.aestevez.chatbackend.dtos.MessageDto;
import com.aestevez.chatbackend.dtos.MessageType;
import com.aestevez.chatbackend.dtos.TypingDto;
import com.aestevez.chatbackend.services.ConnectedUsersService;
import com.aestevez.chatbackend.services.MessageService;

import jakarta.validation.Valid;

@Controller 
public class ChatWebSocketController {
    
    private final MessageService messageService;
    private final ConnectedUsersService connectedUsersService;
    private final SimpMessagingTemplate messagingTemplate;

    public ChatWebSocketController(MessageService messageService,
                                    ConnectedUsersService connectedUsersService,
                                    SimpMessagingTemplate messagingTemplate) {
        this.messageService = messageService;
        this.connectedUsersService = connectedUsersService;
        this.messagingTemplate = messagingTemplate;
    }

    @MessageMapping("/chat.send")
    @SendTo("/topic/messages")
    public MessageDto sendMessage(@Valid MessageDto incoming) {
        return messageService.sendMessage(incoming);
    }

    @MessageMapping ("/chat.join")
    @SendTo ("/topic/messages")
    public MessageDto joinChat(@Payload String username, SimpMessageHeaderAccessor headerAccessor) {
        headerAccessor.getSessionAttributes().put("username", username);
        connectedUsersService.addUser(username);
        messagingTemplate.convertAndSend("/topic/users", connectedUsersService.getConnectedUsers());

        return new MessageDto(null, username, username + " se unió al chat", LocalDateTime.now(), MessageType.JOIN);
    }

    @MessageMapping ("/chat.typing")
    @SendTo ("/topic/typing")
    public TypingDto typing(TypingDto typingDto) {
        return typingDto;
    }
}
