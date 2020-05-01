package org.ss_proj.htmlchecker;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.kklisura.cdt.launch.ChromeArguments;
import com.github.kklisura.cdt.launch.ChromeLauncher;
import com.github.kklisura.cdt.services.ChromeDevToolsService;
import com.github.kklisura.cdt.services.ChromeService;
import com.github.kklisura.cdt.services.types.ChromeTab;
import org.eclipse.actf.visualization.eval.problem.IProblemItem;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.ss_proj.lowvision.Browser;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

import static org.hamcrest.Matchers.greaterThan;
import static org.junit.Assert.*;

public class CheckerTest {
//    private static String URL = "https://www.yahoo.co.jp/";
//    private static String URL = "https://github.com/";
//    private static String URL = "https://www.facebook.com/";
    private static String URL = "https://www.town.minami.lg.jp/";

    private ChromeLauncher launcher;
    private Browser browser;
    private Path workDirectory;

    @Before
    public void setUp() throws Exception {
        ChromeArguments.Builder argumentsBuilder = ChromeArguments.defaults(false);
        argumentsBuilder.additionalArguments("enable-automation", true);
        argumentsBuilder.additionalArguments("lang", Locale.getDefault().toLanguageTag());

        this.launcher = new ChromeLauncher();
        final ChromeService chromeService = launcher.launch(argumentsBuilder.build());

        final ChromeTab tab = chromeService.getTabs().get(0);
        chromeService.activateTab(tab);

        final ChromeDevToolsService devToolsService = chromeService.createDevToolsService(tab);
        this.browser = new Browser(devToolsService, tab.getId());
        this.browser.navigate(URL);

        this.workDirectory = Files.createTempDirectory("htmlchecker");
    }

    @After
    public void tearDown() throws Exception {
        this.launcher.close();

        Files.walk(this.workDirectory)
                .sorted(Comparator.reverseOrder())
                .map(Path::toFile)
                .forEach(File::delete);
    }

    @Ignore
    @Test
    public void check() throws Exception {
        Checker checker = new Checker(browser, URL, workDirectory.toString());
        checker.run();

        List<IProblemItem> problemItems = checker.getProblemList();
        assertThat(problemItems.size(), greaterThan(1));

        ObjectMapper mapper = new ObjectMapper();
        for (IProblemItem problemItem : problemItems) {
            System.out.println(mapper.writeValueAsString(problemItem));
        }
    }
}