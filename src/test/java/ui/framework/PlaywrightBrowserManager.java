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
                browser = playwright.chromium().launch(
                        new BrowserType.LaunchOptions()
                                .setHeadless(headless)
                                .setSlowMo(1000)
                                .setArgs(Arrays.asList(
                                        "--disable-blink-features=AutomationControlled",
                                        "--disable-gpu",
                                        "--no-sandbox",
                                        "--disable-dev-shm-usage",
                                        "--disable-extensions",
                                        "--start-maximized",
                                        "--disable-setuid-sandbox",
                                        "--remote-debugging-port=0",
                                        "--disable-background-timer-throttling",
                                        "--disable-backgrounding-occluded-windows",
                                        "--disable-renderer-backgrounding"
                                ))
                );
                break;

            case "firefox":
                browser = playwright.firefox().launch(
                        new BrowserType.LaunchOptions().setHeadless(headless)
                );
                break;

            case "webkit":
                browser = playwright.webkit().launch(
                        new BrowserType.LaunchOptions().setHeadless(headless)
                );
                break;

            default:
                throw new IllegalArgumentException("Unsupported browser: " + browserName);
        }

        playwright.selectors().setTestIdAttribute("data-test");

        context = browser.newContext(
                new Browser.NewContextOptions().setViewportSize(null)
        );

        page = context.newPage();
        return page;
    }

    public void close() {
        // Close in correct order with full safety
        try {
            if (page != null && !page.isClosed()) {
                page.close();
            }
        } catch (Exception e) {
            System.out.println("Error closing page: " + e.getMessage());
        }

        try {
            if (context != null) {
                context.close();
            }
        } catch (Exception e) {
            System.out.println("Error closing context: " + e.getMessage());
        }

        try {
            if (browser != null) {
                browser.close();
            }
        } catch (Exception e) {
            System.out.println("Error closing browser: " + e.getMessage());
        }

        try {
            if (playwright != null) {
                playwright.close();
            }
        } catch (Exception e) {
            System.out.println("Error closing playwright: " + e.getMessage());
        }

        // 🔑 IMPORTANT: reset references to avoid reuse issues
        page = null;
        context = null;
        browser = null;
        playwright = null;
    }
}