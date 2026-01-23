package ru.pp.gamma.overlord.export.pdf.converter;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

@Component
public class ImagesToPdfConverter {

    /**
     * Ковертирует список изображений в pdf файл
     *
     * @param images список изображений
     * @param scale  число, на которое уменьшаем полученные изображения
     * @return pdf в байтах
     */
    public byte[] convert(List<byte[]> images, double scale) {
        try (PDDocument pdf = new PDDocument()) {

            images.forEach(image -> drawImageInNewPage(pdf, image, scale));

            ByteArrayOutputStream out = new ByteArrayOutputStream();
            pdf.save(out);
            return out.toByteArray();

        } catch (IOException e) {
            throw new RuntimeException("Failed to convert images to pdf", e);
        }
    }

    private void drawImageInNewPage(PDDocument pdf, byte[] image, double scale) {
        try {
            PDImageXObject img = PDImageXObject.createFromByteArray(pdf, image, null);

            float widthPt = (float) (img.getWidth() / scale);
            float heightPt = (float) (img.getHeight() / scale);

            PDRectangle mediaBox = new PDRectangle(widthPt, heightPt);
            PDPage page = new PDPage(mediaBox);
            pdf.addPage(page);

            try (var content = new PDPageContentStream(pdf, page)) {
                content.drawImage(img, 0, 0, widthPt, heightPt);
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to draw image in pdf", e);
        }
    }

}
