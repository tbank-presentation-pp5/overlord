package ru.pp.gamma.overlord.presentation.template.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "template_slide_field",
        uniqueConstraints = @UniqueConstraint(columnNames = {"template_slide_id", "json_key"})
)
public class TemplateSlideField {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private SlideFieldType type;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "json_key", nullable = false)
    private String jsonKey;

    @Column(name = "prompt", nullable = false)
    private String prompt;

    @Column(name = "example", nullable = false)
    private String example;

    @ManyToOne
    @JoinColumn(name = "template_slide_id", nullable = false)
    private TemplateSlide slide;
}
