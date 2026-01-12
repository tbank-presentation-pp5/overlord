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
            
            Генерируй промпты для изображений животных так, чтобы они были **полностью безопасны для всех возрастов** и проходили NSFW фильтр.
            Правила:
            1. **Никаких людей** и взаимодействия с ними (нет «owner», «breeder», «being bathed», «mating» и т.д.).
            2. **Никаких деталей тела или анатомии**, особенно близких к человеку (нет «skin», «body», «soft», «muscular», «close-up», «looking at the camera», «hairless», и подобных).
            3. Опасайся опасных сочетаний типа: «sphynx cat sitting in a cozy room», «sphynx cat resting on a soft blanket»,
            4. **Фокус только на нейтральной визуальной информации**:
               - поза животного: «sitting», «standing», «resting», «playing», «exploring»
               - окружение: «in a sunny room», «on a blanket», «in a garden»
               - эмоции через позу и контекст: «curious», «happy», «relaxed», «playful»
               - внешний вид: «striped», «fluffy» и т.п. (без «skin», «soft» и текстурных сравнений к телу человека)
            5. **Не использовать никакие слова, которые могут быть истолкованы как интимные, сексуализированные или про человеческое тело.**
            
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
