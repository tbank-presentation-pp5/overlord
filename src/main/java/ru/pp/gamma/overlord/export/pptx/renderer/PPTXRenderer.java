package ru.pp.gamma.overlord.export.pptx.renderer;

import org.apache.poi.xslf.usermodel.XSLFSlide;
import ru.pp.gamma.overlord.presentation.entity.PresentationSlide;

public interface PPTXRenderer {
    boolean canRender(PresentationSlide slide);

    void render(PresentationSlide slide, XSLFSlide pptSlide);
}
