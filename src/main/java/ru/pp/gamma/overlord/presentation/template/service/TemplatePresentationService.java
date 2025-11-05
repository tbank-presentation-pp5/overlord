package ru.pp.gamma.overlord.presentation.template.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.pp.gamma.overlord.exception.WebException;
import ru.pp.gamma.overlord.presentation.template.entity.TemplatePresentation;
import ru.pp.gamma.overlord.presentation.template.repository.TemplatePresentationRepository;

@RequiredArgsConstructor
@Service
public class TemplatePresentationService {

    private final TemplatePresentationRepository templatePresentationRepository;

    @Transactional(readOnly = true)
    public TemplatePresentation getById(long id) {
        return templatePresentationRepository.findById(id)
                .orElseThrow(() -> new WebException(HttpStatus.NOT_FOUND, "Шаблон презентации с id=%d не найден", id));
    }
}
