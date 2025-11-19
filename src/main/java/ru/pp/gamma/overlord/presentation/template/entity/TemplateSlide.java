package ru.pp.gamma.overlord.presentation.template.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "template_slide",
        uniqueConstraints = @UniqueConstraint(columnNames = {"template_presentation_id", "type"})
)
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

    @Column(name = "layout_id", nullable = false)
    private Integer layoutId;

    @ManyToOne
    @JoinColumn(name = "template_presentation_id", nullable = false)
    private TemplatePresentation presentations;

    @OneToMany(mappedBy = "slide")
    private List<TemplateSlideField> fields;
}
