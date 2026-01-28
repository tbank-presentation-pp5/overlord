package ru.pp.gamma.overlord.export.pdfv2;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClient;
import ru.pp.gamma.overlord.export.pptx.PPTXExportService;
import ru.pp.gamma.overlord.presentation.entity.Presentation;

import static org.springframework.http.MediaType.MULTIPART_FORM_DATA;

@Component
public class PDFV2ExportService {

    private static final String PATH = "/cool/convert-to/pdf";

    @Value("${collabora.url}")
    private String collaboraUrl;

    private final RestClient client;
    private final PPTXExportService pptxExportService;

    public PDFV2ExportService(@Qualifier("pdfv2RestClient") RestClient client, PPTXExportService pptxExportService) {
        this.client = client;
        this.pptxExportService = pptxExportService;
    }

    public byte[] export(Presentation presentation) {
        byte[] ppt = pptxExportService.export(presentation);
        return convertViaCollabora(ppt);
    }

    private byte[] convertViaCollabora(byte[] ppt) {
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("data", prepareBody(ppt));

        return client.post()
                .uri(collaboraUrl + PATH)
                .contentType(MULTIPART_FORM_DATA)
                .body(body)
                .retrieve()
                .body(byte[].class);
    }

    private Resource prepareBody(byte[] ppt) {
        return new ByteArrayResource(ppt) {
            @Override
            public String getFilename() {
                return "presentation.pptx";
            }
        };
    }
}
