package ui.locators;

import com.microsoft.playwright.Page;
import ui.utils.PlaywrightMethods;

import static ui.utils.StaticVariables.HOME_PAGE_MENU_ITEMS;
import static ui.utils.StaticVariables.HOME_SUBMENU_ITEMS;

public class HomePage extends PlaywrightMethods {

    private final Page page;

    public HomePage(Page page) {
        super(page);
        this.page = page;
    }

    private final String signIn = ".login";
    private final String contactUs = "text=Contact us";
    private final String search = "#search_query_top";
    private final String account = ".account";
    private final String menuItems = "div#block_top_menu > ul > li";
    private final String submenuItem = "ul.submenu > li > a";

    public void clickSignIn() {
        click(signIn);
    }

    public void clickContactUs() {
        click(contactUs);
    }

    public void searchItems(String searchItem) {
        fill(search, searchItem);
        pressEnter(search);
    }

    public void clickWhenLoggedIn() {
        click(account);
    }

    public void hoverOverMenuItem(String menu) {
        scrollIntoView(menuItems);
        int index = arrayListToInt(HOME_PAGE_MENU_ITEMS, menu);
        hover(menuItems, index);
    }

    public void clickMenuItem(String menu) {
        scrollIntoView(menuItems);
        int index = arrayListToInt(HOME_PAGE_MENU_ITEMS, menu);
        click(menuItems, index);
    }

    public void clickToChooseSubmenuItem(String menu, String submenu) {
        hoverOverMenuItem(menu);
        int index = arrayListToInt(HOME_SUBMENU_ITEMS, submenu);
        hover(submenuItem, index);
        click(submenuItem, index);
    }

    // Utility: Hover by index
    private void hover(String selector, int index) {
        page.locator(selector).nth(index).hover();
    }
}
