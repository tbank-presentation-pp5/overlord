package ru.pp.gamma.overlord.export.pptx.renderer;

import lombok.RequiredArgsConstructor;
import org.apache.poi.sl.usermodel.PictureData;
import org.apache.poi.xddf.usermodel.chart.XDDFChartData;
import org.apache.poi.xddf.usermodel.chart.XDDFPieChartData;
import org.apache.poi.xslf.usermodel.*;
import org.openxmlformats.schemas.presentationml.x2006.main.impl.CTPictureImpl;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.pp.gamma.overlord.common.util.MinioRepository;
import ru.pp.gamma.overlord.export.pptx.chart.PPTXChartPieCreator;
import ru.pp.gamma.overlord.presentation.entity.PresentationSlide;
import ru.pp.gamma.overlord.presentation.entity.SlideField;

import java.util.List;

@RequiredArgsConstructor
@Component
public class PPTXDefaultShapeRenderer implements PPTXRenderer {

    @Value("${minio.bucket}")
    private String imagesBucket;

    private final MinioRepository minioRepository;
    private final PPTXChartPieCreator pptxChartPieCreator;

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
                .orElseThrow(() -> new RuntimeException("Shape not found: " + field.getTemplate().getShapeName()));

        switch (field.getTemplate().getContentType()) {
            case TEXT -> renderText(field, shape);
            case IMAGE -> renderImage(field, shape, pptSlide);
            case CHART -> renderChart(field, shape);
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


        // Создаем новый PictureData для каждого изображения
        XSLFPictureData newPictureData = pptSlide.getSlideShow().addPicture(imageData, PictureData.PictureType.JPEG);

        // Получаем новый relation ID для этого слайда
        String relId = pptSlide.addRelation(null, XSLFRelation.IMAGES, newPictureData).getRelationship().getId();

        // Обновляем ссылку на изображение в XML, сохраняя все остальные свойства
        CTPictureImpl xmlPicture = (CTPictureImpl) imageShape.getXmlObject();
        if (xmlPicture.getBlipFill() != null && xmlPicture.getBlipFill().getBlip() != null) {
            xmlPicture.getBlipFill().getBlip().setEmbed(relId);
        }

    }

    private void renderChart(SlideField field, XSLFShape shape) {
        XSLFGraphicFrame frame = (XSLFGraphicFrame) shape;
        XSLFChart chart = frame.getChart();
        List<XDDFChartData> chartSeries = chart.getChartSeries();

        XDDFChartData chartData = chartSeries.getFirst();
        if (chartData instanceof XDDFPieChartData) {
            pptxChartPieCreator.create(field, chart);
        }
    }
}
