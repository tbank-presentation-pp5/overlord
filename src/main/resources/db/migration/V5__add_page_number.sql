ALTER TABLE template_slide
    ADD is_need_page_number BOOLEAN;

UPDATE template_slide
SET is_need_page_number = false
WHERE is_need_page_number IS NULL;

ALTER TABLE template_slide
    ALTER COLUMN is_need_page_number SET NOT NULL;

-- Обновление данных
UPDATE template_slide
SET is_need_page_number = true
WHERE id in (3, 4, 5, 6);
