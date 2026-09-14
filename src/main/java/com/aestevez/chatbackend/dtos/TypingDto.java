package com.aestevez.chatbackend.dtos;

public record TypingDto(
    String username,
    boolean typing
) {
    
}
