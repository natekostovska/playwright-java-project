package ui.tests;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import org.testng.Assert;
import org.testng.annotations.*;
import ui.framework.PageObjectManager;
import ui.framework.PlaywrightBrowserManager;
import ui.utils.AddAndUpdatePropertiesFileParameters;
import ui.utils.PlaywrightMethods;

import java.lang.reflect.Method;
import java.util.Arrays;

public class BaseTest {

    protected PlaywrightBrowserManager browserManager;
    protected Page page;
    protected PageObjectManager pages;

    @BeforeClass(alwaysRun = true)
    public void setupBrowserManager() {
        browserManager = new PlaywrightBrowserManager();
    }

    @BeforeMethod(alwaysRun = true)
    public void setUp(Method method) {
        String browser = getProp("browser");
        boolean headless = Boolean.parseBoolean(System.getenv().getOrDefault("CI", "false"));

        page = browserManager.open(browser, headless);
        page.setDefaultTimeout(10000);
        page.setDefaultNavigationTimeout(10000);

        pages = new PageObjectManager(page);

        page.navigate(getProp("url"));
        handleCookiesIfPresent();
        String title = page.title();
        System.out.println("Page Title: " + title);
        Assert.assertTrue(title.contains("Practice Software Testing"));

        // Navigate to login **only if test is in loginCombinations group**
        if (method.getAnnotation(Test.class) != null &&
                Arrays.asList(method.getAnnotation(Test.class).groups()).contains("loginCombinations")) {
            pages.getLoginPage().navigateToLogin();
        }
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown() {
        browserManager.close();
    }

    protected String getProp(String key) {
        return AddAndUpdatePropertiesFileParameters.loadAndGetPropertyFromPropertiesFile(
                key, "src/test/resources/Environment.properties"
        );
    }

    private void handleCookiesIfPresent() {
        Locator acceptCookies = page.locator("button:has-text('Accept')");
        if (acceptCookies.isVisible()) {
            acceptCookies.click();
        }
    }

}
