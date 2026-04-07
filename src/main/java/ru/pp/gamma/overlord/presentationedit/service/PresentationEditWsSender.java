package ru.pp.gamma.overlord.presentationedit.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import ru.pp.gamma.overlord.presentationedit.SessionWSStorage;
import ru.pp.gamma.overlord.presentationedit.ws.message.common.PresentationEditBaseMessage;

import java.io.IOException;

@RequiredArgsConstructor
@Service
public class PresentationEditWsSender {

    private final SessionWSStorage sessionWSStorage;
    private final ObjectMapper objectMapper;

    public void notifyByPresentationId(long presentationId, PresentationEditBaseMessage message) {

        try {
            String json = objectMapper.writeValueAsString(message);
            sessionWSStorage.wsSessions.get(presentationId).forEach(session -> {
                try {
                    session.sendMessage(new TextMessage(json));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

}
