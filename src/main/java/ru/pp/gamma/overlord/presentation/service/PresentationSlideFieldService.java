package ru.pp.gamma.overlord.presentation.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.pp.gamma.overlord.image.entity.Image;
import ru.pp.gamma.overlord.image.service.ImageService;
import ru.pp.gamma.overlord.presentation.entity.SlideField;
import ru.pp.gamma.overlord.presentation.repository.PresentationSlideFieldRepository;

@RequiredArgsConstructor
@Service
public class PresentationSlideFieldService {

    private final PresentationSlideFieldRepository presentationSlideFieldRepository;
    private final ImageService imageService;
    private final ObjectMapper objectMapper;

    public void updateTextValue(long fieldId, String text) {
        SlideField field = presentationSlideFieldRepository.findById(fieldId).orElseThrow(() -> new RuntimeException("Not found slide field"));
        field.setValue(objectMapper.valueToTree(text));
        presentationSlideFieldRepository.save(field);
    }

    public SlideField getById(long fieldId) {
        return presentationSlideFieldRepository.findById(fieldId)
                .orElseThrow(() -> new RuntimeException("Not found slide field"));
    }

    @Transactional
    public void swapImage(long fieldId, long newImageId) {
        SlideField slideField = getById(fieldId);

        Image oldImage = slideField.getImage();
        imageService.markAsDeleted(oldImage);

        slideField.setImage(imageService.getById(newImageId));
        save(slideField);
    }

    public void save(SlideField slideField) {
        presentationSlideFieldRepository.save(slideField);
    }
}
