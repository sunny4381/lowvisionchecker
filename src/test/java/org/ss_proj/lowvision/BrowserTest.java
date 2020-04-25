package org.ss_proj.lowvision;

import com.github.kklisura.cdt.launch.ChromeArguments;
import com.github.kklisura.cdt.launch.ChromeLauncher;
import com.github.kklisura.cdt.services.ChromeDevToolsService;
import com.github.kklisura.cdt.services.ChromeService;
import com.github.kklisura.cdt.services.types.ChromeTab;
import com.google.common.io.Files;
import org.apache.commons.lang.StringEscapeUtils;
import org.eclipse.actf.model.ui.editor.browser.ICurrentStyles;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.Locale;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

public class BrowserTest {
    private ChromeLauncher launcher;
    private ChromeService chromeService;

    @Before
    public void setUp() throws Exception {
        launcher = new ChromeLauncher();
        ChromeArguments.Builder argumentsBuilder = ChromeArguments.defaults(true);
        argumentsBuilder.additionalArguments("window-size", "1024,768");
        argumentsBuilder.additionalArguments("disable-backgrounding-occluded-windows", true);
        argumentsBuilder.additionalArguments("disable-breakpad", true);
        argumentsBuilder.additionalArguments("disable-dev-shm-usage", true);
        argumentsBuilder.additionalArguments("disable-ipc-flooding-protection", true);
        argumentsBuilder.additionalArguments("disable-renderer-backgrounding", true);
        argumentsBuilder.additionalArguments("disable-session-crashed-bubble", true);
        argumentsBuilder.additionalArguments("disable-web-security", true);
        argumentsBuilder.additionalArguments("enable-automation", true);
        argumentsBuilder.additionalArguments("force-color-profile", "srgb");
        argumentsBuilder.additionalArguments("keep-alive-for-test", true);
        argumentsBuilder.additionalArguments("password-store", "basic");
        argumentsBuilder.additionalArguments("use-mock-keychain", true);
        argumentsBuilder.additionalArguments("disable-features", "site-per-process,TranslateUI");
        argumentsBuilder.additionalArguments("enable-features", "NetworkService,NetworkServiceInProcess");
        argumentsBuilder.additionalArguments("lang", Locale.getDefault().toLanguageTag());
        chromeService = launcher.launch(argumentsBuilder.build());
    }

    @After
    public void tearDown() throws Exception {
        launcher.close();
    }

    @Ignore
    @Test
    public void testFacebookStyles() throws Exception {
        final ChromeTab tab = chromeService.getTabs().get(0);
        chromeService.activateTab(tab);

        final ChromeDevToolsService devToolsService = chromeService.createDevToolsService(tab);
        Browser browser = new Browser(devToolsService, tab.getId());
        browser.navigate("https://www.facebook.com/");

        Collection<ICurrentStyles> styles = browser.getCurrentStyles().values();
        assertThat(styles.size(), greaterThan(0));

        {
            ICurrentStyles htmlStyle = styles.stream().filter((item) -> item.getXPath().equals("/html")).findFirst().get();
            assertThat(htmlStyle.getTagName(), is("html"));
            assertThat(htmlStyle.getFontSize(), isEmptyString());
            assertThat(htmlStyle.getComputedFontSize(), is("16px"));
        }

        {
            ICurrentStyles bodyStyle = styles.stream().filter((item) -> item.getXPath().equals("/html/body")).findFirst().get();
            assertThat(bodyStyle.getTagName(), is("body"));
            assertThat(bodyStyle.getComputedColor(), is("rgb(28, 30, 33)"));
            assertThat(bodyStyle.getComputedBackgroundColor(), is("rgb(255, 255, 255)"));
            assertThat(bodyStyle.getComputedBackgroundImage(), is("none"));
            assertThat(bodyStyle.getFontSize(), isEmptyString());
            assertThat(bodyStyle.getComputedFontSize(), is("12px"));
            assertThat(bodyStyle.getComputedOpacity(), is("1"));
            assertThat(bodyStyle.getChildTexts(), emptyArray());
            assertThat(bodyStyle.getDescendantTextsWithBGImage(), emptyArray());
        }

        {
            ICurrentStyles style = styles.stream().filter((item) -> item.getXPath().equals("/html/body/div[1]/div[1]/div/div/div[3]/div")).findFirst().get();
            assertThat(style.getChildTexts(), arrayContaining("このメニューを開くには、", "と", "を同時に押してください"));
        }

        {
            ICurrentStyles style = styles.stream().filter((item) -> item.getXPath().equals("/html/body/div[1]/div[1]/div/div/div[2]/div/div[1]/a/span[2]/i")).findFirst().get();
            assertThat(style.getComputedBackgroundImage(), containsString("/rsrc.php/v3/yz/r/F5fJ75JdD_h.png"));
        }
    }

    @Ignore
    @Test
    public void convert() throws Exception {
        File file = new File("/Users/nakano_hideo/Projects/lowvisionchecker/src/main/resources/messages_ja.properties");
        String str = Files.asCharSource(file, StandardCharsets.UTF_8).read();
        System.out.println(StringEscapeUtils.unescapeJava(str));
    }
}
