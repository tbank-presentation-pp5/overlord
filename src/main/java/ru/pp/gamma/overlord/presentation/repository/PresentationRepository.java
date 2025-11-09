package ru.pp.gamma.overlord.presentation.repository;

import org.springframework.data.repository.CrudRepository;
import ru.pp.gamma.overlord.presentation.entity.Presentation;

public interface PresentationRepository extends CrudRepository<Presentation, Long> {
}
