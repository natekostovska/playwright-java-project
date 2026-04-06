package ui.utils;

import com.microsoft.playwright.*;
import com.microsoft.playwright.options.*;
import org.apache.commons.lang3.ArrayUtils;

import java.io.FileNotFoundException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class PlaywrightMethods {

    protected Page page;

    public PlaywrightMethods(Page page) {
        this.page = page;
    }

     // ========= BASIC ACTIONS =========

    public Locator locator(String selector) {
        return page.locator(selector);
    }

    public void clickNthElement(String selector) {
        waitForElement(selector);  // Wait until the element is available before clicking
        page.locator(selector).click();
    }

    public void clickNthElement(String selector, int index) {
        waitForElement(selector);  // Wait until the element is available before clicking
        page.locator(selector).nth(index).click();
    }

    public void fill(String selector, String value) {
        waitForElement(selector);  // Wait until the element is available before clicking
        page.locator(selector).fill(value);
    }

    public void pressEnter(String selector) {
        page.locator(selector).press("Enter");
    }

    public String getText(String selector) {
        waitForElement(selector);  // Wait until the element is available to retrieve text
        return page.locator(selector).innerText();
    }

    public boolean isVisible(String selector) {
        return page.locator(selector).isVisible();
    }

    // Improved visibility check with a retry mechanism
    public boolean isVisibleWithRetry(String selector, int retries, int delay) {
        for (int i = 0; i < retries; i++) {
            if (isVisible(selector)) {
                return true;
            }
            waitTime(delay);
        }
        return false;
    }

    // ========= SELECT =========

    public void selectByVisibleText(String selector, String visibleText) {
        waitForElement(selector);  // Wait until the element is available to select
        page.locator(selector).selectOption(new SelectOption().setLabel(visibleText));
    }

    public void selectByValue(String selector, String value) {
        waitForElement(selector);  // Wait until the element is available to select
        page.locator(selector).selectOption(new SelectOption().setValue(value));
    }

    public void selectByIndex(String selector, int index) {
        waitForElement(selector);  // Wait until the element is available to select
        page.locator(selector).selectOption(new SelectOption().setIndex(index));
    }

    // ========= FILE UPLOAD =========

    public void uploadFileFromResources(String selector, String resourcePath) throws Exception {
        Path filePath = Paths.get(ClassLoader.getSystemResource(resourcePath).toURI());
        if (!Files.exists(filePath)) {
            throw new FileNotFoundException("File not found: " + resourcePath);
        }
        page.setInputFiles(selector, filePath);
    }

    // Scroll element into view
    public void scrollIntoView(String selector) {
        page.locator(selector).scrollIntoViewIfNeeded();
    }

    public void hover(String selector, int index) {
        page.locator(selector).nth(index).hover();
    }

    // ========== HELPER METHODS =========

    // Wait for element to be present before performing an action
    public void waitForElement(String selector) {
        page.locator(selector).waitFor(new Locator.WaitForOptions().setTimeout(5000));
    }

    // Wait for element visibility with a custom timeout
    public void waitForElementVisible(String selector, int timeoutInMillis) {
        page.locator(selector).waitFor(new Locator.WaitForOptions().setTimeout(timeoutInMillis).setState(WaitForSelectorState.VISIBLE));
    }

    // Wait for element to become visible or interactable (useful for handling dynamic content)
    public void waitForElementInteractable(String selector, int timeoutInMillis) {
        page.locator(selector).waitFor(new Locator.WaitForOptions().setTimeout(timeoutInMillis).setState(WaitForSelectorState.ATTACHED));
    }

    // ========== ARRAY UTILITIES =========

    // Convert an array value to its index
    public int arrayListToInt(String[] items, String value) {
        int index = ArrayUtils.indexOf(items, value);
        if (index == -1) {
            throw new IllegalArgumentException("Value not found in the list: " + value);
        }
        return index;
    }

    // ========= WAIT TIME =========

    // Wait for a specific amount of time (in milliseconds)
    public static void waitTime(int milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}



