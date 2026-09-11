package com.aestevez.chatbackend.mappers;

import org.springframework.stereotype.Component;

import com.aestevez.chatbackend.dtos.MessageDto;
import com.aestevez.chatbackend.dtos.MessageType;
import com.aestevez.chatbackend.models.Message;

@Component 
public class MessageMapper {
    
    public MessageDto toDto (Message message) {
        return new MessageDto(
            message.getId(),
            message.getUsername(),
            message.getContent(),
            message.getTimestamp(),
            MessageType.CHAT
        );
    }
}
