package ru.pp.gamma.overlord.presentationplan.mapper;

import org.springframework.stereotype.Component;
import ru.pp.gamma.overlord.presentationplan.dto.ai.AiPresentationPlanElementDto;
import ru.pp.gamma.overlord.presentationplan.entity.PlanElementItem;
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
        PresentationPlanElement element = new PresentationPlanElement();
        element.setTitle(dto.title());
        element.setPresentationPlan(plan);

        List<PlanElementItem> items = dto.points().stream()
                .map(point -> mapToPlanElementItem(point, element))
                .toList();

        element.setItems(items);
        return element;
    }

    private PlanElementItem mapToPlanElementItem(String text, PresentationPlanElement element) {
        PlanElementItem item = new PlanElementItem();
        item.setText(text);
        item.setPlanElement(element);
        return item;
    }

}
