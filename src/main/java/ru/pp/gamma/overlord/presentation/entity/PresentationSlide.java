package ru.pp.gamma.overlord.presentation.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import ru.pp.gamma.overlord.presentation.template.entity.TemplateSlide;

@Getter
@Setter
@Entity
@Table(name = "presentation_slide")
public class PresentationSlide {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "template_slide_id", nullable = false)
    private TemplateSlide template;

    @ManyToOne
    @JoinColumn(name = "presentation_id", nullable = false)
    private Presentation presentation;
}
