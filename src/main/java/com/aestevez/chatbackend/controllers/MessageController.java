package com.aestevez.chatbackend.controllers;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.aestevez.chatbackend.dtos.MessageDto;
import com.aestevez.chatbackend.services.MessageService;

@RestController 
public class MessageController {

    private final MessageService messageService;

    public MessageController (MessageService messageService) {
        this.messageService = messageService;
    }

    @GetMapping("/api/messages")
    public List<MessageDto> getMessages() {
        return messageService.getHistory();
    }
}