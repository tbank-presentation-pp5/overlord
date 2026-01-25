package ru.pp.gamma.overlord.export.pptx.renderer;

import org.apache.poi.xslf.usermodel.XSLFAutoShape;
import org.apache.poi.xslf.usermodel.XSLFShape;
import org.apache.poi.xslf.usermodel.XSLFSlide;
import org.springframework.stereotype.Component;
import ru.pp.gamma.overlord.presentation.entity.PresentationSlide;

@Component
public class PPTXPageNumberRenderer implements PPTXRenderer {

    private static final String SHAPE_PAGE_NUMBER = "page_number";

    @Override
    public boolean canRender(PresentationSlide slide) {
        return slide.getTemplate().isNeedPageNumber();
    }

    @Override
    public void render(PresentationSlide slide, XSLFSlide pptSlide) {
        XSLFShape pptShape = pptSlide.getShapes().stream()
                .filter(shape -> shape.getShapeName().equals(SHAPE_PAGE_NUMBER))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Shape with page number not found"));

        ((XSLFAutoShape) pptShape).setText(slide.getOrderNumber().toString());
    }

}
