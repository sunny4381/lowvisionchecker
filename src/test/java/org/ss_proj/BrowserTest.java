package org.ss_proj;

import com.github.kklisura.cdt.launch.ChromeLauncher;
import com.github.kklisura.cdt.services.ChromeDevToolsService;
import com.github.kklisura.cdt.services.ChromeService;
import com.github.kklisura.cdt.services.types.ChromeTab;
import com.google.common.io.Files;
import org.apache.commons.lang.StringEscapeUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.nio.charset.StandardCharsets;

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

    @Test
    public void convert() throws Exception {
        File file = new File("/Users/nakano_hideo/Projects/lowvisionchecker/src/main/resources/messages_ja.properties");
        String str = Files.asCharSource(file, StandardCharsets.UTF_8).read();
        System.out.println(StringEscapeUtils.unescapeJava(str));
    }
}
