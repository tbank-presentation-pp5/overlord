package ru.pp.gamma.overlord.presentationplan;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.pp.gamma.overlord.presentationplan.dto.GeneratePresentationPlanRequestDto;
import ru.pp.gamma.overlord.presentationplan.dto.PresentationPlanResponseDto;
import ru.pp.gamma.overlord.presentationplan.dto.UpdatePresentationPlanRequestDto;
import ru.pp.gamma.overlord.presentationplan.entity.PresentationPlan;
import ru.pp.gamma.overlord.presentationplan.mapper.PresentationPlanMapper;
import ru.pp.gamma.overlord.presentationplan.service.PresentationPlanService;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/presentation-plans/")
public class PresentationPlanController {

    private final PresentationPlanGenerationService presentationPlanGenerationService;
    private final PresentationPlanService presentationPlanService;
    private final PresentationPlanMapper presentationPlanMapper;

    @PostMapping("/generate")
    public PresentationPlanResponseDto generate(@RequestBody GeneratePresentationPlanRequestDto request) {
        PresentationPlan plan = presentationPlanGenerationService
                .generate(request.shortDescription(), request.numberOfSlides());
        presentationPlanService.save(plan);
        return presentationPlanMapper.mapToDto(plan);
    }

    @GetMapping("/{id}")
    public PresentationPlanResponseDto getById(@PathVariable long id) {
        PresentationPlan plan = presentationPlanService.getById(id);
        return presentationPlanMapper.mapToDto(plan);
    }

    @PutMapping("/{id}")
    public PresentationPlanResponseDto update(
            @PathVariable long id,
            @RequestBody UpdatePresentationPlanRequestDto request
    ) {
        PresentationPlan plan = presentationPlanService.getById(id);
        presentationPlanService.update(plan, presentationPlanMapper.toListElements(request));
        return presentationPlanMapper.mapToDto(plan);
    }

}
