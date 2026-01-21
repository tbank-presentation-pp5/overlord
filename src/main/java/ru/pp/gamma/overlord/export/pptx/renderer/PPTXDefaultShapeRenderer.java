package ru.pp.gamma.overlord.export.pptx.renderer;

import lombok.RequiredArgsConstructor;
import org.apache.poi.sl.usermodel.PictureData;
import org.apache.poi.xslf.usermodel.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.pp.gamma.overlord.common.util.MinioRepository;
import ru.pp.gamma.overlord.presentation.entity.PresentationSlide;
import ru.pp.gamma.overlord.presentation.entity.SlideField;

import java.io.IOException;

@RequiredArgsConstructor
@Component
public class PPTXDefaultShapeRenderer implements PPTXRenderer {

    @Value("${minio.bucket}")
    private String imagesBucket;

    private final MinioRepository minioRepository;

    @Override
    public boolean canRender(PresentationSlide slide) {
        return true;
    }

    @Override
    public void render(PresentationSlide slide, XSLFSlide pptSlide) {
        slide.getFields().forEach(field -> renderShape(field, pptSlide));
    }

    private void renderShape(SlideField field, XSLFSlide pptSlide) {
        XSLFShape shape = pptSlide.getShapes().stream()
                .filter(shapeElement -> shapeElement.getShapeName().equals(field.getTemplate().getShapeName()))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Shape not found!"));

        switch (field.getTemplate().getContentType()) {
            case TEXT -> renderText(field, shape);
            case IMAGE -> renderImage(field, shape, pptSlide);
        }
    }

    private void renderText(SlideField field, XSLFShape shape) {
        XSLFTextBox textShape = (XSLFTextBox) shape;
        textShape.getTextParagraphs()
                .getFirst()
                .getTextRuns()
                .getFirst()
                .setText(field.getValue().asText());
    }

    private void renderImage(SlideField field, XSLFShape shape, XSLFSlide pptSlide) {
        XSLFPictureShape imageShape = ((XSLFPictureShape) shape);

        byte[] imageData = minioRepository.get(imagesBucket, field.getImage().getName())
                .orElseThrow(() -> new RuntimeException("image not found"));

        XSLFPictureData newPictureData = pptSlide.getSlideShow().addPicture(imageData, PictureData.PictureType.JPEG);
        XSLFPictureShape newImage = pptSlide.createPicture(newPictureData);
        copyImageProperties(imageShape, newImage);

        try {
            imageShape.getPictureData()
                    .setData(imageData);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        pptSlide.removeShape(imageShape);
    }

    private void copyImageProperties(XSLFPictureShape source, XSLFPictureShape target) {
        target.setAnchor(source.getAnchor());
        target.setRotation(source.getRotation());
        target.setFlipHorizontal(source.getFlipHorizontal());
        target.setFlipVertical(source.getFlipVertical());
        target.setLineColor(source.getLineColor());
        target.setLineWidth(source.getLineWidth());
        target.setLineDash(source.getLineDash());
    }

}
