package ru.pp.gamma.overlord.presentationedit.handler;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketSession;
import ru.pp.gamma.overlord.presentation.service.PresentationSlideFieldService;
import ru.pp.gamma.overlord.presentationedit.service.PresentationEditWsSender;
import ru.pp.gamma.overlord.presentationedit.util.PresentationEditConsts;
import ru.pp.gamma.overlord.presentationedit.ws.message.EditPlainTextMessage;
import ru.pp.gamma.overlord.presentationedit.ws.message.common.PresentationEditBaseMessage;
import ru.pp.gamma.overlord.presentationedit.ws.message.common.PresentationEditMessageType;

@RequiredArgsConstructor
@Component
public class EditPlainTextHandler implements PresentationEditMessageHandler {

    private final PresentationSlideFieldService presentationSlideFieldService;
    private final PresentationEditWsSender presentationEditWsSender;

    @Override
    public boolean canHandle(PresentationEditBaseMessage presentationEditBaseMessage) {
        return PresentationEditMessageType.EDIT_PLAIN_TEXT.equals(presentationEditBaseMessage.getType());
    }

    @Override
    public void handle(WebSocketSession session, PresentationEditBaseMessage baseMessage) {
        EditPlainTextMessage message = (EditPlainTextMessage) baseMessage;
        presentationSlideFieldService.updateTextValue(message.getFieldId(), message.getText());
        presentationEditWsSender.notifyByPresentationId((long) session.getAttributes().get(PresentationEditConsts.PRESENTATION_ID_ATTRIBUTE), message);

    }

}
