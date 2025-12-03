package ru.pp.gamma.overlord.presentationplan.entity;

import java.util.List;

public record PresentationPlanElement(
        String title,
        List<String> points) {
}
