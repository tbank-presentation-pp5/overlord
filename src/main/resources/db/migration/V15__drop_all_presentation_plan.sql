ALTER TABLE plan_element_item
    DROP CONSTRAINT fk_plan_element_item_on_plan_element;

ALTER TABLE presentation_plan_element
    DROP CONSTRAINT fk_presentation_plan_element_on_presentation_plan;

DROP TABLE plan_element_item CASCADE;

DROP TABLE presentation_plan CASCADE;

DROP TABLE presentation_plan_element CASCADE;