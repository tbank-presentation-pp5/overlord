package ru.pp.gamma.overlord.presentationplan.mapper;

import org.springframework.stereotype.Component;
import ru.pp.gamma.overlord.presentationplan.dto.ai.AiPresentationPlanElementDto;
import ru.pp.gamma.overlord.presentationplan.entity.PresentationPlan;
import ru.pp.gamma.overlord.presentationplan.entity.PresentationPlanElement;

import java.util.List;

@Component
public class AiPresentationPlanMapper {

    public PresentationPlan map(List<AiPresentationPlanElementDto> dto, String description, int countSlides) {
        PresentationPlan plan = new PresentationPlan();
        plan.setShortDescription(description);
        plan.setSlidesCount(countSlides);

        List<PresentationPlanElement> elements = dto.stream()
                .map(aiElement -> mapToPresentationPlanElement(aiElement, plan))
                .toList();

        plan.setElements(elements);
        return plan;
    }

    private PresentationPlanElement mapToPresentationPlanElement(
            AiPresentationPlanElementDto dto,
            PresentationPlan plan
    ) {
        return new PresentationPlanElement(
                dto.title(),
                dto.points()
        );
    }

}
