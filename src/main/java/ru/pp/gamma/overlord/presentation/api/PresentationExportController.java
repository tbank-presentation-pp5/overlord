package ru.pp.gamma.overlord.presentation.api;

import com.ibm.icu.text.Transliterator;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.pp.gamma.overlord.export.pdf.PDFExportService;
import ru.pp.gamma.overlord.export.pdfv2.PDFV2ExportService;
import ru.pp.gamma.overlord.export.pptx.PPTXExportService;
import ru.pp.gamma.overlord.presentation.entity.Presentation;
import ru.pp.gamma.overlord.presentation.repository.PresentationRepository;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/presentations/{id}")
public class PresentationExportController {

    private static final String PPTX_MEDIA_TYPE =
            "application/vnd.openxmlformats-officedocument.presentationml.presentation";

    private final PresentationRepository presentationRepository;
    private final PPTXExportService pptxExportService;
    private final PDFExportService pdfExportService;
    private final PDFV2ExportService pdfV2ExportService;

    @GetMapping("/pptx/download")
    public ResponseEntity<byte[]> exportPPTX(@PathVariable long id) {
        Presentation presentation = presentationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Presentation not found"));
        byte[] exportResult = pptxExportService.export(presentation);

        String filename = toLatin(presentation.getName()) + ".pptx";
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename)
                .contentType(MediaType.parseMediaType(PPTX_MEDIA_TYPE))
                .body(exportResult);

    }

    @GetMapping("/pdf/download")
    public ResponseEntity<byte[]> exportPDF(@PathVariable long id) {
        Presentation presentation = presentationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Presentation not found"));
        byte[] exportResult = pdfExportService.export(presentation);

        String filename = toLatin(presentation.getName()) + ".pptx";

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename)
                .contentType(MediaType.APPLICATION_PDF)
                .body(exportResult);
    }

    @GetMapping("/pdfv2/download")
    public ResponseEntity<byte[]> exportPDFV2(@PathVariable long id) {
        Presentation presentation = presentationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Presentation not found"));
        byte[] exportResult = pdfV2ExportService.export(presentation);

        String filename = toLatin(presentation.getName()) + ".pptx";
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename)
                .contentType(MediaType.APPLICATION_PDF)
                .body(exportResult);
    }

    private static String toLatin(String cyrillic) {
        Transliterator transliterator = Transliterator.getInstance("Russian-Latin/BGN");
        return transliterator.transliterate(cyrillic).replace(" ", "_")
                .replace("ʹ", "");
    }
}
