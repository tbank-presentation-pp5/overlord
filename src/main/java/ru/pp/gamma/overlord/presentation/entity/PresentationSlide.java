package ru.pp.gamma.overlord.presentation.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import ru.pp.gamma.overlord.presentation.template.entity.TemplateSlide;

import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "presentation_slide",
        uniqueConstraints = @UniqueConstraint(columnNames = {"order", "id"})
)
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

    @OneToMany(mappedBy = "slide", cascade = CascadeType.PERSIST)
    private List<SlideField> fields;

    @Column(name = "order_number", nullable = false)
    private Integer orderNumber;
}
