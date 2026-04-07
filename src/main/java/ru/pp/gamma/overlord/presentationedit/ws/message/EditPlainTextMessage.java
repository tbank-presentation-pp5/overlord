package ru.pp.gamma.overlord.presentationedit.ws.message;

import lombok.Getter;
import lombok.Setter;
import ru.pp.gamma.overlord.presentationedit.ws.message.common.PresentationEditBaseMessage;
import ru.pp.gamma.overlord.presentationedit.ws.message.common.PresentationEditMessageType;

@Getter
@Setter
public class EditPlainTextMessage extends PresentationEditBaseMessage {
    private PresentationEditMessageType type = PresentationEditMessageType.EDIT_PLAIN_TEXT;

    private String text;
    private Long fieldId;
}
