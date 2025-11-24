package ru.pp.gamma.overlord.presentationplan.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "presentation_plan_element")
public class PresentationPlanElement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title", nullable = false)
    private String title;

    @ManyToOne
    @JoinColumn(name = "presentation_plan_id", nullable = false)
    private PresentationPlan presentationPlan;

    @OneToMany(cascade = CascadeType.PERSIST, mappedBy = "planElement")
    private List<PlanElementItem> items;
}
