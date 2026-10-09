package com.aestevez.chatbackend.mappers;

import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;
import static org.assertj.core.api.Assertions.assertThat;

import com.aestevez.chatbackend.dtos.MessageDto;
import com.aestevez.chatbackend.dtos.MessageType;
import com.aestevez.chatbackend.models.Message;

public class MessageMapperTest {
    private final MessageMapper mapper = new MessageMapper();

    @Test 
    void toDto_copiaLosCamposDeLaEntidad() {
        Message message = new Message("ana", "hola");
        ReflectionTestUtils.setField(message, "id", 7L);
        message.prePersist();

        MessageDto dto = mapper.toDto(message);

        assertThat(dto.id()).isEqualTo(7L);
        assertThat(dto.username()).isEqualTo("ana");
        assertThat(dto.content()).isEqualTo("hola");
        assertThat(dto.timestamp()).isEqualTo(message.getTimestamp());
    }

    @Test
    void toDto_siempreMarcaElMensajeComoCHAT() {
        Message message = new Message("beto", "buenas");

        MessageDto dto = mapper.toDto(message);

        assertThat(dto.type()).isEqualTo(MessageType.CHAT);
    }

    @Test
    void toDto_conEntidadSinGuardar_dejaIdYTimestampEnNull() {
        Message message = new Message("agustin", "hola");

        MessageDto dto = mapper.toDto(message);

        assertThat(dto.id()).isNull();
        assertThat(dto.timestamp()).isNull();
    }
}
