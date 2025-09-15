package ui.locators;

import static ui.tests.BaseTest.page;

public class LoginPage {
    public static final String signInNavLink = "[data-test='nav-sign-in']";
    public static final String inputEmail = "#email";             // Email input field
    public static final String inputPassword = "#password";       // Password input field
    public static final String clickLoginButton = "[data-test='login-submit']";
    public static final String loginHeading = "h3:has-text('Login')";
    public static final String accountTitle = "[data-test='page-title']";
    public static final String loggedUserName = "[data-test='nav-menu']";
    public static final String  emailError = ("[data-test='email-error']");
    public static final String  passwordError = ("[data-test='password-error']");
    public static final String invalidEmailOrPasswordError=("[data-test='login-error']");

    public static void login(String email, String password) {
        page.locator(LoginPage.signInNavLink).click();
        page.locator(LoginPage.inputEmail).fill(email);
        page.locator(LoginPage.inputPassword).fill(password);
        page.locator(LoginPage.clickLoginButton).click();
    }

}
