package org.ss_proj;

import org.eclipse.actf.model.ui.IModelService;
import org.eclipse.actf.model.ui.ImagePositionInfo;
import org.eclipse.actf.model.ui.ModelServiceImageCreator;
import org.eclipse.actf.model.ui.editor.browser.ICurrentStyles;
import org.eclipse.actf.model.ui.editor.browser.IWebBrowserACTF;
import org.eclipse.actf.visualization.engines.lowvision.LowVisionType;
import org.eclipse.actf.visualization.engines.lowvision.PageEvaluation;
import org.eclipse.actf.visualization.engines.lowvision.image.IPageImage;
import org.eclipse.actf.visualization.engines.lowvision.image.PageImageFactory;
import org.eclipse.actf.visualization.eval.problem.IProblemItem;
import org.eclipse.actf.visualization.lowvision.util.LowVisionUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class LowVisionChecker {
    private IPageImage[] framePageImage;
    private ImagePositionInfo[][] imageInfoInHtmlArray;
    private ArrayList<Map<String, ICurrentStyles>> styleInfoArray;
    private String dumpImageFile;
    private LowVisionType lowVisionType;
    private List<IProblemItem> lowvisionProblemList;

    public void run(String address) {
        IWebBrowserACTF browser = null;
        IModelService modelService = browser;

        ModelServiceImageCreator imgCreator = new ModelServiceImageCreator(modelService);
        imgCreator.getScreenImageAsBMP(dumpImageFile, true);
        final int frameId = 0;
        final int lastFrame = 0;

        framePageImage[frameId] = PageImageFactory.createPageImage(dumpImageFile);

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
        targetPage
                .setInteriorImagePosition(imageInfoInHtmlArray[frameId]);
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
