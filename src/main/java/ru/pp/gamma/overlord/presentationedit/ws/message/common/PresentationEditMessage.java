package ru.pp.gamma.overlord.presentationedit.ws.message.common;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PresentationEditMessage extends PresentationEditBaseMessage {
    private PresentationEditMessageType type;
}
