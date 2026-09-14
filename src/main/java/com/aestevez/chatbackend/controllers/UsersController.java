package com.aestevez.chatbackend.controllers;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.aestevez.chatbackend.services.ConnectedUsersService;

@RestController 
public class UsersController {
    
    private final ConnectedUsersService connectedUsersService;

    public UsersController(ConnectedUsersService connectedUsersService){
        this.connectedUsersService = connectedUsersService;
    }

    @GetMapping ("/api/users")
    public List<String> getUsers() {
        return connectedUsersService.getConnectedUsers();
    }
}
