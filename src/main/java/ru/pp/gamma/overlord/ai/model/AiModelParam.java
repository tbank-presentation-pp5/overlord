package ru.pp.gamma.overlord.ai.model;

import lombok.Getter;

import java.util.List;

@Getter
public enum AiModelParam {

    // Стандартные параметры генерации (OpenAI API и аналоги)
    TEMPERATURE(
            "temperature",
            "Температура выборки от 0 до 2. Выше — креативнее, ниже — детерминированнее.",
            ParameterType.NUMBER,
            1.0,
            0.0, 2.0
    ),
    TOP_P(
            "top_p",
            "Nucleus sampling: рассматриваются токены с суммарной вероятностью top_p.",
            ParameterType.NUMBER,
            1.0,
            0.0, 1.0
    ),
    TOP_K(
            "top_k",
            "Ограничивает выбор top_k наиболее вероятными токенами (нестандартный параметр для некоторых моделей).",
            ParameterType.INTEGER,
            null,
            1.0, 50.0
    ),
    REPETITION_PENALTY(
            "repetition_penalty",
            "Штраф за повторение токенов. 1.0 — нет штрафа, выше — меньше повторений (нестандартный).",
            ParameterType.NUMBER,
            null,
            1.0, 2.0
    ),
    SEED(
            "seed",
            "Зерно генератора для детерминированной выборки (насколько это возможно).",
            ParameterType.INTEGER,
            null,
            null, null
    ),
    MAX_TOKENS(
            "max_tokens",
            "Максимальное количество токенов в ответе (deprecated, используйте MAX_COMPLETION_TOKENS).",
            ParameterType.INTEGER,
            null,
            null, null
    ),
    MAX_COMPLETION_TOKENS(
            "max_completion_tokens",
            "Верхняя граница количества токенов для генерации.",
            ParameterType.INTEGER,
            null,
            null, null
    ),
    FREQUENCY_PENALTY(
            "frequency_penalty",
            "Штрафует новые токены на основе их частоты в уже сгенерированном тексте.",
            ParameterType.NUMBER,
            0.0,
            -2.0, 2.0
    ),
    PRESENCE_PENALTY(
            "presence_penalty",
            "Штрафует новые токены за появление в тексте ранее (увеличивает разнообразие тем).",
            ParameterType.NUMBER,
            0.0,
            -2.0, 2.0
    ),
    N(
            "n",
            "Количество вариантов ответа для генерации на каждый входной запрос.",
            ParameterType.INTEGER,
            1,
            1.0, 128.0
    ),
    LOGIT_BIAS(
            "logit_bias",
            "Изменяет вероятность указанных токенов. Маппинг token_id → bias (-100..100).",
            ParameterType.OBJECT,
            null,
            null, null
    ),
    LOGPROBS(
            "logprobs",
            "Возвращать ли логарифмические вероятности выходных токенов.",
            ParameterType.BOOLEAN,
            false,
            null, null
    ),
    TOP_LOGPROBS(
            "top_logprobs",
            "Количество top логарифмических вероятностей на каждую позицию токена (0-20). Требует logprobs=true.",
            ParameterType.INTEGER,
            null,
            0.0, 20.0
    ),
    STOP(
            "stop",
            "Одна или несколько (до 4) последовательностей, при которых генерация остановится.",
            ParameterType.ARRAY_OR_STRING,
            null,
            null, null
    ),
    RESPONSE_FORMAT(
            "response_format",
            "Формат ответа: text, json_object или json_schema со схемой.",
            ParameterType.OBJECT,
            null,
            null, null
    ),
    SERVICE_TIER(
            "service_tier",
            "Тип обработки запроса.",
            ParameterType.STRING,
            "auto",
            null, null,
            List.of("auto", "default", "flex", "scale", "priority")
    ),
    STORE(
            "store",
            "Сохранять ли выходные данные для дистилляции / оценки.",
            ParameterType.BOOLEAN,
            false,
            null, null
    ),
    USER(
            "user",
            "Уникальный идентификатор конечного пользователя для мониторинга злоупотреблений.",
            ParameterType.STRING,
            null,
            null, null
    ),

