package ru.pp.gamma.overlord.presentation.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.pp.gamma.overlord.presentation.entity.Presentation;
import ru.pp.gamma.overlord.presentation.repository.PresentationRepository;
import ru.pp.gamma.overlord.presentationpreview.service.PresentationPreviewSender;

@RequiredArgsConstructor
@Service
public class PresentationService {

    private final PresentationRepository presentationRepository;
    private final PresentationPreviewSender presentationPreviewSender;

    public Presentation getById(long id) {
        return presentationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Presentation not found"));
    }

    public void save(Presentation presentation) {
        presentationRepository.save(presentation);
        presentationPreviewSender.sendPresentationCreatedOrUpdated(presentation.getId());
    }
}
