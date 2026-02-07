package ru.pp.gamma.overlord.presentation.repository;

import org.springframework.data.repository.CrudRepository;
import ru.pp.gamma.overlord.presentation.entity.PresentationSlide;

public interface PresentationSlideRepository extends CrudRepository<PresentationSlide, Long> {
}
