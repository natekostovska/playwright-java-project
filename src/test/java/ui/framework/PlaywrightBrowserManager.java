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
                        .setArgs(Arrays.asList("--disable-extensions", "--start-maximized")));
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

        context = browser.newContext(new Browser.NewContextOptions().setViewportSize(null));

        page = context.newPage();
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