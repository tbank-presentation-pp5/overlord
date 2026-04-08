package ru.pp.gamma.overlord.presentationedit.ws.handler;

import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import ru.pp.gamma.overlord.presentationedit.service.SessionWSStorage;
import ru.pp.gamma.overlord.presentationedit.handler.PresentationEditMessageHandlerRegistry;
import ru.pp.gamma.overlord.presentationedit.mapper.PresentationEditWSMessageMapper;
import ru.pp.gamma.overlord.presentationedit.ws.message.common.PresentationEditBaseMessage;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import static ru.pp.gamma.overlord.presentationedit.util.PresentationEditConsts.PRESENTATION_ID_ATTRIBUTE;

@RequiredArgsConstructor
@Component
public class PresentationEditWSHandler extends TextWebSocketHandler {

    private final PresentationEditWSMessageMapper presentationEditWSMessageMapper;
    private final PresentationEditMessageHandlerRegistry presentationEditMessageHandlerRegistry;
    private final SessionWSStorage sessionWSStorage;

    @Override
    public void afterConnectionEstablished(@NonNull WebSocketSession session) {
        Long presentationId = (Long) session.getAttributes().get(PRESENTATION_ID_ATTRIBUTE);
        sessionWSStorage.wsSessions
                .computeIfAbsent(presentationId, id -> new CopyOnWriteArrayList<>())
                .add(session);
    }

    @Override
    public void afterConnectionClosed(@NonNull WebSocketSession session, @NonNull CloseStatus status) {
        Long presentationId = (Long) session.getAttributes().get(PRESENTATION_ID_ATTRIBUTE);
        List<WebSocketSession> sessions = sessionWSStorage.wsSessions.get(presentationId);
        if (sessions != null) {
            sessions.remove(session);
        }
    }

    @Override
    protected void handleTextMessage(@NonNull WebSocketSession session, TextMessage message) throws Exception {
        PresentationEditBaseMessage mappedMessage = presentationEditWSMessageMapper.map(message.getPayload());
        presentationEditMessageHandlerRegistry.handle(session, mappedMessage);
    }

}
