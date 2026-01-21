package ru.pp.gamma.overlord.export.pptx;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;
import org.apache.poi.sl.usermodel.PictureData;
import org.apache.poi.xslf.usermodel.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.pp.gamma.overlord.common.util.MinioRepository;
import ru.pp.gamma.overlord.presentation.entity.PresentationSlide;
import ru.pp.gamma.overlord.presentation.entity.SlideField;
import ru.pp.gamma.overlord.presentation.template.entity.FontColor;
import ru.pp.gamma.overlord.presentation.template.entity.FontStyle;

import java.awt.*;
import java.awt.geom.Rectangle2D;

import static ru.pp.gamma.overlord.presentation.template.entity.SlideFieldContentType.IMAGE;
import static ru.pp.gamma.overlord.presentation.template.entity.SlideFieldContentType.TEXT;

@RequiredArgsConstructor
@Component
public class PPTXSlideCreator {

    @Value("${minio.bucket}")
    private String imagesBucket;

    private final MinioRepository minioRepository;

    public void fill(PresentationSlide slide, XSLFSlide pptSlide) {
        cleanShapes(pptSlide);
        slide.getFields().forEach(field -> createField(field, pptSlide));
    }

    private void createField(SlideField field, XSLFSlide pptSlide) {
        switch (field.getTemplate().getContentType()) {
            case TEXT -> processTextType(field, pptSlide);
            case IMAGE -> processImageField(field, pptSlide);
        }
    }

    private void processTextType(SlideField field, XSLFSlide pptSlide) {
        XSLFTextBox shape = pptSlide.createTextBox();
        XSLFTextParagraph p = shape.getTextParagraphs().getFirst();
        XSLFTextRun r = p.addNewTextRun();

        JsonNode templateText = field.getTemplate().getMeta();

        r.setText(field.getValue().get("text").asText());
//        r.setFontFamily(templateText.getFontFamily());
//        r.setFontSize(templateText.getFontSize());
//        setFontStyle(r, templateText.getFontStyle());
//        setColor(r, templateText.getColor());

        r.getRPr(true).setLang("ru-RU");

//        shape.setAnchor(new Rectangle2D.Double(
//                templateText.getPosition().getX(),
//                templateText.getPosition().getY(),
//                templateText.getPosition().getWidth(),
//                templateText.getPosition().getHeight()
//        ));
    }

    private void processImageField(SlideField field, XSLFSlide pptSlide) {
        byte[] imageData = minioRepository.get(imagesBucket, field.getImage().getName())
                .orElseThrow(() -> new RuntimeException("image not found"));

        XSLFPictureData pd = pptSlide.getSlideShow().addPicture(imageData, PictureData.PictureType.JPEG);
        XSLFPictureShape shape = pptSlide.createPicture(pd);

//        shape.setAnchor(new Rectangle2D.Double(
//                field.getTemplate().getTemplateImage().getPosition().getX(),
//                field.getTemplate().getTemplateImage().getPosition().getY(),
//                field.getTemplate().getTemplateImage().getPosition().getWidth(),
//                field.getTemplate().getTemplateImage().getPosition().getHeight()
//        ));
    }

    private void setFontStyle(XSLFTextRun r, FontStyle style) {
        switch (style) {
            case BOLD -> r.setBold(true);
            case ITALIC -> r.setItalic(true);
        }
    }

    private void setColor(XSLFTextRun r, FontColor color) {
        switch (color) {
            case WHITE -> r.setFontColor(Color.WHITE);
        }
    }

    private void cleanShapes(XSLFSlide pptSlide) {
        XSLFShape[] shapes = pptSlide.getShapes().toArray(new XSLFShape[0]);
        for (XSLFShape shape : shapes) {
            pptSlide.removeShape(shape);
        }
    }
}
