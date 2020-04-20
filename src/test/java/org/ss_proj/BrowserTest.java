package org.ss_proj;

import com.github.kklisura.cdt.launch.ChromeLauncher;
import com.github.kklisura.cdt.services.ChromeDevToolsService;
import com.github.kklisura.cdt.services.ChromeService;
import com.github.kklisura.cdt.services.types.ChromeTab;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class BrowserTest {
    private ChromeLauncher launcher;
    private ChromeService chromeService;

    @Before
    public void setUp() throws Exception {
        launcher = new ChromeLauncher();
        chromeService = launcher.launch(false);
    }

    @After
    public void tearDown() throws Exception {
        launcher.close();
    }

    @Test
    public void navigate() {
        final ChromeTab tab = chromeService.createTab();

        final ChromeDevToolsService devToolsService = chromeService.createDevToolsService(tab);
        Browser browser = new Browser(devToolsService, tab.getId());
        browser.navigate("https://www.yahoo.co.jp/");

        assertEquals(true, true);
    }
}
