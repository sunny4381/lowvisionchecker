package org.ss_proj.lowvision;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.kklisura.cdt.launch.ChromeLauncher;
import com.github.kklisura.cdt.services.ChromeDevToolsService;
import com.github.kklisura.cdt.services.ChromeService;
import com.github.kklisura.cdt.services.types.ChromeTab;
import com.google.common.io.Files;
import org.eclipse.actf.visualization.engines.lowvision.LowVisionException;
import org.eclipse.actf.visualization.engines.lowvision.LowVisionType;
import org.eclipse.actf.visualization.engines.lowvision.image.ImageException;
import org.eclipse.actf.visualization.eval.problem.IProblemItem;
import picocli.CommandLine;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.List;
import java.util.concurrent.Callable;

public class App implements Callable<Integer> {
    private static final ObjectMapper Mapper = new ObjectMapper();

    @CommandLine.Parameters(index = "0", description = "The url to check")
    private String url = null;

    @CommandLine.Option(names = "--no-headless", negatable = true, description = "specifies to execute with headless or not. default is headless")
    private boolean headless = true;

//    @CommandLine.Option(names = {"-b", "--browser"}, description = "specifies browser. chrome, firefox or edge. default is chrome")
//    private String browser = "chrome";

    @CommandLine.Option(names = {"-o", "--output-report"}, description = "specifies output report file. default is a.json")
    private String outputReportFilepath = "a.json";

    @CommandLine.Option(names = "--output-image", description = "specifies output image file. default doesn't output image file.")
    private String outputImageFilepath = null;

    @CommandLine.Option(names = "--source-image", description = "specifies output source image file. default doesn't output source image file.")
    private String sourceImageFilepath = null;

    @CommandLine.Option(names = "--no-lowvision-eyesight", negatable = true)
    private boolean lowvisionEyesight = true;

    @CommandLine.Option(names = "--lowvision-eyesight-degree")
    private float lowvisionEyesightDegree = 0.5f;

    @CommandLine.Option(names = "--no-lowvision-cvd", negatable = true)
    private boolean lowvisionCVD = true;

    @CommandLine.Option(names = "--lowvision-cvd-type")
    private int lowvisionCVDType = 2;

    @CommandLine.Option(names = "--no-lowvision-color-filter", negatable = true)
    private boolean lowvisionColorFilter = true;

    @CommandLine.Option(names = "--lowvision-color-filter-degree")
    private float lowvisionColorFilterDegree = 0.8f;

    public static void main(String[] args) {
        int exitCode = new CommandLine(new App()).execute(args);
        System.exit(exitCode);
    }

    @Override
    public Integer call() throws Exception {
        this.doCheck();

        return 0;
    }

    private LowVisionType createLowVisionType() throws LowVisionException {
        LowVisionType lowVisionType = new LowVisionType();

        if (this.lowvisionEyesight) {
            lowVisionType.setEyesight(this.lowvisionEyesight);
            lowVisionType.setEyesightDegree(this.lowvisionEyesightDegree);
        }
        if (this.lowvisionCVD) {
            lowVisionType.setCVD(this.lowvisionCVD);
            lowVisionType.setCVDType(this.lowvisionCVDType);
        }
        if (this.lowvisionColorFilter) {
            lowVisionType.setColorFilter(this.lowvisionColorFilter);
            lowVisionType.setColorFilterDegree(this.lowvisionColorFilterDegree);
        }

        return lowVisionType;
    }

    private void doCheck() throws LowVisionException, IOException, ImageException {
        try (ChromeLauncher launcher = new ChromeLauncher()) {
            final ChromeService chromeService = launcher.launch(this.headless);
            final ChromeTab tab = chromeService.createTab();
            final ChromeDevToolsService devToolsService = chromeService.createDevToolsService(tab);

            Browser browser = new Browser(devToolsService, tab.getId());
            browser.navigate(this.url);

            Checker checker = new Checker(browser, this.url, createLowVisionType());
            checker.run();

            outputResults(checker.getProblemList());

            if (this.outputImageFilepath != null && !this.outputImageFilepath.isEmpty()) {
                outputImage(checker.getLowvisionImage());
            }

            if (this.sourceImageFilepath != null && !this.sourceImageFilepath.isEmpty()) {
                outputSourceImage(checker.getSourceImage());
            }
        }
    }

    private void outputResults(List<IProblemItem> problemItems) throws FileNotFoundException, UnsupportedEncodingException, JsonProcessingException {
        try (PrintWriter writer = new PrintWriter(new File(this.outputReportFilepath), "UTF-8")) {
            for (IProblemItem problemItem : problemItems) {
                writer.println(Mapper.writeValueAsString(problemItem));
            }
        }
    }

    private void outputImage(BufferedImage image) throws IOException {
        String format = Files.getFileExtension(this.outputImageFilepath);
        ImageIO.write(image, format, new File(this.outputImageFilepath));
    }

    private void outputSourceImage(BufferedImage image) throws IOException {
        String format = Files.getFileExtension(this.sourceImageFilepath);
        ImageIO.write(image, format, new File(this.sourceImageFilepath));
    }
}
