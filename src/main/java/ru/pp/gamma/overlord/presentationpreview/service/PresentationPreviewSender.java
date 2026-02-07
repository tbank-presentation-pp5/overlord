package ru.pp.gamma.overlord.presentationpreview.service;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import ru.pp.gamma.overlord.kafka.message.PresentationSavedOrUpdatedMessage;

import static ru.pp.gamma.overlord.kafka.KafkaTopic.PRESENTATION_SAVED_OR_UPDATED;

@RequiredArgsConstructor
@Component
public class PresentationPreviewSender {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void sendPresentationCreatedOrUpdated(long presentationId) {
        PresentationSavedOrUpdatedMessage message = new PresentationSavedOrUpdatedMessage(presentationId);
        kafkaTemplate.send(PRESENTATION_SAVED_OR_UPDATED, message);
    }
}
