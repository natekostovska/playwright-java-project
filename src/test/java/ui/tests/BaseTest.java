package ui.tests;

import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.LoadState;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.annotations.*;
import ui.framework.PlaywrightBrowserManager;
import ui.locators.LoginPage;
import ui.utils.AddAndUpdatePropertiesFileParameters;

public class BaseTest {

    protected PlaywrightBrowserManager browserManager;
    protected Page page;
    public LoginPage loginPage;

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
        loginPage = new LoginPage(page);
        page.navigate(getProp("url"));
        page.waitForLoadState(LoadState.NETWORKIDLE);
        String title = page.title();
        Assert.assertEquals(title, "Practice Software Testing - Toolshop - v5.0");
        loginPage.navigateToLogin();
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

}
