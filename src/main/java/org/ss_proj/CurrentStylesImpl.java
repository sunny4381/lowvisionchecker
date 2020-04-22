package org.ss_proj;

import org.eclipse.actf.model.ui.editor.browser.ICurrentStyles;
import org.eclipse.swt.graphics.Rectangle;
import org.w3c.dom.Element;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.Map;

public class CurrentStylesImpl implements ICurrentStyles {
    private String xpath;
    private String tagName;
    private Rect rect;
//    private Style style;
    private Style computedStyle;
    private String href;
    private String[] texts;

    public CurrentStylesImpl() {
    }

    public static CurrentStylesImpl convertFrom(Map<String, ?> map) {
        CurrentStylesImpl ret = new CurrentStylesImpl();

        map.forEach((key, value) -> {
            switch (key) {
                case "xpath":
                    ret.setXPath((String) value);
                    break;
                case "tagName":
                    ret.setTagName((String) value);
                    break;
                case "rect":
                    ret.setRect(Rect.convertFrom((Map<String, Object>) value));
                    break;
//                case "style":
//                    ret.setStyle(Style.convertFrom((Map<String, Object>) value));
//                    break;
                case "computedStyle":
                    ret.setComputedStyle(Style.convertFrom((Map<String, Object>) value));
                    break;
                case "href":
                    ret.setHref((String) value);
                    break;
                case "texts":
                    String[] array = null;
                    if (value != null) {
                        array = Arrays.stream((Object[])value).toArray(String[]::new);
                    }
                    ret.setTexts(array);
                    break;
            }
        });

        return ret;
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

//    public Style getStyle() {
//        return this.style;
//    }
//
//    public void setStyle(Style style) {
//        this.style = style;
//    }

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
        return this.computedStyle.backgroundColor;
    }

    @Override
    public String getBackgroundRepeat() {
        return this.computedStyle.backgroundRepeat;
    }

    @Override
    public String getBackgroundImage() {
        return this.computedStyle.backgroundImage;
    }

    @Override
    public String getColor() {
        return this.computedStyle.color;
    }

    @Override
    public String getDisplay() {
        return this.computedStyle.display;
    }

    @Override
    public String getFontFamily() {
        return this.computedStyle.fontFamily;
    }

    @Override
    public String getFontSize() {
        return this.computedStyle.fontSize;
    }

    @Override
    public String getFontStyle() {
        return this.computedStyle.fontStyle;
    }

    @Override
    public String getFontVariant() {
        return this.computedStyle.fontVariant;
    }

    @Override
    public String getLetterSpacing() {
        return this.computedStyle.letterSpacing;
    }

    @Override
    public String getLineHeight() {
        return this.computedStyle.lineHeight;
    }

    @Override
    public String getPosition() {
        return this.computedStyle.position;
    }

    @Override
    public String getTextAlign() {
        return this.computedStyle.textAlign;
    }

    @Override
    public String getTextDecoration() {
        return this.computedStyle.textDecoration;
    }

    @Override
    public String getVisibility() {
        return this.computedStyle.visibility;
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
        return this.computedStyle.opacity;
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
