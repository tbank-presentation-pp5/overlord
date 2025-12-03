package ru.pp.gamma.overlord.presentationplan.mapper;

import org.springframework.stereotype.Component;
import ru.pp.gamma.overlord.presentationplan.dto.PresentationElementDto;
import ru.pp.gamma.overlord.presentationplan.dto.PresentationPlanResponseDto;
import ru.pp.gamma.overlord.presentationplan.dto.UpdatePresentationPlanRequestDto;
import ru.pp.gamma.overlord.presentationplan.entity.PresentationPlan;
import ru.pp.gamma.overlord.presentationplan.entity.PresentationPlanElement;

import java.util.List;

@Component
public class PresentationPlanMapper {

    public PresentationPlanResponseDto mapToDto(PresentationPlan plan) {
        return new PresentationPlanResponseDto(
                plan.getId(),
                plan.getShortDescription(),
                plan.getSlidesCount(),
                plan.getElements().stream()
                        .map(element -> new PresentationElementDto(
                                        element.title(),
                                        element.points()
                                )
                        )
                        .toList()
        );
    }

    public List<PresentationPlanElement> toListElements(UpdatePresentationPlanRequestDto dto) {
        return dto.plan().stream()
                .map(e -> new PresentationPlanElement(
                        e.title(),
                        e.points()
                ))
                .toList();
    }

}
