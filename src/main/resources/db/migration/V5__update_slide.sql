ALTER TABLE presentation_slide
    ADD "order" INTEGER;

ALTER TABLE presentation_slide
    ALTER COLUMN "order" SET NOT NULL;

ALTER TABLE presentation_slide
    ADD CONSTRAINT uc_b6139988b07ff1a2cede900db UNIQUE ("order", id);

ALTER TABLE slide_field
    DROP COLUMN "order";

ALTER TABLE slide_field
    ALTER COLUMN value SET NOT NULL;