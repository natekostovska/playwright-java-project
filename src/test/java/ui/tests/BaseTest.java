package ui.tests;

import com.microsoft.playwright.Page;
import org.testng.Assert;
import org.testng.annotations.*;
import ui.framework.PageObjectManager;
import ui.framework.PlaywrightBrowserManager;
import ui.utils.AddAndUpdatePropertiesFileParameters;

import java.lang.reflect.Method;
import java.util.Arrays;

public class BaseTest {

    private PlaywrightBrowserManager browserManager;

    // THREAD-SAFE PAGE STORAGE (IMPORTANT FIX)
    private static final ThreadLocal<Page> tlPage = new ThreadLocal<>();

    protected PageObjectManager pages;

    // =========================
    // THREAD SAFE ACCESSORS
    // =========================
    public static Page getPage() {
        return tlPage.get();
    }

    public static void setPage(Page page) {
        tlPage.set(page);
    }

    public static void removePage() {
        tlPage.remove();
    }

    @BeforeMethod(alwaysRun = true)
    public void setUp(Method method) {

        browserManager = new PlaywrightBrowserManager();

        String browser = getProp("browser");
        boolean headless = Boolean.parseBoolean(System.getenv().getOrDefault("CI", "false"));

        // CREATE PAGE
        Page page = browserManager.open(browser, headless);
        setPage(page);

        page.setDefaultTimeout(10000);
        page.setDefaultNavigationTimeout(10000);

        pages = new PageObjectManager(page);

        page.navigate(getProp("url"));
        page.waitForLoadState();

        handleCookiesIfPresent();


        // group-based navigation
        Test testAnnotation = method.getAnnotation(Test.class);
        if (testAnnotation != null) {
            boolean isLoginTest = Arrays.asList(testAnnotation.groups())
                    .contains("loginCombinations");

            if (isLoginTest) {
                pages.getLoginPage().navigateToLogin();
            }
        }
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown() {
        try {
            Page page = getPage();

            if (page != null) {
                page.close();
            }

            if (browserManager != null) {
                browserManager.close();
            }

        } catch (Exception ignored) {

        } finally {
            removePage(); // VERY IMPORTANT (prevents thread leaks)
        }
    }

    protected String getProp(String key) {
        return AddAndUpdatePropertiesFileParameters
                .loadAndGetPropertyFromPropertiesFile(
                        key,
                        "src/test/resources/Environment.properties"
                );
    }

    private void handleCookiesIfPresent() {
        try {
            Page page = getPage();

            if (page == null) return;

            var acceptCookies = page.locator("button:has-text('Accept')");

            if (acceptCookies.count() > 0) {
                acceptCookies.first().click(new com.microsoft.playwright.Locator.ClickOptions().setTimeout(2000));
            }

        } catch (Exception ignored) {
        }
    }
}