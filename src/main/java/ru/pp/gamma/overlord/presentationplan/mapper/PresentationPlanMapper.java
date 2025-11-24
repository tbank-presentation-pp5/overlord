package ru.pp.gamma.overlord.presentationplan.mapper;

import org.springframework.stereotype.Component;
import ru.pp.gamma.overlord.presentationplan.dto.GeneratePresentationPlanResponseDto;
import ru.pp.gamma.overlord.presentationplan.dto.GenerationPresentationElementDto;
import ru.pp.gamma.overlord.presentationplan.entity.PlanElementItem;
import ru.pp.gamma.overlord.presentationplan.entity.PresentationPlan;

@Component
public class PresentationPlanMapper {

    public GeneratePresentationPlanResponseDto mapToDto(PresentationPlan plan) {
        return new GeneratePresentationPlanResponseDto(
                plan.getId(),
                plan.getShortDescription(),
                plan.getSlidesCount(),
                plan.getElements().stream()
                        .map(element -> new GenerationPresentationElementDto(
                                        element.getTitle(),
                                        element.getItems().stream()
                                                .map(PlanElementItem::getText)
                                                .toList()
                                )
                        )
                        .toList()
        );
    }

}
