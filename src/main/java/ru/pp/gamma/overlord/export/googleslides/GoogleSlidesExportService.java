package ru.pp.gamma.overlord.export.googleslides;

import com.google.api.client.auth.oauth2.BearerToken;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.http.ByteArrayContent;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.File;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.pp.gamma.overlord.export.pptx.PPTXExportService;
import ru.pp.gamma.overlord.presentation.entity.Presentation;

import java.io.IOException;

/**
 * Oauth авторизация для получения access токена - <a href="https://developers.google.com/identity/protocols/oauth2/javascript-implicit-flow"/>документация</a>
 * В google console созданный ключ - <a href="https://console.cloud.google.com/auth/overview">Console</a>
 * Для загрузки используется Drive API
 */
@RequiredArgsConstructor
@Service
public class GoogleSlidesExportService {

    private static final String APPLICATION_NAME = "t-bank-presentation-export";
    private static final String PPTX_MIME_TYPE = "application/vnd.openxmlformats-officedocument.presentationml.presentation";
    private static final String GOOGLE_SLIDES_MIME_TYPE = "application/vnd.google-apps.presentation";

    private final PPTXExportService pptxExportService;

    public File export(Presentation presentation, String accessToken) {
        byte[] pptxInBytes = pptxExportService.export(presentation);

        Credential credential = new Credential(BearerToken.authorizationHeaderAccessMethod())
                .setAccessToken(accessToken);
        Drive drive = new Drive.Builder(new NetHttpTransport(), GsonFactory.getDefaultInstance(), credential)
                .setApplicationName(APPLICATION_NAME)
                .build();

        File fileMetadata = new File()
                .setName(presentation.getName())
                .setMimeType(GOOGLE_SLIDES_MIME_TYPE);
        ByteArrayContent fileData = new ByteArrayContent(PPTX_MIME_TYPE, pptxInBytes);

        try {
            return drive.files().create(fileMetadata, fileData)
                    .setFields("id")
                    .execute();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
