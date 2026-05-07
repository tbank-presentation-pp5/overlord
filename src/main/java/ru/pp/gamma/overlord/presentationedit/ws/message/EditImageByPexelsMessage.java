package ru.pp.gamma.overlord.presentationedit.ws.message;

import lombok.Getter;
import lombok.Setter;
import ru.pp.gamma.overlord.presentationedit.ws.message.common.PresentationEditBaseMessage;
import ru.pp.gamma.overlord.presentationedit.ws.message.common.PresentationEditMessageType;

@Getter
@Setter
public class EditImageByPexelsMessage extends PresentationEditBaseMessage {
    private PresentationEditMessageType type;

    private Long fieldId;
    private Long pexelsImageId;
}
