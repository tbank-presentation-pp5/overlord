ALTER TABLE template_text
    ALTER COLUMN font_color SET NOT NULL;

ALTER TABLE template_text
    ALTER COLUMN font_family SET NOT NULL;

ALTER TABLE template_text
    ALTER COLUMN font_size SET NOT NULL;

ALTER TABLE template_text
    ALTER COLUMN font_style SET NOT NULL;

ALTER TABLE template_image
    ALTER COLUMN height SET NOT NULL;

ALTER TABLE template_shape_position
    ALTER COLUMN height SET NOT NULL;

ALTER TABLE template_slide
    ALTER COLUMN layout_id SET NOT NULL;

ALTER TABLE template_image
    ALTER COLUMN position_id SET NOT NULL;

ALTER TABLE template_text
    ALTER COLUMN position_id SET NOT NULL;

ALTER TABLE template_image
    ALTER COLUMN width SET NOT NULL;

ALTER TABLE template_shape_position
    ALTER COLUMN width SET NOT NULL;

ALTER TABLE template_shape_position
    ALTER COLUMN x SET NOT NULL;

ALTER TABLE template_shape_position
    ALTER COLUMN y SET NOT NULL;