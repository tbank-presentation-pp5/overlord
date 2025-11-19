package ru.pp.gamma.overlord.export.pptx;

import lombok.RequiredArgsConstructor;
import org.apache.poi.xslf.usermodel.XMLSlideShow;
import org.apache.poi.xslf.usermodel.XSLFSlide;
import org.apache.poi.xslf.usermodel.XSLFSlideLayout;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.pp.gamma.overlord.common.util.MinioRepository;
import ru.pp.gamma.overlord.presentation.entity.Presentation;
import ru.pp.gamma.overlord.presentation.entity.PresentationSlide;

import java.io.*;

@RequiredArgsConstructor
@Service
public class PPTXExportService {

    private static final String TEMPLATE_OBJECT_NAME = "layout.pptx";
    private static final String ID_LAYOUT_PREFIX = "rId";

    @Value("${minio.util-bucket}")
    private String utilBucket;

    private final MinioRepository minioRepository;
    private final PPTXSlideCreator pptxSlideCreator;

    public byte[] export(Presentation presentation) {
        try (XMLSlideShow ppt = openTemplate()) {
            return processPresentation(presentation, ppt);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private byte[] processPresentation(Presentation presentation, XMLSlideShow ppt) {
        cleanTemplate(ppt);
        presentation.getSlides().forEach(slide -> createSlide(slide, ppt));
        return pptToByte(ppt);
    }

    private void createSlide(PresentationSlide slide, XMLSlideShow ppt) {
        XSLFSlideLayout layout = getSlideLayoutById(ppt, slide.getTemplate().getLayoutId());
        XSLFSlide pptSlide = ppt.createSlide(layout);
        pptxSlideCreator.fill(slide, pptSlide);
    }

    private XMLSlideShow openTemplate() {
        byte[] template = minioRepository.get(utilBucket, TEMPLATE_OBJECT_NAME)
                .orElseThrow(() -> new RuntimeException("Template not found!"));

        try (InputStream stream = new ByteArrayInputStream(template)) {
            return new XMLSlideShow(stream);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void cleanTemplate(XMLSlideShow ppt) {
        int count = ppt.getSlides().size();
        for (int i = 0; i < count; i++) {
            ppt.removeSlide(0);
        }
    }

    private byte[] pptToByte(XMLSlideShow ppt) {
        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            ppt.write(out);
            return out.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private XSLFSlideLayout getSlideLayoutById(XMLSlideShow ppt, int id) {
        return (XSLFSlideLayout) ppt.getSlideMasters().getFirst()
                .getRelationById(ID_LAYOUT_PREFIX + id);
    }
}
