package com.aestevez.chatbackend.websocket;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import com.aestevez.chatbackend.dtos.MessageDto;
import com.aestevez.chatbackend.services.MessageService;

import jakarta.validation.Valid;

@Controller 
public class ChatWebSocketController {
    
    private final MessageService messageService;

    public ChatWebSocketController(MessageService messageService) {
        this.messageService = messageService;
    }

    @MessageMapping("/chat.send")
    @SendTo("/topic/messages")
    public MessageDto sendMessage(@Valid MessageDto incoming) {
        return messageService.sendMessage(incoming);
    }
}
