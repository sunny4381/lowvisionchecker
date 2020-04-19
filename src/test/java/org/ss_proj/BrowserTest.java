package org.ss_proj;

import io.webfolder.cdp.Launcher;
import io.webfolder.cdp.session.Session;
import io.webfolder.cdp.session.SessionFactory;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class BrowserTest {
    private Launcher launcher;
    private SessionFactory factory;

    @Before
    public void setUp() throws Exception {
        launcher = new Launcher();
        factory = launcher.launch();
    }

    @After
    public void tearDown() throws Exception {
        factory.close();
        launcher.kill();
    }

    @Test
    public void navigate() {
        try (Session session = factory.create()) {
            Browser browser = new Browser(session);
            browser.navigate("https://www.yahoo.co.jp/");
        }
    }
}
