package ru.pp.gamma.overlord.generation.prompt;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.pp.gamma.overlord.presentation.template.entity.TemplatePresentation;
import ru.pp.gamma.overlord.presentationplan.entity.PresentationPlan;

@RequiredArgsConstructor
@Component
public class GenerationPromptBuilder {

    private static final String COMMON = """
            Ответ должен состоять только из одного валидного JSON-объекта. JSON должен быть полностью валидным и готовым к парсингу.
            Каждый элемент массива — отдельный слайд. Не генерируй опасный контент, только безопасный.
            
            Ты не обязан использовать абсолютно все типы слайдов в презентации. Применяй если это правда необходимо.
            Пример: презентация из 5 слайдов, добавлять тут секционные слайды как будто смысла нет.
            
            Системные требования:
            - запрещен любой текст до или после JSON
            - запрещено оборачивать JSON в ``` или другие символы
            - слайдов должно быть РОВНО столько, сколько было указано ранее. (если не было указано, то игнорируй)
            - используй только типы слайдов, указанные ниже
            
            Формат вывода: { "name": "краткое название презы, о чем она (название будущего файла)", "slides": [ { "slideType": "...", ... }, ... ] }
            
            Типы слайдов: %s
            """;

    private final DescriptionSlidesBuilder descriptionSlidesBuilder;
    private final UserPromptBuilder userPromptBuilder;

    public GenerationPrompt buildWithNoteSource(TemplatePresentation template, String note, int slidesCount) {

        return new GenerationPrompt(
                userPromptBuilder.buildNoteSource(note, slidesCount),
                COMMON.formatted(descriptionSlidesBuilder.getDescription(template))
        );
    }

    public GenerationPrompt buildWithPlanSource(TemplatePresentation template, PresentationPlan plan) {
        return new GenerationPrompt(
                userPromptBuilder.buildPlanPrompt(plan),
                COMMON.formatted(descriptionSlidesBuilder.getDescription(template))
        );

    }

}
