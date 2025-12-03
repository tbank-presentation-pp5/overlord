package ru.pp.gamma.overlord.presentationplan.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.pp.gamma.overlord.presentationplan.entity.PresentationPlan;
import ru.pp.gamma.overlord.presentationplan.entity.PresentationPlanElement;
import ru.pp.gamma.overlord.presentationplan.repository.PresentationPlanRepository;

import java.util.List;

@RequiredArgsConstructor
@Service
public class PresentationPlanService {

    private final PresentationPlanRepository presentationPlanRepository;

    public void save(PresentationPlan plan) {
        presentationPlanRepository.save(plan);
    }

    public PresentationPlan getById(long id) {
        return presentationPlanRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Plan not found"));
    }

    public void update(PresentationPlan plan, List<PresentationPlanElement> elements) {
        plan.setElements(elements);
        save(plan);
    }
}
