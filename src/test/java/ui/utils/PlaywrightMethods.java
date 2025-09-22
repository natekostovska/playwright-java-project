package ui.utils;

import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.Frame;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.BoundingBox;
import com.microsoft.playwright.options.MouseButton;
import com.microsoft.playwright.options.SelectOption;

import java.nio.file.Paths;
import java.util.List;

import static ui.tests.BaseTest.page;


public class PlaywrightMethods {
    // ========== BASIC LOCATOR ACTIONS ==========

    public Locator locator(String selector) {
        return page.locator(selector);
    }

    public static void click(String selector) {
        page.locator(selector).click();
    }

    public void click(String selector, int index) {
        page.locator(selector).nth(index).click();
    }

    public static void fill(String selector, String value) {
        page.locator(selector).fill(value);
    }

    public void pressEnter(String selector) {
        page.locator(selector).press("Enter");
    }

    public void fillAndPressEnter(String selector, String value) {
        fill(selector, value);
        pressEnter(selector);
    }

    public static String getText(String selector) {
        return page.locator(selector).innerText();
    }

    public String getText(String selector, int index) {
        return page.locator(selector).nth(index).innerText();
    }

    public List<String> getAllTexts(String selector) {
        return page.locator(selector).allInnerTexts();
    }

    public void clear(String selector) {
        page.locator(selector).fill("");
    }

    public static boolean isVisible(String selector) {
        return page.locator(selector).isVisible();
    }

    public static boolean isEnabled(String selector) {
        return page.locator(selector).isEnabled();
    }

    public boolean isDisabled(String selector) {
        return !isEnabled(selector);
    }

    // ========== MOUSE ACTIONS ==========

    public void doubleClick(String selector) {
        page.locator(selector).dblclick();
    }

    public void rightClick(String selector) {
        page.locator(selector).click(new Locator.ClickOptions().setButton(MouseButton.RIGHT));
    }

    public void hover(String selector) {
        page.locator(selector).hover();
    }

    public void scrollIntoView(String selector) {
        page.locator(selector).scrollIntoViewIfNeeded();
    }

    // ========== SELECT (Dropdown) ==========

    public void selectByVisibleText(String selector, String visibleText) {
        page.locator(selector).selectOption(new SelectOption().setLabel(visibleText));
    }

    public void selectByValue(String selector, String value) {
        page.locator(selector).selectOption(value);
    }

    public void selectByIndex(String selector, int index) {
        String optionValue = page.locator(selector + " option").nth(index).getAttribute("value");
        page.locator(selector).selectOption(optionValue);
    }

    // ========== ALERTS ==========

    public String handleAlertAndAccept() {
        final String[] alertText = new String[1];
        page.onceDialog(dialog -> {
            alertText[0] = dialog.message();
            dialog.accept();
        });
        return alertText[0];
    }

    public String handleAlertAndDismiss() {
        final String[] alertText = new String[1];
        page.onceDialog(dialog -> {
            alertText[0] = dialog.message();
            dialog.dismiss();
        });
        return alertText[0];
    }

    // ========== TABS ==========

    public Page switchToNewTab() {
        BrowserContext context = page.context();
        Page newTab = context.waitForPage(() -> {
            // Trigger opening of new tab/window
        });
        return newTab;
    }

    // ========== FRAMES ==========

    public Frame getFrameByName(String name) {
        return page.frame(name);
    }

    public Frame getFrameByIndex(int index) {
        return page.frames().get(index);
    }

    public void switchToMainFrame() {
        // Not needed in Playwright, use frame/page directly
    }

    // ========== FILE UPLOAD ==========

    public void uploadFile(String selector, String filePath) {
        page.setInputFiles(selector, Paths.get(filePath));
    }

    // ========== SLIDERS / DRAG & DROP ==========

    public void dragAndDrop(String sourceSelector, String targetSelector) {
        page.dragAndDrop(sourceSelector, targetSelector);
    }

    public void dragAndDropOffset(String selector, int x, int y) {
        Locator locator = page.locator(selector);
        BoundingBox box = locator.boundingBox();
        if (box != null) {
            page.mouse().move(box.x + box.width / 2, box.y + box.height / 2);
            page.mouse().down();
            page.mouse().move(box.x + x, box.y + y);
            page.mouse().up();
        }
    }

    public static void waitTime(int waitTime) {
        try {
            Thread.sleep(waitTime);
        } catch (Exception e) {
        }
    }
}
