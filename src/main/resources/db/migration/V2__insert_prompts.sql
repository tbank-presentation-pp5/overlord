INSERT INTO template_presentation(name)
VALUES ('Базовый шаблон Т-банк');

INSERT INTO template_slide(type, name, prompt)
values ('MAIN', 'Титульный', 'Главный слайд презентации (титульный). Содержит только основной заголовок'),
       ('SECTION', 'Отбивочный',
        'Слайд, разделяющий части презентации. Содержит название раздела и краткое пояснение.'),
       ('TEXT_WITH_IMAGE', 'Контентный слайд с изображением',
        'Слайд с заголовком, текстовым содержимым и фотографией. Модель также должна сгенерировать промпт для создания изображения.');

INSERT INTO template_slide_field(type, name, json_key, prompt, example, template_slide_id)
values ('TEXT', 'Заголовок', 'title', 'string — основной заголовок (1 строка, до 10 слов)',
        'Как искусственный интеллект меняет образование', 1),
       ('TEXT', 'Название раздела', 'section_title', 'string — название раздела', 'Анализ рынка', 2),
       ('TEXT', 'Заголовок', 'title', 'string — заголовок слайда', 'Рост использования ИИ в образовании', 3),
       ('TEXT', 'Основной текст', 'text', 'string — основной текст (2–4 предложения)',
        'Технологии машинного обучения активно внедряются в образовательные платформы, помогая адаптировать курсы под каждого студента.',
        3),
       ('IMAGE', 'Изображение', 'image_prompt', 'string — промпт на английском языке для генерации изображения',
        'modern classroom with students using laptops, teacher explaining AI concepts, futuristic lighting, realistic style',
        3);

INSERT INTO template_presentation_slide(presentation_template_id, slide_template_id)
VALUES (1, 1),
       (1, 2),
       (1, 3);
