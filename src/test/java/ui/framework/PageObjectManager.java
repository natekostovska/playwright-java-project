package ui.framework;

import com.microsoft.playwright.Page;
import ui.locators.ContactPage;
import ui.locators.LoginPage;

public class PageObjectManager {

    private final Page page;

    private LoginPage loginPage;
    private ContactPage contactPage;

    public PageObjectManager(Page page) {
        this.page = page;
    }

    public LoginPage getLoginPage() {
        if (loginPage == null) loginPage = new LoginPage(page);
        return loginPage;
    }

    public ContactPage getContactPage() {
        if (contactPage == null) contactPage = new ContactPage(page);
        return contactPage;
    }
}
