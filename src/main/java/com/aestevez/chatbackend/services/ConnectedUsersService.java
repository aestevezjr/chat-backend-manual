package com.aestevez.chatbackend.services;

import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Service;

@Service 
public class ConnectedUsersService {
    
    private final Set<String> connectedUsers = ConcurrentHashMap.newKeySet();

    public void addUser(String username) {
        connectedUsers.add(username);
    }

    public void removeUser(String username){
        connectedUsers.remove(username);
    }

    public List<String> getConnectedUsers() {
        return List.copyOf(connectedUsers);
    }
}
