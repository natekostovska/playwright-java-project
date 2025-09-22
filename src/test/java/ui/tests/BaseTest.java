package ui.tests;

import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.LoadState;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.annotations.*;
import ui.framework.PlaywrightBrowserManager;
import ui.utils.AddAndUpdatePropertiesFileParameters;

import static ui.locators.LoginPage.navigateToLogin;

public class BaseTest {

    protected PlaywrightBrowserManager browserManager;
    public static Page page;

    @BeforeClass(alwaysRun = true)
    public void setupBrowserManager() {
        browserManager = new PlaywrightBrowserManager();
    }

    @BeforeMethod(alwaysRun = true)
    public void setUp(ITestResult result) {
        String[] groups = result.getMethod().getGroups();
        String browser = getProp("browser");
        boolean headless = Boolean.parseBoolean(System.getenv().getOrDefault("CI", "false"));

        page = browserManager.open(browser, headless);
        page.navigate(getProp("url"));
        page.waitForLoadState(LoadState.NETWORKIDLE);


        if (containsGroup(groups, "smoke") || containsGroup(groups, "regression")) {
            String title = page.title();
            Assert.assertEquals(title, "Practice Software Testing - Toolshop - v5.0");
        }

        if (containsGroup(groups, "loginCombinations")) {
            navigateToLogin();
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
    private boolean containsGroup(String[] groups, String target) {
        for (String group : groups) {
            if (group.equalsIgnoreCase(target)) return true;
        }
        return false;
    }
}
