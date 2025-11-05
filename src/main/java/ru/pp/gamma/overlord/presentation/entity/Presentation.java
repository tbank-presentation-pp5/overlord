package ru.pp.gamma.overlord.presentation.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import ru.pp.gamma.overlord.presentation.template.entity.TemplatePresentation;

import java.time.Instant;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "presentation")
public class Presentation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "created_at", nullable = false, updatable = false)
    @CreationTimestamp
    private Instant createdAt;

    @Column(name = "updated_at", nullable = false)
    @UpdateTimestamp
    private Instant updatedAt;

    @ManyToOne
    @JoinColumn(name = "template_presentation__id", nullable = false)
    private TemplatePresentation template;

    @OneToMany(mappedBy = "presentation")
    private List<PresentationSlide> slides;
}
