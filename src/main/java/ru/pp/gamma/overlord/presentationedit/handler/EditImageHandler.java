package ru.pp.gamma.overlord.presentationedit.handler;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.socket.WebSocketSession;
import ru.pp.gamma.overlord.image.entity.Image;
import ru.pp.gamma.overlord.image.mapper.ImageMapper;
import ru.pp.gamma.overlord.image.service.ImageService;
import ru.pp.gamma.overlord.presentation.entity.SlideField;
import ru.pp.gamma.overlord.presentation.service.PresentationSlideFieldService;
import ru.pp.gamma.overlord.presentationedit.redis.producer.EditPresentationRedisNotifier;
import ru.pp.gamma.overlord.presentationedit.util.PresentationEditConsts;
import ru.pp.gamma.overlord.presentationedit.ws.message.EditImageMessage;
import ru.pp.gamma.overlord.presentationedit.ws.message.ImageUpdatedMessage;
import ru.pp.gamma.overlord.presentationedit.ws.message.common.PresentationEditBaseMessage;
import ru.pp.gamma.overlord.presentationedit.ws.message.common.PresentationEditMessageType;

@RequiredArgsConstructor
@Component
public class EditImageHandler implements PresentationEditMessageHandler {

    private final ImageService imageService;
    private final ImageMapper imageMapper;
    private final PresentationSlideFieldService presentationSlideFieldService;
    private final EditPresentationRedisNotifier editPresentationRedisNotifier;

    @Override
    public boolean canHandle(PresentationEditBaseMessage presentationEditBaseMessage) {
        return PresentationEditMessageType.EDIT_IMAGE.equals(presentationEditBaseMessage.getType());
    }

    @Transactional
    @Override
    public void handle(WebSocketSession session, PresentationEditBaseMessage baseMessage) {
        EditImageMessage message = (EditImageMessage) baseMessage;
        Image newImage = imageService.getById(message.getImageId());

        SlideField slideField = presentationSlideFieldService.getById(message.getFieldId());
        Image oldImage = slideField.getImage();

        imageService.markAsDeleted(oldImage);
        slideField.setImage(newImage);
        presentationSlideFieldService.save(slideField);

        ImageUpdatedMessage responseMessage = new ImageUpdatedMessage()
                .setFieldId(slideField.getId())
                .setImageId(newImage.getId())
                .setUrl(imageMapper.toImageUrl(newImage));

        editPresentationRedisNotifier.byPresentationId((long) session.getAttributes().get(PresentationEditConsts.PRESENTATION_ID_ATTRIBUTE), responseMessage);
    }
}
