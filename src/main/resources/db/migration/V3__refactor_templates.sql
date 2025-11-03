ALTER TABLE template_presentation_slide
    DROP CONSTRAINT fk_tempresli_on_template_presentation;

ALTER TABLE template_presentation_slide
    DROP CONSTRAINT fk_tempresli_on_template_slide;

ALTER TABLE template_slide
    ADD template_presentation_id BIGINT;

UPDATE template_slide
SET template_presentation_id = '1'
WHERE template_presentation_id IS NULL;
ALTER TABLE template_slide
    ALTER COLUMN template_presentation_id SET NOT NULL;

ALTER TABLE template_slide
    ADD CONSTRAINT uc_57a9a70ccab3c288677900af0 UNIQUE (template_presentation_id, type);

ALTER TABLE template_slide_field
    ADD CONSTRAINT uc_939b0409cceed0bffaf755ccf UNIQUE (template_slide_id, json_key);

ALTER TABLE template_slide
    ADD CONSTRAINT FK_TEMPLATE_SLIDE_ON_TEMPLATE_PRESENTATION FOREIGN KEY (template_presentation_id) REFERENCES template_presentation (id);

DROP TABLE template_presentation_slide CASCADE;