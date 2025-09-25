package ui.tests;

import com.microsoft.playwright.Page;
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

    @BeforeMethod(onlyForGroups = {"loginCombinations"})
    public void setUp() {
        String browser = getProp("browser");
        boolean headless = Boolean.parseBoolean(System.getenv().getOrDefault("CI", "false"));

        page = browserManager.open(browser, headless);
        page.setDefaultTimeout(10000);
        page.setDefaultNavigationTimeout(10000);
        page.navigate(getProp("url"));
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
