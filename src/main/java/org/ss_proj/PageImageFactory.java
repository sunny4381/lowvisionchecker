package org.ss_proj;

import org.eclipse.actf.visualization.internal.engines.lowvision.image.IInt2D;
import org.eclipse.actf.visualization.internal.engines.lowvision.image.Int2D;
import org.eclipse.actf.visualization.internal.engines.lowvision.image.PageImage;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class PageImageFactory {
    public static PageImage loadFromPng(final String fileName) throws IOException {
        return loadFromPng(new File(fileName));
    }

    public static PageImage loadFromPng(final File file) throws IOException {
        final BufferedImage image = ImageIO.read(file);
        final IInt2D int2dWhole = loadImageData(image);
        return new PageImage(int2dWhole, false);
    }

    private static IInt2D loadImageData(final BufferedImage image) {
        final int imageWidth = image.getWidth();
        final int imageHeight = image.getHeight();
        Int2D destImage = new Int2D(imageWidth, imageHeight);
        for (int y = 0; y < imageHeight; y++) {
            for (int x = 0; x < imageWidth; x++) {
                int argb = image.getRGB(x, y);
                int rgb = argb & 0x00ffffff;
                destImage.getData()[imageHeight - y - 1][x] = rgb;
            }
        }
        return destImage;
    }
}
