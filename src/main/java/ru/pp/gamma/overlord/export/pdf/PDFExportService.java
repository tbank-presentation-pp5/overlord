package ru.pp.gamma.overlord.export.pdf;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.pp.gamma.overlord.export.pdf.converter.ImagesToPdfConverter;
import ru.pp.gamma.overlord.export.pdf.converter.PPTXToImageConverter;
import ru.pp.gamma.overlord.export.pptx.PPTXExportService;
import ru.pp.gamma.overlord.presentation.entity.Presentation;

import java.util.List;

@RequiredArgsConstructor
@Service
public class PDFExportService {

    private static final double SCALE = 2.0;

    private final PPTXExportService pptxExportService;
    private final PPTXToImageConverter pptxToImageConverter;
    private final ImagesToPdfConverter imagesToPdfConverter;


    public byte[] export(Presentation presentation) {
        byte[] bytePresentation = pptxExportService.export(presentation);
        List<byte[]> images = pptxToImageConverter.convert(bytePresentation, SCALE);
        return imagesToPdfConverter.convert(images, SCALE);
    }

}
