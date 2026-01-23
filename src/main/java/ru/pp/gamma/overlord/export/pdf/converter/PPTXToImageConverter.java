package ru.pp.gamma.overlord.export.pdf.converter;

import org.apache.poi.xslf.usermodel.XMLSlideShow;
import org.apache.poi.xslf.usermodel.XSLFSlide;
import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

@Component
public class PPTXToImageConverter {

    /**
     * Конвертирует презентацию в список изображений png
     *
     * @param presentation презентация pptx
     * @param scale        Насколько масштабировать размер изображения
     * @return список изображений. Количество = количество слайдов
     */
    public List<byte[]> convert(byte[] presentation, double scale) {
        try (ByteArrayInputStream inputStream = new ByteArrayInputStream(presentation)) {

            try (XMLSlideShow ppt = new XMLSlideShow(inputStream)) {
                return convert(ppt, scale);
            }

        } catch (IOException e) {
            throw new RuntimeException("IO exception when converting PPTX to Image", e);
        }
    }

    private List<byte[]> convert(XMLSlideShow ppt, double scale) {
        Dimension pageSize = ppt.getPageSize();
        return ppt.getSlides().stream()
                .map(slide -> convertSlide(slide, pageSize, scale))
                .toList();
    }

    private byte[] convertSlide(XSLFSlide slide, Dimension pageSize, double scale) {
        int w = (int) Math.ceil(pageSize.width * scale);
        int h = (int) Math.ceil(pageSize.height * scale);

        BufferedImage img = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);

        Graphics2D g = img.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
        g.setPaint(Color.WHITE);
        g.fillRect(0, 0, w, h);
        g.transform(AffineTransform.getScaleInstance(scale, scale));

        slide.draw(g);
        g.dispose();

        return toBytes(img);
    }

    private byte[] toBytes(BufferedImage img) {
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            ImageIO.write(img, "png", out);

            return out.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException("IO exception when write Image as byte[]", e);
        }
    }

}
