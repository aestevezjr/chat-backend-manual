package com.aestevez.chatbackend.controllers;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController 
public class MessageController {

    @GetMapping("/api/messages")
    public List<String> getMessages() {
        return List.of(
            "¡Hola! Bienvenido al chat.",
            "Este es un mensaje de prueba.",
            "Todavía no hay base de datos, esto es una lista fija."
        );
    }
}