package ru.pp.gamma.overlord.presentation.template.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "template_slide")
public class TemplateSlide {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false, unique = true)
    private SlideType type;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "prompt", nullable = false)
    private String prompt;

    @ManyToMany(mappedBy = "slides")
    private List<TemplatePresentation> presentations;

    @OneToMany(mappedBy = "slide")
    private List<TemplateSlideField> fields;
}
