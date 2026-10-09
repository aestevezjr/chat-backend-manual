package com.aestevez.chatbackend.repositories;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.boot.jpa.test.autoconfigure.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import com.aestevez.chatbackend.models.Message;

@DataJpaTest 
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles ("test")
public class MessageRepositoryTest {
    
    @Autowired 
    private MessageRepository messageRepository;

    @Autowired 
    private TestEntityManager entityManager;

    @Test 
    void save_asignaIdYTimestamp() {
        Message guardado = messageRepository.save(new Message("ana", "hola"));

        assertThat(guardado.getId()).isNotNull();
        assertThat(guardado.getTimestamp()).isNotNull();
    }

    @Test 
    void findAllByOrderByIdAsc_devuelveLosMensajesEnordenDeInsercion() {
        messageRepository.save(new Message("ana", "primero"));
        messageRepository.save(new Message("pedro", "segundo"));
        messageRepository.save(new Message("ana", "tercero"));

        List<Message> resultado = messageRepository.findAllByOrderByIdAsc();

        assertThat(resultado)
            .extracting(Message::getContent)
            .containsExactly("primero", "segundo", "tercero");
    }

    @Test 
    void findAllByOrderByIdAsc_sinDatos_devuelveListaVacia() {
        assertThat(messageRepository.findAllByOrderByIdAsc()).isEmpty();
    }

    @Test
    void save_guardaEnLaBaseDeDatos() {
        Message mensaje = new Message("ana", "hola");
        Message resultado = messageRepository.save(mensaje);

        entityManager.flush();
        entityManager.clear();

        List<Message> mensajes = messageRepository.findAllByOrderByIdAsc();

        assertThat(mensajes).hasSize(1);
        Message primero = mensajes.get(0);
        assertThat(primero)
            .isNotSameAs(mensaje);
        assertThat(resultado.getId()).isEqualTo(primero.getId());
        assertThat(primero.getUsername()).isEqualTo(mensaje.getUsername());
        assertThat(primero.getContent()).isEqualTo(mensaje.getContent());
    }
}
