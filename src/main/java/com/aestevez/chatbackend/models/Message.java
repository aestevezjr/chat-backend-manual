package com.aestevez.chatbackend.models;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;

@Entity 
@Table (name="messages")
public class Message {
    @Id 
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;

    private String content;

    private LocalDateTime timestamp;

    protected Message() {
        // constructor vacío requerido por Hibernate (lo usa por reflexión), no se usa directamente
    }

    public Message(String username, String content) {
        this.username = username;
        this.content = content;
    }

    @PrePersist 
    public void prePersist() {
        this.timestamp = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getContent() {
        return content;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }
}
