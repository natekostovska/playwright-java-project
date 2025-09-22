package ui.framework;

import com.microsoft.playwright.*;
import lombok.Getter;

import java.util.Arrays;

@Getter
public class PlaywrightBrowserManager {
    private Playwright playwright;
    private Browser browser;
    private BrowserContext context;
    private Page page;

    public Page open(String browserName, boolean headless) {
        playwright = Playwright.create();

        switch (browserName.toLowerCase()) {
            case "chrome":
                browser = playwright.chromium().launch(new BrowserType.LaunchOptions()
                        .setHeadless(headless)
                        .setArgs(Arrays.asList(
                                "--disable-extensions",
                                "--start-maximized",
                                "--disable-blink-features=AutomationControlled",
                                "--no-sandbox",
                                "--disable-dev-shm-usage"
                        )));
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

        context = browser.newContext(new Browser.NewContextOptions()
                .setViewportSize(null)
        );

        page = context.newPage();

        // Inject stealth scripts BEFORE any navigation
        page.addInitScript("""
            Object.defineProperty(navigator, 'webdriver', { get: () => undefined });

            const originalQuery = window.navigator.permissions.query;
            window.navigator.permissions.query = (parameters) => (
                parameters.name === 'notifications' ?
                Promise.resolve({ state: Notification.permission }) :
                originalQuery(parameters)
            );

            Object.defineProperty(navigator, 'plugins', { get: () => [1, 2, 3, 4, 5] });

            Object.defineProperty(navigator, 'languages', { get: () => ['en-US', 'en'] });
        """);

        return page;
    }

    public void closePageOnly() {
        if (page != null) page.close();
        if (context != null) context.close();
    }

    public void close() {
        closePageOnly();
        if (browser != null) browser.close();
        if (playwright != null) playwright.close();
    }
}
