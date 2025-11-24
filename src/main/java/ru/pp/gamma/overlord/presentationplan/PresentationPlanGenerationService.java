package ru.pp.gamma.overlord.presentationplan;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.pp.gamma.overlord.ai.api.AiTextClient;
import ru.pp.gamma.overlord.presentationplan.dto.ai.AiPresentationPlanElementDto;
import ru.pp.gamma.overlord.presentationplan.entity.PresentationPlan;
import ru.pp.gamma.overlord.presentationplan.mapper.AiPresentationPlanMapper;

import java.util.List;

@RequiredArgsConstructor
@Service
public class PresentationPlanGenerationService {

    private static final String SYSTEM_PROMPT = """
            Ты — эксперт по созданию презентаций.
            Пользователь даёт:
            1. Тему презентации (одно-два слова или короткая фраза)
            2. Максимальное количество слайдов (целое число ≥ 1)
            
            Требования:
            - НЕ используй личные истории, личные воспоминания, эмоции, субъективные мнения или художественные описания.
            - НЕ генерируй подпункты из одного слова. Каждый пункт — это короткое, информативное предложение длиной 4–12 слов.
            - Пункты должны содержать конкретные идеи, факты или аспекты темы.
            - Стиль нейтральный, деловой, без выдуманных персонажей и художественных образов.
            - Если слайд является титульным, оставь список пунктов пустым.
            
            Твоя задача — составить подробный план презентации по этой теме, учитывая заданное количество слайдов.
            Для каждого слайда нужно:
            - Заголовок слайда
            - 0–3 подпунктов с ключевой информацией (если пункты не требуются, то не указывай. Например, титульный слайд)
            
            Если материала слишком много для одного слайда, раздели его логично на несколько слайдов.
            Сохраняй краткость, ясность и логическую структуру.
            
            Ответь в формате JSON, выводи JSON без обёртки:
            [{"title": "Заголовок слайда", "points": ["подпункт 1", ...]}, ...]
            """;

    private static final String USER_PROMPT = """
            Тема презентации: %s
            Количество слайдов: %d
            """;

    private final AiTextClient aiTextClient;
    private final ObjectMapper objectMapper;
    private final AiPresentationPlanMapper aiPresentationPlanMapper;


    public PresentationPlan generate(String description, int countSlides) {
        String response = aiTextClient.generate(SYSTEM_PROMPT, getUserPrompt(description, countSlides));
        List<AiPresentationPlanElementDto> parsed = parseResponse(response);
        return aiPresentationPlanMapper.map(parsed, description, countSlides);
    }

    private String getUserPrompt(String description, int slidesCount) {
        return String.format(USER_PROMPT, description, slidesCount);
    }

    private List<AiPresentationPlanElementDto> parseResponse(String response) {
        try {
            return objectMapper.readValue(
                    response,
                    new TypeReference<>() {
                    }
            );
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
