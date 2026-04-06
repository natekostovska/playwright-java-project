package ui.locators;

import com.microsoft.playwright.Page;
import ui.utils.PlaywrightMethods;

import static ui.utils.StaticVariables.HOME_PAGE_MENU_ITEMS;
import static ui.utils.StaticVariables.HOME_SUBMENU_ITEMS;

public class HomePage extends PlaywrightMethods {

    public HomePage(Page page) {
        super(page);
    }

    public static final String SIGN_IN = ".login";
    public static final String CONTACT_US = "text=Contact us";
    public static final String SEARCH = "#search_query_top";
    public static final String ACCOUNT = ".account";
    public static final String MENU_ITEMS = "div#block_top_menu > ul > li";
    public static final String SUBMENU_ITEM = "ul.submenu > li > a";

    public void clickSignIn() {
        clickNthElement(SIGN_IN);
    }

    public void clickContactUs() {
        clickNthElement(CONTACT_US);
    }

    public void searchItems(String searchItem) {
        fill(SEARCH, searchItem);
        pressEnter(SEARCH);
    }

    public void clickWhenLoggedIn() {
        clickNthElement(ACCOUNT);
    }

    public void hoverOverMenuItem(String menu) {
        scrollIntoView(MENU_ITEMS);
        int index = arrayListToInt(HOME_PAGE_MENU_ITEMS, menu);
        hover(MENU_ITEMS, index);
    }

    public void clickMenuItem(String menu) {
        scrollIntoView(MENU_ITEMS);
        int index = arrayListToInt(HOME_PAGE_MENU_ITEMS, menu);
        clickNthElement(MENU_ITEMS, index);
    }

    public void clickToChooseSubmenuItem(String menu, String submenu) {
        hoverOverMenuItem(menu);
        int index = arrayListToInt(HOME_SUBMENU_ITEMS, submenu);
        hover(SUBMENU_ITEM, index);
        clickNthElement(SUBMENU_ITEM, index);
    }
}
