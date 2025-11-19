-- layout_id
UPDATE template_slide
SET layout_id = 1
WHERE type = 'MAIN';

UPDATE template_slide
SET layout_id = 2
WHERE type = 'SECTION';

UPDATE template_slide
SET layout_id = 3
WHERE type = 'TEXT_WITH_IMAGE';


-- image
INSERT INTO template_shape_position(x, y, width, height) --1
VALUES (573.5115748031496, 157.33645669291337, 352, 264);

UPDATE template_image
SET position_id = 1,
    width       = 704,
    height      = 528
WHERE id = 1;


-- заголовок
INSERT INTO template_shape_position(x, y, width, height) --2
VALUES (36.59110236220472, 224.65748031496062, 820.1232283464567, 54.173464566929134);

INSERT INTO template_text(font_family, font_size, font_style, font_color, position_id) --1
VALUES ('Tinkoff Sans', 44.2, 'BOLD', 'WHITE', 2);

UPDATE template_slide_field
SET template_text_id = 1
WHERE id = 1;


-- Название раздела
INSERT INTO template_shape_position(x, y, width, height) --3
VALUES (268.89149606299213, 217.93622047244094, 534.0477952755906, 107.44645669291339);

INSERT INTO template_text(font_family, font_size, font_style, font_color, position_id) --2
VALUES ('Tinkoff Sans', 36.9, 'BOLD', 'WHITE', 3);

UPDATE template_slide_field
SET template_text_id = 2
WHERE id = 2;


-- Заголовок слайда
INSERT INTO template_shape_position(x, y, width, height) --4
VALUES (36.59110236220472, 27.21299212598425, 761.5027559055118, 44.67708661417323);

INSERT INTO template_text(font_family, font_size, font_style, font_color, position_id) --3
VALUES ('Tinkoff Sans', 36.9, 'BOLD', 'WHITE', 4);

UPDATE template_slide_field
SET template_text_id = 3
WHERE id = 3;


-- Основной текст
INSERT INTO template_shape_position(x, y, width, height) --5
VALUES (36.591181102362206, 138.20984251968503, 468.4834645669291, 300.8943307086614);

INSERT INTO template_text(font_family, font_size, font_style, font_color, position_id) --4
VALUES ('Tinkoff Sans', 28, 'REGULAR', 'WHITE', 5);

UPDATE template_slide_field
SET template_text_id = 4
WHERE id = 4;
