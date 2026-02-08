package ru.pp.gamma.overlord.presentation.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.pp.gamma.overlord.presentation.entity.Presentation;

public interface PresentationRepository extends JpaRepository<Presentation, Long> {
}
