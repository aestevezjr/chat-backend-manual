package com.aestevez.chatbackend.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.aestevez.chatbackend.models.Message;

public interface MessageRepository extends JpaRepository<Message, Long> {
    
    List<Message> findAllByOrderByIdAsc();
}
