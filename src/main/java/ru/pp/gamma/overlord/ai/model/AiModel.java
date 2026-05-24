package ru.pp.gamma.overlord.ai.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Map;

@Getter
@RequiredArgsConstructor
public enum AiModel {
    CF_MISTRAL_SMALL(
            "@cf/mistralai/mistral-small-3.1-24b-instruct",
            AiApiStyle.CF_PATH_SMART,
            "Mistral Small 3.1 24B (CF)",
            Map.of(
                    AiModelParam.MAX_TOKENS, 128_000,
                    AiModelParam.MAX_COMPLETION_TOKENS, 8_192,
                    AiModelParam.TEMPERATURE, 0.21,
                    AiModelParam.TOP_K, 17,
                    AiModelParam.TOP_P, 0.95,
                    AiModelParam.REPETITION_PENALTY, 1.05,
                    AiModelParam.FREQUENCY_PENALTY, 0.0,
                    AiModelParam.PRESENCE_PENALTY, 0.0,
                    AiModelParam.RAW, true
            )
    ),
    CF_GEMMA_4_26B_A4B_IT(
            "@cf/google/gemma-4-26b-a4b-it",
            AiApiStyle.CF_PATH_SMART,
            "Google Gemma 4 26B (CF)",
            Map.of(
                    AiModelParam.MAX_TOKENS, 256_000,
                    AiModelParam.MAX_COMPLETION_TOKENS, 16_384,
                    AiModelParam.TEMPERATURE, 1,
                    AiModelParam.TOP_K, 64,
                    AiModelParam.TOP_P, 0.91,
                    AiModelParam.PRESENCE_PENALTY, 0.0,
                    AiModelParam.FREQUENCY_PENALTY, 0.0,
                    AiModelParam.RAW, true,
                    AiModelParam.REASONING_EFFORT, "medium"
            )
    ),
    CF_GPT_OSS_120B(
            "@cf/openai/gpt-oss-120b",
            AiApiStyle.CF_RESPONSES,
            "GPT-OSS 120B (CF)",
            Map.of() // Пока что забил на реализацию GPT-OSS
    ),
    CF_KIMI_K2_6(
            "@cf/moonshotai/kimi-k2.6",
            AiApiStyle.CF_PATH_SMART,
            "Kimi K2.6 (CF)",
            Map.of(
                    AiModelParam.MAX_TOKENS, 16_384,
                    AiModelParam.MAX_COMPLETION_TOKENS, 16_384,
                    AiModelParam.TEMPERATURE, 1,
                    AiModelParam.TOP_P, 0.95,
                    AiModelParam.FREQUENCY_PENALTY, 0.0,
                    AiModelParam.PRESENCE_PENALTY, 0.0,
                    AiModelParam.RAW, true,
                    AiModelParam.REASONING_EFFORT, "medium"
            )
    ),
    CF_KIMI_K2_5(
            "@cf/moonshotai/kimi-k2.5",
            AiApiStyle.CF_PATH_SMART,
            "Kimi K2.5 (CF)",
            Map.of(
                    AiModelParam.MAX_TOKENS, 16_384,
                    AiModelParam.MAX_COMPLETION_TOKENS, 16_384,
                    AiModelParam.TEMPERATURE, 1,
                    AiModelParam.TOP_P, 0.95,
                    AiModelParam.FREQUENCY_PENALTY, 0.0,
                    AiModelParam.PRESENCE_PENALTY, 0.0,
                    AiModelParam.RAW, true,
                    AiModelParam.REASONING_EFFORT, "medium"
            )
    ),
    CF_NEMOTRON_3_120B(
            "@cf/nvidia/nemotron-3-120b-a12b",
            AiApiStyle.CF_PATH_SMART,
            "Nemotron 3 120B A12B (CF)",
            Map.of(
                    AiModelParam.MAX_TOKENS, 256_000,
                    AiModelParam.MAX_COMPLETION_TOKENS, 4_096,
                    AiModelParam.TEMPERATURE, 0.50,
                    AiModelParam.TOP_K, 30,
                    AiModelParam.TOP_P, 0.90,
                    AiModelParam.FREQUENCY_PENALTY, 0.0,
                    AiModelParam.PRESENCE_PENALTY, 0.1,
                    AiModelParam.RAW, true,
                    AiModelParam.REASONING_EFFORT, "medium"
            )
    ),
    CF_GEMMA_3_12B(
            "@cf/google/gemma-3-12b-it",
            AiApiStyle.CF_PATH_SMART,
            "Gemma 3 (CF)",
            Map.of(
                    AiModelParam.MAX_TOKENS, 80_000,
                    AiModelParam.MAX_COMPLETION_TOKENS, 8_192,
                    AiModelParam.TEMPERATURE, 1,
                    AiModelParam.TOP_K, 40,
                    AiModelParam.TOP_P, 0.95,
                    AiModelParam.REPETITION_PENALTY, 1.0,
                    AiModelParam.FREQUENCY_PENALTY, 0.0,
                    AiModelParam.PRESENCE_PENALTY, 0.0,
                    AiModelParam.RAW, true
            )
    ),
    CF_QWEN_2_5_CODER(
            "@cf/qwen/qwen2.5-coder-32b-instruct",
            AiApiStyle.CF_PATH_SMART,
            "Qwen 2.5 Coder (CF)",
            Map.of(
                    AiModelParam.MAX_TOKENS, 4_096,
                    AiModelParam.MAX_COMPLETION_TOKENS, 4_096,
                    AiModelParam.TEMPERATURE, 0.25,
                    AiModelParam.TOP_K, 25,
                    AiModelParam.TOP_P, 0.5,
                    AiModelParam.RAW, true
            )
    ),
    CF_QWEN3_30B(
            "@cf/qwen/qwen3-30b-a3b-fp8",
            AiApiStyle.CF_PATH_SMART,
            "Qwen3 30B A3B FP8 (CF)",
            Map.of(
                    AiModelParam.MAX_TOKENS, 4_096,
                    AiModelParam.MAX_COMPLETION_TOKENS, 4_096,
                    AiModelParam.TEMPERATURE, 0.50,
                    AiModelParam.TOP_K, 20,
                    AiModelParam.TOP_P, 0.90,
                    AiModelParam.REPETITION_PENALTY, 1.0,
                    AiModelParam.FREQUENCY_PENALTY, 0.0,
                    AiModelParam.PRESENCE_PENALTY, 0.0,
                    AiModelParam.RAW, true
            )
    ),
    GIGACHAT_2(
            "GigaChat-2",
            AiApiStyle.GIGACHAT,
            "GigaChat 2",
            Map.of()
    ),
    GIGACHAT_2_PRO(
            "GigaChat-2-Pro",
            AiApiStyle.GIGACHAT,
            "GigaChat 2 Pro",
            Map.of(
                    AiModelParam.TEMPERATURE, 0.55,
                    AiModelParam.TOP_P, 0.95,
                    AiModelParam.REPETITION_PENALTY, 1.10,
                    AiModelParam.FREQUENCY_PENALTY, 0.0,
                    AiModelParam.PRESENCE_PENALTY, 0.0
            )
    ),
    GIGACHAT_2_MAX(
            "GigaChat-2-Max",
            AiApiStyle.GIGACHAT,
            "GigaChat 2 Max",
            Map.of(
                    AiModelParam.TEMPERATURE, 0.25,
                    AiModelParam.TOP_P, 0.85,
                    AiModelParam.REPETITION_PENALTY, 1.05,
                    AiModelParam.FREQUENCY_PENALTY, 0.0,
                    AiModelParam.PRESENCE_PENALTY, 0.15
            )
    ),
    GIGACHAT_3_ULTRA(
            "GigaChat-3-Ultra",
            AiApiStyle.GIGACHAT,
            "GigaChat 3 Ultra",
            Map.of(
                    AiModelParam.TEMPERATURE, 0.25,
                    AiModelParam.TOP_P, 0.85,
                    AiModelParam.REPETITION_PENALTY, 1.05,
                    AiModelParam.FREQUENCY_PENALTY, 0.0,
                    AiModelParam.PRESENCE_PENALTY, 0.15
            )
    );

    // ещё на тесте, протестирую доделаю уберу:
    /*
     * модели, модели которые чет не заводились:
     *   @cf/meta/llama-3.3-70b-instruct
     *   @cf/google/gemma-3-27b-it
     *   @cf/deepseek-ai/deepseek-r1-distill-llama-70b
     *
     * модели с нефиксируемыми проблемами (не включены):
     *   @cf/qwen/qwq-32b        - content всегда пустой, только reasoning_content (thinking chain)
     *   @cf/openai/gpt-oss-20b  - генерирует структурно сломанный JSON
     */

    private final String modelId;

    private final AiApiStyle apiStyle;

    private final String displayName;

    private final Map<AiModelParam, Object> defaultParams;
}