package ru.pp.gamma.overlord.presentation.api;

import com.google.api.services.drive.model.File;
import com.ibm.icu.text.Transliterator;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;
import ru.pp.gamma.overlord.export.googleslides.GoogleSlidesExportService;
import ru.pp.gamma.overlord.export.image.ImageExportService;
import ru.pp.gamma.overlord.export.pdf.PDFExportService;
import ru.pp.gamma.overlord.export.pdfv2.PDFV2ExportService;
import ru.pp.gamma.overlord.export.pptx.PPTXExportService;
import ru.pp.gamma.overlord.presentation.dto.export.PresentationExportSlidesRequest;
import ru.pp.gamma.overlord.presentation.dto.export.PresentationExportSlidesResponse;
import ru.pp.gamma.overlord.presentation.entity.Presentation;
import ru.pp.gamma.overlord.presentation.service.PresentationService;

import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/presentations/{id}")
public class PresentationExportController {

    private static final String PPTX_MEDIA_TYPE =
            "application/vnd.openxmlformats-officedocument.presentationml.presentation";
    private static final String PATTERN_GOOGLE_FILE_VIEW = "https://drive.google.com/file/d/%s/view";

    private final PresentationService presentationService;
    private final PPTXExportService pptxExportService;
    private final PDFExportService pdfExportService;
    private final PDFV2ExportService pdfV2ExportService;
    private final ImageExportService imageExportService;
    private final GoogleSlidesExportService googleSlidesExportService;

    @GetMapping("/pptx/download")
    public ResponseEntity<byte[]> exportPPTX(@PathVariable long id) {
        Presentation presentation = presentationService.getById(id);
        byte[] exportResult = pptxExportService.export(presentation);

        String filename = toLatin(presentation.getName()) + ".pptx";
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename)
                .contentType(MediaType.parseMediaType(PPTX_MEDIA_TYPE))
                .body(exportResult);

    }

    @GetMapping("/pdf/download")
    public ResponseEntity<byte[]> exportPDF(@PathVariable long id) {
        Presentation presentation = presentationService.getById(id);
        byte[] exportResult = pdfExportService.export(presentation);

        String filename = toLatin(presentation.getName()) + ".pptx";

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename)
                .contentType(MediaType.APPLICATION_PDF)
                .body(exportResult);
    }

    @GetMapping("/pdfv2/download")
    public ResponseEntity<byte[]> exportPDFV2(@PathVariable long id) {
        Presentation presentation = presentationService.getById(id);
        byte[] exportResult = pdfV2ExportService.export(presentation);

        String filename = toLatin(presentation.getName()) + ".pptx";
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename)
                .contentType(MediaType.APPLICATION_PDF)
                .body(exportResult);
    }

    @GetMapping(value = "/images-in-zip/download", produces = "application/zip")
    public ResponseEntity<StreamingResponseBody> exportImagesInZip(@PathVariable long id) {
        Presentation presentation = presentationService.getById(id);
        List<byte[]> images = imageExportService.export(presentation, 1);

        StreamingResponseBody body = out -> {
            try (ZipOutputStream zip = new ZipOutputStream(out)) {
                zip.setLevel(0);

                for (int i = 0; i < images.size(); i++) {
                    zip.putNextEntry(new ZipEntry("slide%d.png".formatted(i + 1)));
                    zip.write(images.get(i));
                    zip.closeEntry();
                }
            }
        };

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"images.zip\"")
                .body(body);
    }

    @PostMapping(value = "/google-slides/export")
    public PresentationExportSlidesResponse exportGoogleSlides(
            @PathVariable long id,
            @RequestBody PresentationExportSlidesRequest request
    ) {
        Presentation presentation = presentationService.getById(id);
        File file = googleSlidesExportService.export(presentation, request.oauthAccessToken());
        return new PresentationExportSlidesResponse(PATTERN_GOOGLE_FILE_VIEW.formatted(file.getId()));
    }

    private static String toLatin(String cyrillic) {
        Transliterator transliterator = Transliterator.getInstance("Russian-Latin/BGN");
        return transliterator.transliterate(cyrillic).replace(" ", "_")
                .replace("ʹ", "");
    }
}
