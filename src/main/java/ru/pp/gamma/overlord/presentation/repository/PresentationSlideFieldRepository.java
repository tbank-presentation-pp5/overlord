package ru.pp.gamma.overlord.presentation.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.pp.gamma.overlord.presentation.entity.SlideField;

public interface PresentationSlideFieldRepository extends JpaRepository<SlideField, Long> {
}
