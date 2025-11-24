package ru.pp.gamma.overlord.presentationplan.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.pp.gamma.overlord.presentationplan.entity.PresentationPlan;
import ru.pp.gamma.overlord.presentationplan.repository.PresentationPlanRepository;

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
}
