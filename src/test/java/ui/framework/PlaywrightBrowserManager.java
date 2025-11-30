package ui.framework;

import com.microsoft.playwright.*;
import lombok.Getter;
import java.util.Arrays;

@Getter
public class PlaywrightBrowserManager {
    private static Playwright playwright;
    private static Browser browser;
    private static BrowserContext context;
    private static Page page;

    public static Page open(String browserName, boolean headless) {
        playwright = Playwright.create();

        switch (browserName.toLowerCase()) {
            case "chrome":
                browser = playwright.chromium().launch(new BrowserType.LaunchOptions()
                        .setHeadless(headless)
                        .setSlowMo(1000)
                        .setArgs(Arrays.asList("--disable-blink-features=AutomationControlled",
                                "--disable-gpu",
                                "--no-sandbox",
                                "--disable-dev-shm-usage",
                                "--disable-extensions",
                                "--start-maximized",
                                "--disable-setuid-sandbox",
                                "--remote-debugging-port=0",
                                "--disable-background-timer-throttling",
                                "--disable-backgrounding-occluded-windows",
                                "--disable-renderer-backgrounding")));
                break;
            case "firefox":
                browser = playwright.firefox().launch(new BrowserType.LaunchOptions()
                        .setHeadless(headless));
                break;
            case "webkit":
                browser = playwright.webkit().launch(new BrowserType.LaunchOptions()
                        .setHeadless(headless));
                break;
            default:
                throw new IllegalArgumentException("Unsupported browser: " + browserName);
        }

        playwright.selectors().setTestIdAttribute("data-test");
        context = browser.newContext(new Browser.NewContextOptions().setViewportSize(null));

        page = context.newPage();
        return page;
    }

    public static void closePageOnly() {
        if (page != null) page.close();
        if (context != null) context.close();
    }

    public static void close() {
        closePageOnly();
        if (browser != null) browser.close();
        if (playwright != null) playwright.close();
    }

}