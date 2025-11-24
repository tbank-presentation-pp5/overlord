package ru.pp.gamma.overlord.presentationplan.repository;

import org.springframework.data.repository.CrudRepository;
import ru.pp.gamma.overlord.presentationplan.entity.PresentationPlan;

public interface PresentationPlanRepository extends CrudRepository<PresentationPlan, Long> {
}
