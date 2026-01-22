INSERT INTO template_slide(id, type, name, description, layout_id, template_presentation_id)
VALUES (5,
        'IMAGE_GRID_5',
        'Галерея из 5 изображений',
        'Слайд с пятью визуальными блоками: три равных по ширине в верхнем ряду, два в нижнем ряду, левый нижний расширен по ширине.',
        5,
        1);

INSERT INTO template_slide_field(id, content_type, name, schema_key, description, example, meta, shape_name, template_slide_id)
VALUES (11, 'IMAGE', 'Изображение 1', 'image_1', 'Верхний ряд, левый блок. Квадратное/почти квадратное изображение.',
        '"high-quality photo of a sunny beach, turquoise water, clean sand, centered subject, dark background frame"',
        '{"imageSize": {"width": 936, "height": 936}}', 'image_1', 5),
       (12, 'IMAGE', 'Изображение 2', 'image_2', 'Верхний ряд, центральный блок. Квадратное/почти квадратное изображение.',
        '"high-quality photo of a sunny beach, turquoise water, clean sand, centered subject, dark background frame"',
        '{"imageSize": {"width": 936, "height": 936}}', 'image_2', 5),
       (13, 'IMAGE', 'Изображение 3', 'image_3', 'Верхний ряд, правый блок. Квадратное/почти квадратное изображение.',
        '"high-quality photo of a sunny beach, turquoise water, clean sand, centered subject, dark background frame"',
        '{"imageSize": {"width": 936, "height": 936}}', 'image_3', 5),
       (14, 'IMAGE', 'Изображение 4', 'image_4', 'Нижний ряд, широкий левый блок.',
        '"wide panoramic photo of a sunny beach shoreline, turquoise water, clean sand, cinematic crop, dark background frame"',
        '{"imageSize": {"width": 976, "height": 464}}', 'image_4', 5),
       (15, 'IMAGE', 'Изображение 5', 'image_5', 'Нижний ряд, правый блок. Квадратное/почти квадратное изображение.',
        '"high-quality photo of a sunny beach, turquoise water, clean sand, centered subject, dark background frame"',
        '{"imageSize": {"width": 936, "height": 936}}', 'image_5', 5);
