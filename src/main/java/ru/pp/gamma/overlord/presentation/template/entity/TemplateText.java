package ru.pp.gamma.overlord.presentation.template.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "template_text")
public class TemplateText {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "font_family", nullable = false)
    private String fontFamily;

    @Column(name = "font_size", nullable = false)
    private Double fontSize;

    @Enumerated(EnumType.STRING)
    @Column(name = "font_style", nullable = false)
    private FontStyle fontStyle;

    @Enumerated(EnumType.STRING)
    @Column(name = "font_color", nullable = false)
    private FontColor color;

    @OneToOne
    @JoinColumn(name = "position_id", nullable = false)
    private TemplateShapePosition position;
}
