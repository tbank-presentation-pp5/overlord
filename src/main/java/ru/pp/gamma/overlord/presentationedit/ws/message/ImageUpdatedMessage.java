package ru.pp.gamma.overlord.presentationedit.ws.message;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import ru.pp.gamma.overlord.presentationedit.ws.message.common.PresentationEditBaseMessage;
import ru.pp.gamma.overlord.presentationedit.ws.message.common.PresentationEditMessageType;

@Accessors(chain = true)
@Getter
@Setter
public class ImageUpdatedMessage extends PresentationEditBaseMessage {
    private PresentationEditMessageType type = PresentationEditMessageType.IMAGE_UPDATED;

    private Long fieldId;
    private Long imageId;
    private String url;
}
