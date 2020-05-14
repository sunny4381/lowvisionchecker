package org.ss_proj.lowvision;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.kklisura.cdt.launch.ChromeArguments;
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
import java.util.Locale;
import java.util.concurrent.Callable;

public class App implements Callable<Integer> {
    private static final ObjectMapper Mapper = new ObjectMapper();

    @CommandLine.Parameters(index = "0", description = "The url to check")
    private String url = null;

    @CommandLine.Option(names = "--lang", description = "specifies lang. default is unspecified(use system default)")
    private Locale lang = Locale.getDefault();

    @CommandLine.Option(names = "--no-browser-headless", negatable = true, description = "specifies to execute with headless or not. default is headless")
    private boolean headless = true;

    @CommandLine.Option(names = "--browser-window-size", description = "specifies the browser window size. default is unspecified (auto)")
    private String windowSize = null;

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
        int exitCode = new CommandLine(new App())
                .registerConverter(Locale.class, s -> new Locale.Builder().setLanguageTag(s).build())
                .execute(args);
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
            final ChromeArguments.Builder argumentsBuilder = ChromeArguments.defaults(this.headless);
            argumentsBuilder.additionalArguments("disable-backgrounding-occluded-windows", true);
            argumentsBuilder.additionalArguments("disable-breakpad", true);
            argumentsBuilder.additionalArguments("disable-dev-shm-usage", true);
            argumentsBuilder.additionalArguments("disable-features", "site-per-process,TranslateUI");
            argumentsBuilder.additionalArguments("disable-ipc-flooding-protection", true);
            argumentsBuilder.additionalArguments("disable-renderer-backgrounding", true);
            argumentsBuilder.additionalArguments("disable-session-crashed-bubble", true);
            argumentsBuilder.additionalArguments("disable-web-security", true);
            argumentsBuilder.additionalArguments("enable-automation", true);
            argumentsBuilder.additionalArguments("enable-features", "NetworkService,NetworkServiceInProcess");
            argumentsBuilder.additionalArguments("force-color-profile", "srgb");
            argumentsBuilder.additionalArguments("keep-alive-for-test", true);
            argumentsBuilder.additionalArguments("lang", this.lang.toLanguageTag());
            argumentsBuilder.additionalArguments("password-store", "basic");
            argumentsBuilder.additionalArguments("use-mock-keychain", true);
            if (this.windowSize != null && !this.windowSize.isEmpty()) {
                argumentsBuilder.additionalArguments("window-size", this.windowSize);
            }
            final ChromeService chromeService = launcher.launch(argumentsBuilder.build());
            final ChromeTab tab;
            if (chromeService.getTabs().size() > 0) {
                tab = chromeService.getTabs().get(0);
                chromeService.activateTab(tab);
            } else {
                tab = chromeService.createTab();
            }
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
