package ru.pp.gamma.overlord.presentation.entity;

import com.fasterxml.jackson.databind.JsonNode;
import io.hypersistence.utils.hibernate.type.json.JsonType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Type;
import ru.pp.gamma.overlord.image.entity.Image;
import ru.pp.gamma.overlord.presentation.template.entity.TemplateSlideField;

@Getter
@Setter
@Entity
@Table(name = "slide_field")
public class SlideField {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Type(JsonType.class)
    @Column(name = "value", nullable = false, columnDefinition = "jsonb")
    private JsonNode value;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "image_id")
    private Image image;

    @ManyToOne
    @JoinColumn(name = "template_slide_field_id", nullable = false)
    private TemplateSlideField template;

    @ManyToOne
    @JoinColumn(name = "slide_id", nullable = false)
    private PresentationSlide slide;
}
