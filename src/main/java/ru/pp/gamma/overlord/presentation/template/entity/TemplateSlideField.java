package ru.pp.gamma.overlord.presentation.template.entity;

import com.fasterxml.jackson.databind.JsonNode;
import io.hypersistence.utils.hibernate.type.json.JsonType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Type;

@Getter
@Setter
@Entity
@Table(name = "template_slide_field",
        uniqueConstraints = @UniqueConstraint(columnNames = {"template_slide_id", "schema_key"})
)
public class TemplateSlideField {

    @Id
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "content_type", nullable = false)
    private SlideFieldContentType contentType;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "schema_key", nullable = false)
    private String schemaKey;

    @Column(name = "description", nullable = false)
    private String description;

    @Type(JsonType.class)
    @Column(name = "example", nullable = false, columnDefinition = "jsonb")
    private JsonNode example;

    @Type(JsonType.class)
    @Column(name = "meta", nullable = false, columnDefinition = "jsonb")
    private JsonNode meta;

    @Column(name = "shape_name", nullable = false)
    private String shapeName;

    @ManyToOne
    @JoinColumn(name = "template_slide_id", nullable = false)
    private TemplateSlide slide;
}
