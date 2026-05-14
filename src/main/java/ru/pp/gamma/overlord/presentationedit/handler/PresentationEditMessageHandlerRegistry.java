package ru.pp.gamma.overlord.presentationedit.handler;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketSession;
import ru.pp.gamma.overlord.presentationedit.ws.message.common.PresentationEditBaseMessage;

import java.util.List;

@Component
public class PresentationEditMessageHandlerRegistry {

    private final List<PresentationEditMessageHandler> HANDLERS;

    public PresentationEditMessageHandlerRegistry(List<PresentationEditMessageHandler> handlers) {
        HANDLERS = handlers;
    }

    public void handle(WebSocketSession session, PresentationEditBaseMessage message) {
        HANDLERS.stream()
                .filter(h -> h.canHandle(message))
                .findFirst()
                .ifPresent(h -> h.handle(session, message));
    }
}
