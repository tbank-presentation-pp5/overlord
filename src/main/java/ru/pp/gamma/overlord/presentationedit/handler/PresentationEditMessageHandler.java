package ru.pp.gamma.overlord.presentationedit.handler;

import org.springframework.web.socket.WebSocketSession;
import ru.pp.gamma.overlord.presentationedit.ws.message.common.PresentationEditBaseMessage;

public interface PresentationEditMessageHandler {

    boolean canHandle(PresentationEditBaseMessage presentationEditBaseMessage);

    void handle(WebSocketSession session, PresentationEditBaseMessage baseMessage);
}
