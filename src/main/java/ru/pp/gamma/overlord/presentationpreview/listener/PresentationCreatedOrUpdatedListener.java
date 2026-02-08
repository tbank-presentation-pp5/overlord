package ru.pp.gamma.overlord.presentationpreview.listener;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.support.TransactionTemplate;
import ru.pp.gamma.overlord.export.image.ImageExportService;
import ru.pp.gamma.overlord.image.entity.Image;
import ru.pp.gamma.overlord.image.repository.ImageRepository;
import ru.pp.gamma.overlord.image.service.ImageService;
import ru.pp.gamma.overlord.kafka.message.PresentationSavedOrUpdatedMessage;
import ru.pp.gamma.overlord.presentation.entity.Presentation;
import ru.pp.gamma.overlord.presentation.entity.PresentationSlide;
import ru.pp.gamma.overlord.presentation.repository.PresentationRepository;
import ru.pp.gamma.overlord.presentation.repository.PresentationSlideRepository;

import java.util.List;
import java.util.Optional;

import static ru.pp.gamma.overlord.kafka.KafkaTopic.PRESENTATION_SAVED_OR_UPDATED;

@Slf4j
@RequiredArgsConstructor
@Component
public class PresentationCreatedOrUpdatedListener {

    private final static double SCALE = 0.5;

    private final PresentationRepository presentationRepository;
    private final ImageRepository imageRepository;
    private final PresentationSlideRepository presentationSlideRepository;
    private final ImageExportService imageExportService;
    private final ImageService imageService;
    private final TransactionTemplate transactionTemplate;

    @KafkaListener(topics = PRESENTATION_SAVED_OR_UPDATED)
    public void consume(PresentationSavedOrUpdatedMessage message) {
        presentationRepository.findById(message.presentationId())
                .ifPresentOrElse(
                        this::processMessage,
                        () -> log.info("Presentation not found with id '{}', skip message", message.presentationId())
                );
    }

    private void processMessage(Presentation presentation) {
        List<byte[]> images = imageExportService.export(presentation, SCALE); // 1. Получаем изображения
        if (!validate(presentation, images)) {
            return;
        }

        // 2. Сохраняем в minio
        List<Image> imageEntities = images.stream()
                .map(imageService::savePng)
                .map(name -> new Image()
                        .setName(name))
                .toList();

        // 3. В транзакции меняем preview
        transactionTemplate.executeWithoutResult(status -> {
            imageRepository.saveAll(imageEntities);
            for (int i = 0; i < imageEntities.size(); i++) {
                replacePreview(presentation.getSlides().get(i), imageEntities.get(i));
            }
        });
    }

    private void replacePreview(PresentationSlide slide, Image image) {
        Optional.ofNullable(slide.getPreview())
                .ifPresent(p -> {
                    p.setIsDeleted(true);
                    imageRepository.save(p);
                });
        slide.setPreview(image);
        presentationSlideRepository.save(slide);
    }

    private boolean validate(Presentation presentation, List<byte[]> images) {
        int imageCount = images.size();
        int slidesCount = presentation.getSlides().size();
        if (imageCount != slidesCount) {
            log.error("Count of slides and images not same {}!={}, slip processing", slidesCount, imageCount);
            return false;
        }

        return true;
    }
}
