ALTER TABLE presentation_slide
    ADD order_number INTEGER;

UPDATE presentation_slide
SET order_number = '1111111'
WHERE order_number IS NULL;
ALTER TABLE presentation_slide
    ALTER COLUMN order_number SET NOT NULL;

ALTER TABLE presentation_slide
    ADD CONSTRAINT uc_f3b8071584c3c03e49ee017e4 UNIQUE (id);

ALTER TABLE presentation_slide
    DROP COLUMN "order";