package ru.pp.gamma.overlord.presentation.template.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "template_presentation")
public class TemplatePresentation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @ManyToMany
    @JoinTable(
            name = "template_presentation_slide",
            joinColumns = @JoinColumn(name = "presentation_template_id", nullable = false),
            inverseJoinColumns = @JoinColumn(name = "slide_template_id", nullable = false)
    )
    private List<TemplateSlide> slides;
}
