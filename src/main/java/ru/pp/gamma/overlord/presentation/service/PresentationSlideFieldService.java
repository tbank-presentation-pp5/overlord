package ru.pp.gamma.overlord.presentation.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.pp.gamma.overlord.presentation.entity.SlideField;
import ru.pp.gamma.overlord.presentation.repository.PresentationSlideFieldRepository;

@RequiredArgsConstructor
@Service
public class PresentationSlideFieldService {

    private final PresentationSlideFieldRepository presentationSlideFieldRepository;
    private final ObjectMapper objectMapper;

    public void updateTextValue(long fieldId, String text) {
        SlideField field = presentationSlideFieldRepository.findById(fieldId).orElseThrow(() -> new RuntimeException("Not found slide field"));
        field.setValue(objectMapper.valueToTree(text));
        presentationSlideFieldRepository.save(field);
    }

}
