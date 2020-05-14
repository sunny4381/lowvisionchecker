package org.ss_proj.htmlchecker;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.kklisura.cdt.launch.ChromeArguments;
import com.github.kklisura.cdt.launch.ChromeLauncher;
import com.github.kklisura.cdt.services.ChromeDevToolsService;
import com.github.kklisura.cdt.services.ChromeService;
import com.github.kklisura.cdt.services.types.ChromeTab;
import org.eclipse.actf.visualization.eval.problem.IProblemItem;
import org.ss_proj.lowvision.Browser;
import picocli.CommandLine;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.Callable;

@CommandLine.Command(name = "htmlchecker", description = "html accessibility checker")
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

    @CommandLine.Option(names = {"-o", "--output-report"}, description = "specifies output report file. default is a.json")
    private String outputReportFilepath = "a.json";

    public static void main(String[] args) {
        int exitCode = new CommandLine(new App())
                .registerConverter(Locale.class, s -> new Locale.Builder().setLanguageTag(s).build())
                .execute(args);
        System.exit(exitCode);
    }

    @Override
    public Integer call() throws Exception {
        Path workDirectory = Files.createTempDirectory("htmlchecker");

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
            if (this.headless && chromeService.getTabs().size() > 0) {
                tab = chromeService.getTabs().get(0);
                chromeService.activateTab(tab);
            } else {
                tab = chromeService.createTab();
            }
            final ChromeDevToolsService devToolsService = chromeService.createDevToolsService(tab);

            Browser browser = new Browser(devToolsService, tab.getId());
            browser.navigate(this.url);

            Checker checker = new Checker(browser, this.url, workDirectory.toString());
            checker.run();

            outputResults(checker.getProblemList());
        } finally {
            Files.walk(workDirectory)
                    .sorted(Comparator.reverseOrder())
                    .map(Path::toFile)
                    .forEach(File::delete);
        }

        return 0;
    }

    private void outputResults(List<IProblemItem> problemItems) throws FileNotFoundException, UnsupportedEncodingException, JsonProcessingException {
        try (PrintWriter writer = new PrintWriter(new File(this.outputReportFilepath), "UTF-8")) {
            for (IProblemItem problemItem : problemItems) {
                writer.println(Mapper.writeValueAsString(problemItem));
            }
        }
    }
}
