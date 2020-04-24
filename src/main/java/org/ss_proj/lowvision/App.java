package org.ss_proj.lowvision;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.kklisura.cdt.launch.ChromeLauncher;
import com.github.kklisura.cdt.services.ChromeDevToolsService;
import com.github.kklisura.cdt.services.ChromeService;
import com.github.kklisura.cdt.services.types.ChromeTab;
import org.eclipse.actf.visualization.engines.lowvision.LowVisionException;
import org.eclipse.actf.visualization.engines.lowvision.LowVisionType;
import org.eclipse.actf.visualization.eval.problem.IProblemItem;
import picocli.CommandLine;

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

    @CommandLine.Option(names = {"-o", "--output"}, description = "specifies output file. default is a.json")
    private String output = "a.json";

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
        List<IProblemItem> problemItems = this.doCheck();

        outputResults(problemItems);

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

    private List<IProblemItem> doCheck() throws LowVisionException, IOException {
        try (ChromeLauncher launcher = new ChromeLauncher()) {
            final ChromeService chromeService = launcher.launch(this.headless);
            final ChromeTab tab = chromeService.createTab();
            final ChromeDevToolsService devToolsService = chromeService.createDevToolsService(tab);

            Browser browser = new Browser(devToolsService, tab.getId());
            browser.navigate(this.url);


            Checker checker = new Checker(browser, this.url, createLowVisionType());
            checker.run();

            return checker.getProblemList();
        }
    }

    private void outputResults(List<IProblemItem> problemItems) throws FileNotFoundException, UnsupportedEncodingException, JsonProcessingException {
        try (PrintWriter writer = new PrintWriter(new File(this.output), "UTF-8")) {
            for (IProblemItem problemItem : problemItems) {
                writer.println(Mapper.writeValueAsString(problemItem));
            }
        }
    }
}
