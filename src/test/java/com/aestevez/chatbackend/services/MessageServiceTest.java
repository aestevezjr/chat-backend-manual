package com.aestevez.chatbackend.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;

import com.aestevez.chatbackend.dtos.MessageDto;
import com.aestevez.chatbackend.dtos.MessageType;
import com.aestevez.chatbackend.mappers.MessageMapper;
import com.aestevez.chatbackend.models.Message;
import com.aestevez.chatbackend.repositories.MessageRepository;

@ExtendWith (MockitoExtension.class)
public class MessageServiceTest {

    @Mock 
    private MessageRepository messageRepository;

    @Mock 
    private MessageMapper messageMapper;

    @InjectMocks  
    private MessageService messageService;

    @Test
    void getHistory_devuelveLosMensajesMapeadosEnElMismoOrden() {
        Message primero = new Message("ana", "hola");
        Message segundo = new Message("pedro", "buenas");
        MessageDto dtoPrimero = new MessageDto(1L, "ana", "hola", null, MessageType.CHAT);
        MessageDto dtoSegundo = new MessageDto(2L, "pedro", "buenas", null, MessageType.CHAT);

        when(messageRepository.findAllByOrderByIdAsc()).thenReturn(List.of(primero, segundo));
        when(messageMapper.toDto(primero)).thenReturn(dtoPrimero);
        when(messageMapper.toDto(segundo)).thenReturn(dtoSegundo);

        List<MessageDto> resultado = messageService.getHistory();

        assertThat(resultado).containsExactly(dtoPrimero, dtoSegundo);
    }

    @Test
    void getHistory_sinMensajes_devuelveListaVaciaYNoUsaElMapper() {
        when(messageRepository.findAllByOrderByIdAsc()).thenReturn(List.of());

        List<MessageDto> resultado = messageService.getHistory();

        assertThat(resultado).isEmpty();
        verifyNoInteractions(messageMapper);
    }

    @Test 
    void sendMessage_guardaLosDatosRecibidosYDevuelveElDtoDelMensajeGuardado(){
        MessageDto entrada = new MessageDto(null, "ana", "hola", null, MessageType.CHAT);
        Message guardado = new Message("ana", "hola");
        MessageDto salida = new MessageDto(5L,"ana", "hola", null, MessageType.CHAT);

        when(messageRepository.save(any(Message.class))).thenReturn(guardado);
        when(messageMapper.toDto(guardado)).thenReturn(salida);

        MessageDto resultado = messageService.sendMessage(entrada);

        ArgumentCaptor<Message> captor = ArgumentCaptor.forClass(Message.class);
        verify(messageRepository).save(captor.capture());
        assertThat(captor.getValue().getUsername()).isEqualTo("ana");
        assertThat(captor.getValue().getContent()).isEqualTo("hola");
        assertThat(resultado).isEqualTo(salida);
    }

    @Test 
    void sendMessage_lanzaExcepcionDelRepositorioAlGuardar() {
        MessageDto messageDto = new MessageDto(null, "agustin", "hola", null, MessageType.CHAT);

        when(messageRepository.save(any(Message.class))).thenThrow(new DataIntegrityViolationException("No se pudo guardar"));

        assertThatThrownBy(() -> messageService.sendMessage(messageDto)).isInstanceOf(DataIntegrityViolationException.class).hasMessage("No se pudo guardar");

        verifyNoInteractions(messageMapper);


    }
}
