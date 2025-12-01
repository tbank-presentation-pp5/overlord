package ru.pp.gamma.overlord.generation.prompt;

import org.springframework.stereotype.Component;
import ru.pp.gamma.overlord.presentationplan.entity.PresentationPlan;

@Component
public class UserPromptBuilder {

    public String buildNoteSource(String note, int slidesCount) {
        return """
                Разбей следующий текст на слайды и верни результат строго в формате JSON: %s.
                
                Требования:
                - Cлайдов должно быть РОВНО=%d
                """
                .formatted(note, slidesCount);
    }

    public String buildPlanPrompt(PresentationPlan plan) {
        String template = """
                Составь презентацию по плану. План - это список элементов, каждый элемент равен одному слайду.
                Каждый элемент имеет заголовок - это будущий заголовок слайда. В элементе есть подсписок, это набросок
                идей того, что может быть на слайде.
                Не нужно дословно переписывать текст на слайд. Сделай саммари, перераскажи со своими дополнениями.
                Необязательно отразить в содержании все пункты подсписка, только основное, остальное можешь не рассказывать.
                
                Важные правила:
                1. Не переписывай текст дословно. Используй идеи как подсказку и делай краткое саммари своими словами.
                2. Для каждого слайда укажи только основные идеи, не обязательно включать все пункты.
                3. Добавляй только содержательные дополнения, которые улучшают понимание темы, без выдуманных фактов.
                4. Используй формулировки, понятные для презентации: короткие, простые, легко читаемые.
                
                Соответствие количеству слайдов = количеству пунктов в плане НЕ НУЖНО.
                Можешь саму структру презентации как угодно делать. (Соблюдай порядок)
                Например, использовать секционные слайды, если они действительно нужны
                План: %s
                """;

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
