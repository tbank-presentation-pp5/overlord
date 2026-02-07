ALTER TABLE presentation_slide
    ADD image_preview_id BIGINT;

ALTER TABLE image
    ADD is_deleted BOOLEAN;

UPDATE image
SET is_deleted = 'false'
WHERE is_deleted IS NULL;
ALTER TABLE image
    ALTER COLUMN is_deleted SET NOT NULL;

ALTER TABLE presentation_slide
    ADD CONSTRAINT uc_189a72f8fa500bf90bc93cf5c UNIQUE (order_number, id);

ALTER TABLE presentation_slide
    ADD CONSTRAINT uc_presentation_slide_image_preview UNIQUE (image_preview_id);

ALTER TABLE presentation_slide
    ADD CONSTRAINT FK_PRESENTATION_SLIDE_ON_IMAGE_PREVIEW FOREIGN KEY (image_preview_id) REFERENCES image (id);

ALTER TABLE image
    ALTER COLUMN name SET NOT NULL;
