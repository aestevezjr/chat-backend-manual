package com.aestevez.chatbackend.controllers;

import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import com.aestevez.chatbackend.dtos.MessageDto;
import com.aestevez.chatbackend.dtos.MessageType;
import com.aestevez.chatbackend.services.MessageService;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@WebMvcTest(MessageController.class)
public class MessageControllerTest {
    
    @Autowired 
    private MockMvc mockMvc;

    @MockitoBean 
    private MessageService messageService;

    @Test 
    void getMessages_devuelveElHistorialComoJson() throws Exception {
        when(messageService.getHistory()).thenReturn(List.of(
            new MessageDto(1L, "ana", "hola", null, MessageType.CHAT),
            new MessageDto(2L, "pedro", "buenas", null, MessageType.CHAT)
        ));

        mockMvc.perform(get("/api/messages"))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.length()").value(2))
            .andExpect(jsonPath("$[0].username").value("ana"))
            .andExpect(jsonPath("$[0].type").value("CHAT"))
            .andExpect(jsonPath("$[1].content").value("buenas"));
    }

    @Test 
    void getMessages_sinMensajes_devuelveArrayVacio() throws Exception {
        when(messageService.getHistory()).thenReturn(List.of());

        mockMvc.perform(get("/api/messages"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$").isEmpty());
    }

    @Test 
    void getMessages_compruebaConfiguracionCORSCorrecta() throws Exception {
        mockMvc.perform(get("/api/messages").header("Origin", "http://localhost:5173"))
            .andExpect(status().isOk())
            .andExpect(header().string("Access-Control-Allow-Origin", "http://localhost:5173"));
            
    }

    @Test 
    void getMessages_compruebaConfiguracionCORSOrigenNoPermitido() throws Exception {
        mockMvc.perform(get("/api/messages").header("Origin", "http://localhost:5172"))
            .andExpect(status().isForbidden())
            .andExpect(header().doesNotExist("Access-Control-Allow-Origin"));
            
    }
}
