ALTER TABLE template_slide_field
    ADD template_text_id BIGINT;

ALTER TABLE template_slide_field
    ADD CONSTRAINT uc_template_slide_field_template_text UNIQUE (template_text_id);

ALTER TABLE template_slide_field
    ADD CONSTRAINT FK_TEMPLATE_SLIDE_FIELD_ON_TEMPLATE_TEXT FOREIGN KEY (template_text_id) REFERENCES template_text (id);