package ru.pp.gamma.overlord.presentationedit.handler;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketSession;
import ru.pp.gamma.overlord.common.util.RegexUtil;
import ru.pp.gamma.overlord.externalapi.pexels.client.PexelsClient;
import ru.pp.gamma.overlord.externalapi.pexels.dto.PexelsPhoto;
import ru.pp.gamma.overlord.externalapi.util.FileDownloader;
import ru.pp.gamma.overlord.image.entity.Image;
import ru.pp.gamma.overlord.image.enums.ImageFormat;
import ru.pp.gamma.overlord.image.mapper.ImageMapper;
import ru.pp.gamma.overlord.image.service.ImageService;
import ru.pp.gamma.overlord.presentation.service.PresentationSlideFieldService;
import ru.pp.gamma.overlord.presentationedit.redis.producer.EditPresentationRedisNotifier;
import ru.pp.gamma.overlord.presentationedit.util.PresentationEditConsts;
import ru.pp.gamma.overlord.presentationedit.ws.message.EditImageByPexelsMessage;
import ru.pp.gamma.overlord.presentationedit.ws.message.ImageUpdatedMessage;
import ru.pp.gamma.overlord.presentationedit.ws.message.common.PresentationEditBaseMessage;
import ru.pp.gamma.overlord.presentationedit.ws.message.common.PresentationEditMessageType;

import java.util.Optional;

@RequiredArgsConstructor
@Component
public class EditImageByPexelsHandler implements PresentationEditMessageHandler {

    private final PexelsClient pexelsClient;
    private final FileDownloader fileDownloader;
    private final ImageService imageService;
    private final PresentationSlideFieldService presentationSlideFieldService;
    private final ImageMapper imageMapper;
    private final EditPresentationRedisNotifier editPresentationRedisNotifier;


    @Override
    public boolean canHandle(PresentationEditBaseMessage presentationEditBaseMessage) {
        return PresentationEditMessageType.EDIT_IMAGE_BY_PEXELS.equals(presentationEditBaseMessage.getType());
    }

    @Override
    public void handle(WebSocketSession session, PresentationEditBaseMessage baseMessage) {
        EditImageByPexelsMessage message = (EditImageByPexelsMessage) baseMessage;

        PexelsPhoto pexelsPhoto = pexelsClient.getById(message.getPexelsImageId());
        Optional<String> extension = RegexUtil.resolveFileExtensionFromUrl(pexelsPhoto.src().large2x());
        if (extension.isEmpty()) {
            throw new RuntimeException("Extension pexels image is null in EditImageByPexelsHandler");
        }
        Optional<ImageFormat> imageFormat = ImageFormat.fromExtension(extension.get());
        if (imageFormat.isEmpty()) {
            throw new RuntimeException("Image format '%s' unsupported EditImageByPexelsHandler".formatted(extension.get()));
        }

        byte[] imageData = fileDownloader.download(pexelsPhoto.src().large2x());
        Image image = imageService.uploadImage(imageData, imageFormat.get());

        presentationSlideFieldService.swapImage(message.getFieldId(), image.getId());

        ImageUpdatedMessage responseMessage = new ImageUpdatedMessage()
                .setFieldId(message.getFieldId())
                .setImageId(image.getId())
                .setUrl(imageMapper.toImageUrl(image));

        editPresentationRedisNotifier.byPresentationId((long) session.getAttributes().get(PresentationEditConsts.PRESENTATION_ID_ATTRIBUTE), responseMessage);
    }

}
