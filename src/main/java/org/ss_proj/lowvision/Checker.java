package org.ss_proj.lowvision;

import org.eclipse.actf.model.ui.ImagePositionInfo;
import org.eclipse.actf.model.ui.editor.browser.ICurrentStyles;
import org.eclipse.actf.visualization.engines.lowvision.LowVisionType;
import org.eclipse.actf.visualization.engines.lowvision.PageEvaluation;
import org.eclipse.actf.visualization.engines.lowvision.image.IPageImage;
import org.eclipse.actf.visualization.eval.problem.IProblemItem;
import org.eclipse.actf.visualization.lowvision.util.LowVisionUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class Checker {
    private final Browser browser;
    private final String address;
    private final LowVisionType lowVisionType;

    private IPageImage[] framePageImage;
    private ImagePositionInfo[][] imageInfoInHtmlArray;
    private ArrayList<Map<String, ICurrentStyles>> styleInfoArray;
    private List<IProblemItem> lowvisionProblemList;

    public Checker(final Browser browser, final String address, final LowVisionType lowVisionType) {
        this.browser = browser;
        this.address = address;

        this.framePageImage = new IPageImage[1];
        this.imageInfoInHtmlArray = new ImagePositionInfo[1][];
        this.styleInfoArray = new ArrayList<>(1);
        this.styleInfoArray.add(Collections.emptyMap());

        this.lowVisionType = lowVisionType;
    }

    public List<IProblemItem> getProblemList() {
        return this.lowvisionProblemList;
    }

    public void run() throws IOException {
        final byte[] screenshot = browser.takeScreenshot();
        final int frameId = 0;
        final int lastFrame = 0;

        framePageImage[frameId] = PageImageFactory.loadFromPng(screenshot);

        imageInfoInHtmlArray[frameId] = browser.getAllImagePosition();
        styleInfoArray.set(frameId, browser.getStyleInfo().getCurrentStyles());

        if (lastFrame > 1) { // TODO frameURL.length?
            imageInfoInHtmlArray[frameId] = LowVisionUtil
                    .trimInfoImageInHtml(imageInfoInHtmlArray[frameId],
                            framePageImage[frameId].getHeight());
            styleInfoArray.set(frameId, LowVisionUtil
                    .trimStyleInfoArray(styleInfoArray.get(frameId),
                            framePageImage[frameId].getHeight()));
        }

        PageEvaluation targetPage = new PageEvaluation(framePageImage[frameId]);
        targetPage.setInteriorImagePosition(imageInfoInHtmlArray[frameId]);
        targetPage.setCurrentStyles(styleInfoArray.get(frameId));

        lowvisionProblemList = targetPage.check(lowVisionType, address, frameId);

//        // TODO frames
//        try {
//            removeTempFile(reportFile);
//            reportFile = LowVisionVizPlugin.createTempFile(
//                    PREFIX_REPORT, SUFFIX_HTML);
//            // TODO modelservice type
//            if (webBrowser != null) {
//                removeTempFile(reportImageFile);
//                reportImageFile = LowVisionVizPlugin.createTempFile(
//                        PREFIX_REPORT, SUFFIX_BMP);
//                targetPage
//                        .generateReport(reportFile.getParent(),
//                                reportFile.getName(),
//                                reportImageFile.getName(),
//                                lowvisionProblemList);
//            } else {// current lv mode doesn't support ODF
//                reportImageFile = null;
//                targetPage.unsupportedModeReport(reportFile);
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

        targetPage = null;

//        checkResult.addProblemItems(lowvisionProblemList);
    }
}