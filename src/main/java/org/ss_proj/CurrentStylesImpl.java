package org.ss_proj;

import com.github.kklisura.cdt.protocol.types.dom.Node;
import org.eclipse.actf.model.ui.editor.browser.ICurrentStyles;
import org.eclipse.swt.graphics.Rectangle;
import org.w3c.dom.Element;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.net.URL;

public class CurrentStylesImpl implements ICurrentStyles {
    private final Node node;
    private final String xpath;
    private final Rect nodeRect;
    private final Style style;

    public CurrentStylesImpl(final Node node, final String xpath, final Rect nodeRect, final Style style) {
        this.node = node;
        this.xpath = xpath;
        this.nodeRect = nodeRect;
        this.style = style;
    }

    @Override
    public String getXPath() {
        return this.xpath;
    }

    @Override
    public String getTagName() {
        return node.getNodeName();
    }

    @Override
    public Rectangle getRectangle() {
        return new Rectangle(nodeRect.getX().intValue(), nodeRect.getY().intValue(), nodeRect.getWidth().intValue(), nodeRect.getHeight().intValue());
    }

    @Override
    public boolean isLink() {
        if (!"a".equalsIgnoreCase(this.node.getNodeName())) {
            return false;
        }

        String href = null;
        for (String attribute : this.node.getAttributes()) {

        }

//        return href != null;
        throw new NotImplementedException();
    }

    @Override
    public URL getLinkURL() {
//        return null;
        throw new NotImplementedException();
    }

    @Override
    public String getBackgroundColor() {
        return this.style.backgroundColor;
    }

    @Override
    public String getBackgroundRepeat() {
        return this.style.backgroundRepeat;
    }

    @Override
    public String getBackgroundImage() {
        return this.style.backgroundImage;
    }

    @Override
    public String getColor() {
        return this.style.color;
    }

    @Override
    public String getDisplay() {
        return this.style.display;
    }

    @Override
    public String getFontFamily() {
        return this.style.fontFamily;
    }

    @Override
    public String getFontSize() {
        return this.style.fontSize;
    }

    @Override
    public String getFontStyle() {
        return this.style.fontStyle;
    }

    @Override
    public String getFontVariant() {
        return this.style.fontVariant;
    }

    @Override
    public String getLetterSpacing() {
        return this.style.letterSpacing;
    }

    @Override
    public String getLineHeight() {
        return this.style.lineHeight;
    }

    @Override
    public String getPosition() {
        return this.style.position;
    }

    @Override
    public String getTextAlign() {
        return this.style.textAlign;
    }

    @Override
    public String getTextDecoration() {
        return this.style.textDecoration;
    }

    @Override
    public String getVisibility() {
        return this.style.visibility;
    }

    @Override
    public Element getElement() {
        throw new NotImplementedException();
    }

    @Override
    public String getComputedColor() {
        return this.getColor();
    }

    @Override
    public String getComputedBackgroundColor() {
        return this.getBackgroundColor();
    }

    @Override
    public String getComputedBackgroundImage() {
        return this.getBackgroundImage();
    }

    @Override
    public String getOpacity() {
        return this.style.opacity;
    }

    @Override
    public boolean hasChildText() {
        throw new NotImplementedException();
    }

    @Override
    public boolean hasDescendantTextWithBGImage() {
        throw new NotImplementedException();
    }

    @Override
    public String[] getChildTexts() {
        throw new NotImplementedException();
    }

    @Override
    public String[] getDescendantTextsWithBGImage() {
        throw new NotImplementedException();
    }
}
