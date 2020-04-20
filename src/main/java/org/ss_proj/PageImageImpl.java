package org.ss_proj;

import org.eclipse.actf.model.ui.ImagePositionInfo;
import org.eclipse.actf.visualization.engines.lowvision.LowVisionIOException;
import org.eclipse.actf.visualization.engines.lowvision.LowVisionType;
import org.eclipse.actf.visualization.engines.lowvision.image.IPageImage;
import org.eclipse.actf.visualization.engines.lowvision.image.ImageException;
import org.eclipse.actf.visualization.eval.problem.IProblemItem;
import org.eclipse.actf.visualization.internal.engines.lowvision.problem.LowVisionProblemException;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;

public class PageImageImpl implements IPageImage, AutoCloseable {
    private final BufferedImage image;

    public PageImageImpl(final BufferedImage image) {
        this.image = image;
    }

    public static PageImageImpl loadFromPng(final String fileName) throws IOException {
        return loadFromPng(new File(fileName));
    }

    public static PageImageImpl loadFromPng(final File file) throws IOException {
        return new PageImageImpl(ImageIO.read(file));
    }

    @Override
    public int getWidth() {
        return this.image.getWidth();
    }

    @Override
    public int getHeight() {
        return this.image.getHeight();
    }

    @Override
    public void extractCharacters() throws ImageException {
        throw new NotImplementedException();
    }

    @Override
    public ImagePositionInfo[] getInteriorImagePosition() {
        throw new NotImplementedException();
    }

    @Override
    public void setInteriorImagePosition(ImagePositionInfo[] infoArray) {
        throw new NotImplementedException();
    }

    @Override
    public boolean hasInteriorImageArraySet() {
        throw new NotImplementedException();
    }

    @Override
    public List<IProblemItem> checkCharacters(LowVisionType type, String urlS, int frameId) throws ImageException, LowVisionProblemException {
        throw new NotImplementedException();
    }

    @Override
    public BufferedImage getBufferedImage() {
        return this.image;
    }

    @Override
    public void writeToBMPFile(String _fileName) throws LowVisionIOException {
        throw new NotImplementedException();
    }

    @Override
    public void writeToBMPFile(String _fileName, int _bitCount) throws LowVisionIOException {
        throw new NotImplementedException();
    }

    @Override
    public void close() {
    }
}
