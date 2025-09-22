package ui.tests;

import com.microsoft.playwright.Page;
import org.testng.Assert;
import org.testng.annotations.*;
import ui.framework.PlaywrightBrowserManager;
import ui.locators.LoginPage;
import ui.utils.AddAndUpdatePropertiesFileParameters;

public class BaseTest {

    protected PlaywrightBrowserManager browserManager;
    public static Page page;

    @BeforeClass(alwaysRun = true)
    public void setupBrowserManager() {
        browserManager = new PlaywrightBrowserManager();
    }

    @BeforeMethod(alwaysRun = true)
    @Parameters({"testGroup"})
    public void setUp(@Optional("") String testGroup) {
        final String browser = getProp("browser");
        // Detect CI
        final boolean headless; // Run headless ONLY on CI
        headless = Boolean.parseBoolean(System.getenv().getOrDefault("CI", "false"));

        page = browserManager.open(browser, headless);
        page.navigate(getProp("url"));

        if (testGroup.equalsIgnoreCase("smoke") || testGroup.equalsIgnoreCase("regression")) {
            String title = page.title();
            Assert.assertEquals(title, "Practice Software Testing - Toolshop - v5.0");
            LoginPage.navigateToLogin();
        }
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown() {
        browserManager.closePageOnly();
    }

    @AfterClass(alwaysRun = true)
    public void closeAll() {
        browserManager.close();
    }

    protected String getProp(String key) {
        return AddAndUpdatePropertiesFileParameters.loadAndGetPropertyFromPropertiesFile(
                key, "src/test/resources/Environment.properties"
        );
    }
}
