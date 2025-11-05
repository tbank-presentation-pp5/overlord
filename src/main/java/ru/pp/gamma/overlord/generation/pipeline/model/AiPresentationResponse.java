package ru.pp.gamma.overlord.generation.pipeline.model;

import com.fasterxml.jackson.databind.JsonNode;

import java.util.List;


public record AiPresentationResponse(String name, List<JsonNode> slides) {
}