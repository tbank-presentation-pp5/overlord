package ru.pp.gamma.overlord.presentation.template.repository;

import org.springframework.data.repository.CrudRepository;
import ru.pp.gamma.overlord.presentation.template.entity.TemplatePresentation;

public interface TemplatePresentationRepository extends CrudRepository<TemplatePresentation, Long> {
}
