package ru.pp.gamma.overlord.export.image;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.pp.gamma.overlord.export.pdf.converter.PPTXToImageConverter;
import ru.pp.gamma.overlord.export.pptx.PPTXExportService;
import ru.pp.gamma.overlord.presentation.entity.Presentation;

import java.util.List;

/**
 * Сервис экспортирует презентацию в PNG изображения
 */
@RequiredArgsConstructor
@Service
public class ImageExportService {

    private final PPTXExportService pptxExportService;
    private final PPTXToImageConverter pptxToImageConverter;

    /**
     * Экспортирует презентацию в список массивов байтов - изображения слайдов в PNG
     *
     * @param presentation презентация с базы данных
     * @param scale        на сколько масштабировать изображение, 1 - оригинал
     * @return список массивов байтов - изображения слайдов
     */
    public List<byte[]> export(Presentation presentation, double scale) {
        byte[] bytePresentation = pptxExportService.export(presentation);
        return pptxToImageConverter.convert(bytePresentation, scale);
    }

}
