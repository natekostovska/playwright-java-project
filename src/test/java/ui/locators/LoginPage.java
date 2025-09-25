package ui.locators;

import com.microsoft.playwright.Page;
import ui.utils.PlaywrightMethods;

public class LoginPage extends PlaywrightMethods {
    public static final String signInNavLink = "[data-test='nav-sign-in']";
    public static final String inputEmail = "#email";
    public static final String inputPassword = "#password";
    public static final String clickLoginButton = "[data-test='login-submit']";
    public static final String loginHeading = "h3:has-text('Login')";
    public static final String accountTitle = "[data-test='page-title']";
    public static final String loggedUserName = "[data-test='nav-menu']";
    public static final String emailError = "[data-test='email-error']";
    public static final String passwordError = "[data-test='password-error']";
    public static final String invalidEmailOrPasswordError = "[data-test='login-error']";

    public LoginPage(Page page) {
        super(page);  // Pass page to PlaywrightMethods constructor
    }


    public void navigateToLogin() {
        click(signInNavLink);
    }

    public void login(String email, String password) {
        fill(inputEmail, email);
        fill(inputPassword, password);
        click(clickLoginButton);
    }
}
