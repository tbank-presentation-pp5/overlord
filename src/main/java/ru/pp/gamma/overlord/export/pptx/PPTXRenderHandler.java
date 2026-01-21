package ru.pp.gamma.overlord.export.pptx;

import org.apache.poi.xslf.usermodel.XSLFSlide;
import org.springframework.stereotype.Component;
import ru.pp.gamma.overlord.export.pptx.renderer.PPTXRenderer;
import ru.pp.gamma.overlord.presentation.entity.PresentationSlide;

import java.util.List;

@Component
public class PPTXRenderHandler {

    private final List<PPTXRenderer> renderers;

    public PPTXRenderHandler(List<PPTXRenderer> renderers) {
        this.renderers = renderers;
    }

    public void render(PresentationSlide slide, XSLFSlide pptSlide) {
        renderers.stream()
                .filter(r -> r.canRender(slide))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Renderer not found"))
                .render(slide, pptSlide);
    }
}