    // Параметры, связанные с мультимодальностью и аудио
    MODALITIES(
            "modalities",
            "Запрашиваемые типы выходных данных: ['text'], ['audio'] или ['text','audio'].",
            ParameterType.ARRAY,
            null,
            null, null
    ),
    AUDIO(
            "audio",
            "Параметры аудиовыхода (voice и format). Обязателен, если modalities включает 'audio'.",
            ParameterType.OBJECT,
            null,
            null, null
    ),

    // Параметры для инструментов и функций
    TOOLS(
            "tools",
            "Список инструментов (функции и custom), которые модель может вызывать.",
            ParameterType.ARRAY,
            null,
            null, null
    ),
    TOOL_CHOICE(
            "tool_choice",
            "Контроль вызова инструментов: 'none', 'auto', 'required', либо конкретная функция/custom инструмент.",
            ParameterType.OBJECT_OR_STRING,
            null,
            null, null,
            List.of("none", "auto", "required")
    ),
    PARALLEL_TOOL_CALLS(
            "parallel_tool_calls",
            "Разрешить ли параллельные вызовы функций при использовании инструментов.",
            ParameterType.BOOLEAN,
            true,
            null, null
    ),

    // Устаревшие параметры для функций (совместимость)
    FUNCTION_CALL(
            "function_call",
            "(Deprecated) Управляет вызовом функции: 'none', 'auto' или объект с именем.",
            ParameterType.OBJECT_OR_STRING,
            null,
            null, null
    ),
    FUNCTIONS(
            "functions",
            "(Deprecated) Список функций, которые модель может вызывать.",
            ParameterType.ARRAY,
            null,
            null, null
    ),

    // Параметры для моделей рассуждений
    REASONING_EFFORT(
            "reasoning_effort",
            "Ограничение усилий на рассуждение для моделей рассуждения (o1, o3-mini и др.).",
            ParameterType.STRING,
            null,
            null, null,
            List.of("low", "medium", "high")
    ),
    CHAT_TEMPLATE_KWARGS(
            "chat_template_kwargs",
            "Аргументы шаблона чата: enable_thinking (вкл/выкл рассуждения) и clear_thinking (сохранять контекст).",
            ParameterType.OBJECT,
            null,
            null, null
    ),

    // Прочие параметры (метаданные, предсказания, веб-поиск)
    METADATA(
            "metadata",
            "Набор до 16 пар ключ-значение, прикрепляемых к объекту запроса.",
            ParameterType.OBJECT,
            null,
            null, null
    ),
    PREDICTION(
            "prediction",
            "Параметры предсказания контента (type: 'content').",
            ParameterType.OBJECT,
            null,
            null, null
    ),
    WEB_SEARCH_OPTIONS(
            "web_search_options",
            "Опции для встроенного веб-поиска: search_context_size и user_location.",
            ParameterType.OBJECT,
            null,
            null, null
    ),

    // Кастомные / расширенные параметры (не входят в стандарт OpenAI, но нужны для некоторых моделей)
    RAW(
            "raw",
            "Если true, шаблон чата не применяется — требуется строгое соответствие формату конкретной модели.",
            ParameterType.BOOLEAN,
            true,
            null, null
    ),
    LORA(
            "lora",
            "Название LoRA-модели для донастройки базовой модели (кастомное расширение).",
            ParameterType.STRING,
            null,
            null, null
    ),
    SKIP_SPECIAL_TOKENS(
            "skip_special_tokens",
            "Если true, специальные токены (например, <|endoftext|>) не включаются в выходной текст.",
            ParameterType.BOOLEAN,
            true,
            null, null
    );

    private final String jsonKey;
    private final String description;
    private final ParameterType type;
    private final Object defaultValue;
    private final Double min;
    private final Double max;
    private final List<String> possibleValues;

    AiModelParam(String jsonKey, String description, ParameterType type, Object defaultValue, Double min, Double max) {
        this(jsonKey, description, type, defaultValue, min, max, null);
    }

    AiModelParam(String jsonKey, String description, ParameterType type,
                 Object defaultValue, Double min, Double max, List<String> possibleValues) {
        this.jsonKey = jsonKey;
        this.description = description;
        this.type = type;
        this.defaultValue = defaultValue;
        this.min = min;
        this.max = max;
        this.possibleValues = possibleValues;
    }

    public enum ParameterType {
        NUMBER,
        INTEGER,
        STRING,
        BOOLEAN,
        OBJECT,
        ARRAY,
        ARRAY_OR_STRING,
        OBJECT_OR_STRING
    }
}
