package ru.pp.gamma.overlord.presentationedit.mapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.pp.gamma.overlord.presentationedit.ws.message.EditPlainTextMessage;
import ru.pp.gamma.overlord.presentationedit.ws.message.common.PresentationEditBaseMessage;
import ru.pp.gamma.overlord.presentationedit.ws.message.common.PresentationEditMessage;
import ru.pp.gamma.overlord.presentationedit.ws.message.common.PresentationEditMessageType;

import java.util.Map;

@Slf4j
@Component
public class PresentationEditWSMessageMapper {
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper()
            .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

    private static final Map<PresentationEditMessageType, Class<?>> TYPE_TO_CLASS = Map.of(
            PresentationEditMessageType.EDIT_PLAIN_TEXT, EditPlainTextMessage.class
    );

    public PresentationEditBaseMessage map(String json) {
        PresentationEditMessageType type = getTypeMessage(json);

        try {
            return (PresentationEditBaseMessage) OBJECT_MAPPER.readValue(json, TYPE_TO_CLASS.get(type));
        } catch (JsonProcessingException e) {
            log.error("Ошибка при десериализации сообщения изменения презентации (WS). Сообщение не Соответствует типу. Type: {}, json: {}", type, json);
            throw new RuntimeException(e);
        }
    }

    private PresentationEditMessageType getTypeMessage(String json) {
        try {
            return OBJECT_MAPPER.readValue(json, PresentationEditMessage.class).getType();
        } catch (JsonProcessingException e) {
            log.error("Ошибка десериализации при получении типа сообщения в WS, json: {}", json);
            throw new RuntimeException(e);
        }
    }
}
