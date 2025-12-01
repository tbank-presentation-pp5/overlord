package ru.pp.gamma.overlord.generation.prompt;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.pp.gamma.overlord.presentationplan.entity.PresentationPlan;
import ru.pp.gamma.overlord.presentationplan.service.PresentationPlanService;

@RequiredArgsConstructor
@Component
public class UserPromptBuilder {

    private final PresentationPlanService presentationPlanService;

    public String buildNoteSource(String note, int slidesCount) {
        return """
                Разбей следующий текст на слайды и верни результат строго в формате JSON: %s.
                Cлайдов должно быть РОВНО=%d"""
                .formatted(note, slidesCount);
    }

    public String buildPlanPrompt(long planId) {
        String template = """
                Составь презентацию на основе плана. Опирайся на план, следуй ему.
                Не нужно слово в слово переписывать, это просто набросок, в каком направлении мыслить.
                Абсолютно все темы должны быть отражены. Заголовок темы - это один слайд.
                
                ЕСЛИ ТЕБЕ НЕ ХВАТАЕТ СЛАЙДОВ, ТО МОЖЕШЬ ПРЕВЫСИТЬ ЛИМИТ
                План: %s""";

        PresentationPlan plan = presentationPlanService.getById(planId);
        StringBuilder sb = new StringBuilder("\n");
        plan.getElements().forEach(element -> {
            sb
                    .append("* ")
                    .append(element.getTitle())
                    .append("\n");

            element.getItems()
                    .forEach(item -> sb.append("  - ")
                            .append(item.getText())
                            .append("\n")
                    );

        });

        return String.format(template, sb);
    }

}
