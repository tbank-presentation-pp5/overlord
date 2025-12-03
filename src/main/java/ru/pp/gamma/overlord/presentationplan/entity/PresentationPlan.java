package ru.pp.gamma.overlord.presentationplan.entity;

import io.hypersistence.utils.hibernate.type.json.JsonType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Type;

import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "presentation_plan")
public class PresentationPlan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "short_description", nullable = false, columnDefinition = "TEXT")
    private String shortDescription;

    @Column(name = "slides_count", nullable = false)
    private Integer slidesCount;

    @Type(JsonType.class)
    @Column(name = "elements", nullable = false, columnDefinition = "jsonb")
    private List<PresentationPlanElement> elements;
}
