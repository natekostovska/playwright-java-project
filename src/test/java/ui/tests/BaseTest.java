package ui.tests;

import com.microsoft.playwright.*;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import ui.locators.LoginPage;
import ui.methods.AddAndUpdatePropertiesFileParameters;

import java.util.Arrays;

public class BaseTest {
    protected static Playwright playwright;
    public static Browser browser;
    protected static BrowserContext browserContext;
    public static Page page;

    @BeforeClass(alwaysRun = true)
    public static void setUpBrowser() {
        boolean isCI = Boolean.parseBoolean(System.getenv().getOrDefault("CI", "false"));
        playwright = Playwright.create();
        browser = playwright.chromium().launch(
                new BrowserType.LaunchOptions()
                        .setHeadless(isCI)
                        .setArgs(Arrays.asList("--no-sandbox", "--disable-extensions", "--disable-gpu", "--start-maximized"))
        );

    }

    @BeforeMethod(alwaysRun = true, onlyForGroups = {"smoke", "regression"})
    public void beforeMethodForLoggedInTests() {
        browserContext = browser.newContext(new Browser.NewContextOptions()
                .setViewportSize(null)); // Allow real screen size
        page = browserContext.newPage();

        page.navigate(getProp("url"));
        String title = page.title();
        Assert.assertEquals(title, "Practice Software Testing - Toolshop - v5.0");

        LoginPage.login(getProp("email"), getProp("password"));


    }

    @BeforeMethod(alwaysRun = true, onlyForGroups = {"loginCombinations"})
    public void beforeMethodForLoginTests() {
        browserContext = browser.newContext(new Browser.NewContextOptions()
                .setViewportSize(null)); // Allow real screen size
        page = browserContext.newPage();
        page.navigate(getProp("url"));
    }

    @AfterMethod(alwaysRun = true)
    void cleanupPage() {
        if (page != null) page.close();
        if (browserContext != null) browserContext.close();
    }

    @AfterClass(alwaysRun = true)
    static void cleanUp() {
        if (browser != null) browser.close();
        if (playwright != null) playwright.close();
    }


    private static BrowserType.LaunchOptions headlessSlow(int delay) {
        return new BrowserType.LaunchOptions().setHeadless(false).setSlowMo(delay);
        // return null; - if i want hardcoded values
    }

    protected String getProp(String key) {
        return AddAndUpdatePropertiesFileParameters.loadAndGetPropertyFromPropertiesFile(
                key, "src/test/resources/Environment.properties"
        );
    }

}
