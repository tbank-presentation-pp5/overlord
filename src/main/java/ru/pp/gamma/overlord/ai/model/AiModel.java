package ru.pp.gamma.overlord.ai.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum AiModel {
    CF_MISTRAL_SMALL(
            "@cf/mistralai/mistral-small-3.1-24b-instruct",
            AiApiStyle.CF_PATH,
            8_192,
            "Mistral Small 3.1 24B (Cloudflare)"
    ),
    CF_GEMMA_4_26B_A4B_IT(
            "@cf/google/gemma-4-26b-a4b-it",
            AiApiStyle.CF_PATH_SMART,
            8_192,
            "Google Gemma 4 26B (Cloudflare)"
    ),
    CF_GLM_4_7_FLASH(
            "@cf/zai-org/glm-4.7-flash",
            AiApiStyle.CF_PATH_SMART,
            8_192,
            "GLM 4.7 Flash (Cloudflare)"
    ),
    CF_GPT_OSS_120B(
            "@cf/openai/gpt-oss-120b",
            AiApiStyle.CF_RESPONSES,
            8_192,
            "GPT-OSS 120B (Cloudflare)"
    ),
    CF_KIMI_K2_5(
            "@cf/moonshotai/kimi-k2.5",
            AiApiStyle.CF_PATH_SMART,
            8_192,
            "Kimi K2.5 (Cloudflare)"
    ),
    CF_NEMOTRON_3_120B(
            "@cf/nvidia/nemotron-3-120b-a12b",
            AiApiStyle.CF_PATH_SMART,
            8_192,
            "Nemotron 3 120B A12B (Cloudflare)"
    ),
    CF_GEMMA_3_12B(
            "@cf/google/gemma-3-12b-it",
            AiApiStyle.CF_PATH_SMART,
            4_096, // low limit — model hits CF output cap quickly
            "Gemma 3 (Cloudflare)"
    ),
    CF_QWEN_2_5_CODER(
            "@cf/qwen/qwen2.5-coder-32b-instruct",
            AiApiStyle.CF_PATH,
            4_096, // context window 32768 total, safe output limit
            "Qwen 2.5 Coder (Cloudflare)"
    ),
    CF_QWEN3_30B(
            "@cf/qwen/qwen3-30b-a3b-fp8",
            AiApiStyle.CF_PATH_SMART,
            8_192,
            "Qwen3 30B A3B FP8 (Cloudflare)"
    ),
    // gigachat не принимает max_tokens в этом формате
    GIGACHAT_2(
            "GigaChat-2",
            AiApiStyle.GIGACHAT,
            0,
            "GigaChat 2"
    ),
    GIGACHAT_2_PRO(
            "GigaChat-2-Pro",
            AiApiStyle.GIGACHAT,
            0,
            "GigaChat 2 Pro"
    ),
    GIGACHAT_2_MAX(
            "GigaChat-2-Max",
            AiApiStyle.GIGACHAT,
            0,
            "GigaChat 2 Max"
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

    private final int maxOutputTokens;

    private final String displayName;
}