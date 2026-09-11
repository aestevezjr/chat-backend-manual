package com.aestevez.chatbackend.dtos;

import java.time.LocalDateTime;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record MessageDto(
    Long id,
    @NotBlank(message = "El username no puede estar vacío")
    String username,
    @NotBlank (message = "El contenido no puede estar vacío")
    @Size (max = 500, message = "El mensaje no puede superar los 500 caracteres")
    String content,
    LocalDateTime timestamp,
    MessageType type
) {
    
}
