package ru.pp.gamma.overlord.presentationplan;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.pp.gamma.overlord.ai.api.AiTextClient;
import ru.pp.gamma.overlord.ai.model.AiModel;
import ru.pp.gamma.overlord.presentationplan.dto.ai.AiPresentationPlanElementDto;
import ru.pp.gamma.overlord.presentationplan.entity.PresentationPlan;
import ru.pp.gamma.overlord.presentationplan.mapper.AiPresentationPlanMapper;

import java.util.List;
import java.util.regex.Pattern;

@Slf4j
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
            - 0–3 подпунктов с ключевой информацией (если пункты не требуются, то не указывай)
            
            Если материала слишком много для одного слайда, раздели его логично на несколько слайдов.
            Сохраняй краткость, ясность и логическую структуру.
            
            ВАЖНО: выведи ТОЛЬКО валидный JSON-массив, без какого-либо текста до или после него,
            без markdown-блоков (без ```), без экранирования фигурных скобок.
            Формат:
            [{"title": "Заголовок слайда", "points": ["подпункт 1", ...]}, ...]
            """;

    private static final String USER_PROMPT = """
            Тема презентации: %s
            Количество слайдов: %d
            """;

    private static final AiModel DEFAULT_MODEL = AiModel.CF_MISTRAL_SMALL;

    private static final Pattern ESCAPED_OBJECT_SEPARATOR =
            Pattern.compile("}\\s*]\\s*,\\s*\"\\{\"");

    private final AiTextClient aiTextClient;
    private final ObjectMapper objectMapper;
    private final AiPresentationPlanMapper aiPresentationPlanMapper;

    public PresentationPlan generate(String description, int countSlides, AiModel model) {
        String response = aiTextClient.generate(SYSTEM_PROMPT, getUserPrompt(description, countSlides), model);
        List<AiPresentationPlanElementDto> parsed = parseResponse(response, model);
        return aiPresentationPlanMapper.map(parsed, description, countSlides);
    }

    public PresentationPlan generate(String description, int countSlides) {
        return generate(description, countSlides, DEFAULT_MODEL);
    }

    private String getUserPrompt(String description, int slidesCount) {
        return String.format(USER_PROMPT, description, slidesCount);
    }

    private List<AiPresentationPlanElementDto> parseResponse(String raw, AiModel model) {
        String json = extractJson(raw, model);
        json = repairJson(json, model);
        try {
            return objectMapper.readValue(json, new TypeReference<>() {
            });
        } catch (JsonProcessingException e) {
            throw new RuntimeException(
                    "Failed to parse presentation plan from model [%s]. Raw: %s"
                            .formatted(model.getModelId(), raw), e);
        }
    }

    private String extractJson(String raw, AiModel model) {
        if (raw == null || raw.isBlank()) {
            throw new RuntimeException("Empty response from model: " + model.getModelId());
        }

        int start = raw.indexOf('[');
        if (start == -1) {
            throw new RuntimeException(
                    "No JSON array found in response from model [%s]. Raw: %s"
                            .formatted(model.getModelId(), raw));
        }

        String json = raw.substring(start).stripTrailing();

        if (json.endsWith("```")) {
            json = json.substring(0, json.lastIndexOf("```")).stripTrailing();
        }

        if (start > 0) {
            log.warn("Model [{}]: stripped {} chars of non-JSON prefix.", model.getModelId(), start);
        }

        return json;
    }

    private String repairJson(String json, AiModel model) {
        // комментарии для себя оставлю потом уберу
        // баг: }],"{"  =>  },{" (объект ошибочно завёрнут в строку)
        if (ESCAPED_OBJECT_SEPARATOR.matcher(json).find()) {
            log.warn("Model [{}]: repairing escaped object separators in JSON.", model.getModelId());
            // разбираем весь массив: убираем ],"{ паттерн
            // строка вида: [...}],"{"title":...}],"{"title":...}]
            // после fix:   [...},{"title":...},{"title":...}]
            json = json
                    // }],"{"  →  },{"
                    .replaceAll("}\\s*]\\s*,\\s*\"\\{\"", "},{\"")
                    // убираем лишние закрывающие ]  перед ,{ которые остались
                    // (на случай если паттерн повторяется)
                    .replaceAll("}\\s*,\\s*\"\\{\"", "},{\"");

            // убеждаемся, что массив закрыт ровно одним ]
            // считаем баланс скобок и добавляем закрывающую если нужно
            json = balanceBrackets(json);
        }

        return json;
    }

    private String balanceBrackets(String json) {
        int depth = 0;
        for (char c : json.toCharArray()) {
            if (c == '[') depth++;
            else if (c == ']') depth--;
        }
        if (depth > 0) {
            log.warn("JSON is missing {} closing bracket(s), appending.", depth);
            json = json + "]".repeat(depth);
        }
        return json;
    }
}