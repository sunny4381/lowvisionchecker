package org.ss_proj;

import org.eclipse.actf.model.ui.editor.browser.ICurrentStyles;
import org.eclipse.swt.graphics.Rectangle;
import org.w3c.dom.Element;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.net.MalformedURLException;
import java.net.URL;

public class CurrentStylesImpl implements ICurrentStyles {
    private String xpath;
    private String tagName;
    private Rect rect;
    private Style style;
    private Style computedStyle;
    private String href;
    private String[] texts;

    public CurrentStylesImpl() {
    }

    @Override
    public String getXPath() {
        return this.xpath;
    }

    public void setXPath(String xpath) {
        this.xpath = xpath;
    }

    @Override
    public String getTagName() {
        return this.tagName;
    }

    public void setTagName(String tagName) {
        this.tagName = tagName;
    }

    public Rect getRect() {
        return rect;
    }

    public void setRect(Rect rect) {
        this.rect = rect;
    }

    public Style getStyle() {
        return this.style;
    }

    public void setStyle(Style style) {
        this.style = style;
    }

    public Style getComputedStyle() {
        return computedStyle;
    }

    public void setComputedStyle(Style computedStyle) {
        this.computedStyle = computedStyle;
    }

    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
    }

    public String[] getTexts() {
        return this.texts;
    }

    public void setTexts(String[] value) {
        this.texts = value;
    }

    @Override
    public Rectangle getRectangle() {
        if (this.rect == null) {
            return null;
        }

        return new Rectangle(
                this.rect.getX().intValue(), this.rect.getY().intValue(),
                this.rect.getWidth().intValue(), this.rect.getHeight().intValue());
    }

    @Override
    public boolean isLink() {
        return this.href != null;
    }

    @Override
    public URL getLinkURL() {
        if (this.href == null) {
            return null;
        }

        try {
            return new URL(this.href);
        } catch (MalformedURLException e) {
            return null;
        }
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
        return this.computedStyle.fontSize;
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
    public String getComputedColor() {
        return this.computedStyle.color;
    }

    @Override
    public String getComputedBackgroundColor() {
        return this.computedStyle.backgroundColor;
    }

    @Override
    public String getComputedBackgroundImage() {
        return this.computedStyle.backgroundImage;
    }

    @Override
    public String getOpacity() {
        return this.style.opacity;
    }

    @Override
    public boolean hasChildText() {
        return this.texts != null && this.texts.length > 0;
    }

    @Override
    public String[] getChildTexts() {
        return this.texts;
    }

    @Override
    public boolean hasDescendantTextWithBGImage() {
        return false;
    }

    @Override
    public String[] getDescendantTextsWithBGImage() {
        return null;
    }

    @Override
    public Element getElement() {
//        throw new NotImplementedException();
        return null;
    }
}
