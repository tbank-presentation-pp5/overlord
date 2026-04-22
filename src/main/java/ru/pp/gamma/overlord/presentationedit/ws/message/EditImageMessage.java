package ru.pp.gamma.overlord.presentationedit.ws.message;

import lombok.Getter;
import lombok.Setter;
import ru.pp.gamma.overlord.presentationedit.ws.message.common.PresentationEditBaseMessage;
import ru.pp.gamma.overlord.presentationedit.ws.message.common.PresentationEditMessageType;

@Getter
@Setter
public class EditImageMessage extends PresentationEditBaseMessage {
    private PresentationEditMessageType type;

    private Long fieldId;
    private Long imageId;
}
