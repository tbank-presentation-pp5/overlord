package ru.pp.gamma.overlord.presentation.api;

import com.ibm.icu.text.Transliterator;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.pp.gamma.overlord.export.pptx.PPTXExportService;
import ru.pp.gamma.overlord.presentation.entity.Presentation;
import ru.pp.gamma.overlord.presentation.repository.PresentationRepository;

import java.nio.charset.StandardCharsets;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/presentations/{id}")
public class PresentationExportController {

    private static final String PPTX_MEDIA_TYPE =
            "application/vnd.openxmlformats-officedocument.presentationml.presentation";

    private final PresentationRepository presentationRepository;
    private final PPTXExportService pptxExportService;

    @GetMapping(value = "/pptx/download")
    public ResponseEntity<byte[]> exportPPTX(@PathVariable long id) {
        Presentation presentation = presentationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Presentation not found"));
        byte[] exportResult = pptxExportService.export(presentation);

        String filename = sanitizeFilename(presentation.getName()) + ".pptx";

        HttpHeaders headers = new HttpHeaders();

        ContentDisposition contentDisposition = ContentDisposition.builder("attachment")
                .filename(filename, StandardCharsets.UTF_8)
                .build();

        headers.setContentDisposition(contentDisposition);
        headers.setContentType(MediaType.parseMediaType(PPTX_MEDIA_TYPE));

        return ResponseEntity.ok()
                .headers(headers)
                .body(exportResult);
    }

    private static String sanitizeFilename(String filename) {
        if (filename == null || filename.isEmpty()) {
            return "presentation";
        }

        String latin = toLatin(filename);

        // запрещенные символы для всех ОС
        String sanitized = latin.replaceAll("[\u0000-\u001F\u007F]", "")
                .replaceAll("[<>:\"/\\\\|?*]", "") // Windows запрещенные
                .replace("/", "")                  // Unix запрещенные
                .replace("\\", "")
                .trim();

        // удаляем ведущие/конечные точки и пробелы (для windows)
        sanitized = sanitized.replaceAll("^\\.+|\\.+$", "");
        sanitized = sanitized.replaceAll("\\s+$|^\\s+", "");

        // обработка зарезервированных имен в Windows (да понимаю кринж)
        String[] windowsReserved = {
                "CON", "PRN", "AUX", "NUL",
                "COM1", "COM2", "COM3", "COM4", "COM5", "COM6", "COM7", "COM8", "COM9",
                "LPT1", "LPT2", "LPT3", "LPT4", "LPT5", "LPT6", "LPT7", "LPT8", "LPT9"
        };

        for (String reserved : windowsReserved) {
            if (sanitized.toUpperCase().startsWith(reserved) &&
                    (sanitized.length() == reserved.length() ||
                            sanitized.charAt(reserved.length()) == '.')) {
                sanitized = "file_" + sanitized;
                break;
            }
        }

        // ограничение длины
        if (sanitized.length() > 249) {
            sanitized = sanitized.substring(0, 249);
        }

        if (sanitized.isEmpty()) {
            return "presentation";
        }

        // имя не заканчивается на точку или пробел (для Windows)
        while (sanitized.endsWith(".") || sanitized.endsWith(" ")) {
            sanitized = sanitized.substring(0, sanitized.length() - 1);
        }

        return sanitized;
    }

    private static String toLatin(String cyrillic) {
        Transliterator transliterator = Transliterator.getInstance("Russian-Latin/BGN");
        return transliterator.transliterate(cyrillic).replace(" ", "_")
                .replace("ʹ", "");
    }
}
