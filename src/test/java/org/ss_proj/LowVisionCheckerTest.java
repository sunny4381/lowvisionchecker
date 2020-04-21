package org.ss_proj;

import com.github.kklisura.cdt.launch.ChromeLauncher;
import com.github.kklisura.cdt.services.ChromeDevToolsService;
import com.github.kklisura.cdt.services.ChromeService;
import com.github.kklisura.cdt.services.types.ChromeTab;
import org.eclipse.actf.visualization.engines.lowvision.LowVisionType;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class LowVisionCheckerTest {
    private ChromeLauncher launcher;
    private Browser browser;

//    private static String URL = "https://www.yahoo.co.jp/";
    private static String URL = "https://github.com/";

    @Before
    public void setUp() throws Exception {
        launcher = new ChromeLauncher();
        ChromeService chromeService = launcher.launch(false);

        final ChromeTab tab = chromeService.createTab();
        final ChromeDevToolsService devToolsService = chromeService.createDevToolsService(tab);
        browser = new Browser(devToolsService, tab.getId());
        browser.navigate(URL);
    }

    @After
    public void tearDown() throws Exception {
        launcher.close();
    }

    @Test
    public void run() throws Exception {
        LowVisionType lowVisionType = new LowVisionType();
        lowVisionType.setEyesight(true);
        lowVisionType.setEyesightDegree(0.5f);
        lowVisionType.setCVD(true);
        lowVisionType.setCVDType(2);
        lowVisionType.setColorFilter(true);
        lowVisionType.setColorFilterDegree(0.8f);

        LowVisionChecker checker = new LowVisionChecker(browser, URL, lowVisionType);
        checker.run();
    }
}