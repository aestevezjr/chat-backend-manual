package com.aestevez.chatbackend.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.aestevez.chatbackend.dtos.MessageDto;
import com.aestevez.chatbackend.mappers.MessageMapper;
import com.aestevez.chatbackend.repositories.MessageRepository;

@Service 
public class MessageService {
    
    private final MessageRepository messageRepository;
    private final MessageMapper messageMapper;

    public MessageService(MessageRepository messageRepository, MessageMapper messageMapper) {
        this.messageRepository = messageRepository;
        this.messageMapper = messageMapper;
    }

    public List<MessageDto> getHistory() {
        return messageRepository.findAllByOrderByIdAsc()
                .stream()
                .map(messageMapper::toDto)
                .toList();
    }
}
