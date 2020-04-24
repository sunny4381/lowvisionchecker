package org.ss_proj.lowvision;

import org.eclipse.actf.visualization.eval.ICheckerInfoProvider;

import java.io.InputStream;
import java.util.ResourceBundle;

public class HtmlCheckerInfoProvider implements ICheckerInfoProvider {
    public HtmlCheckerInfoProvider() {
    }

    @Override
    public InputStream[] getGuidelineInputStreams() {
        return new InputStream[0];
    }

    @Override
    public InputStream[] getCheckItemInputStreams() {
        InputStream is = this.getClass().getResourceAsStream("checkitem.xml");
        return new InputStream[] { is };
    }

    @Override
    public ResourceBundle getDescriptionRB() {
        return null;
    }
}
