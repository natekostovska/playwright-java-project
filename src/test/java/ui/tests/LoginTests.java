package ui.tests;

import io.qameta.allure.Story;

import org.testng.Assert;
import ui.locators.LoginPage;
import ui.methods.DataSources;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static ui.locators.LoginPage.*;


public class LoginTests extends BaseTest {
    @DataProvider(name = "validLogin")
    public Object[][] validUsers() {
        return DataSources.excel("src/test/resources/data/loginTestDataPW.xlsx", "validUsers");
    }

    @DataProvider(name = "invalidLogin")
    public Object[][] invalidUsers() {
        return DataSources.excel("src/test/resources/data/loginTestDataPW.xlsx", "invalidUsers");
    }

    @Story("User logs in with valid credentials")
    @Test(dataProvider = "validLogin", groups = {"loginCombinations"}, description = "Should log in with valid user and password")
    public void successfulLoginTest(String email, String password, String testScenario) {
        page.locator(LoginPage.signInNavLink).click();
        Assert.assertEquals(page.locator(LoginPage.loginHeading).innerText(), "Login");
        page.locator(LoginPage.inputEmail).fill(email);
        page.locator(LoginPage.inputPassword).fill(password);
        page.locator(LoginPage.clickLoginButton).click();
        Assert.assertEquals(page.locator(LoginPage.accountTitle).innerText(), "My account");
        Assert.assertEquals(page.locator(LoginPage.loggedUserName).innerText().trim(), "Natasha Kostovska");
        System.out.println(testScenario);
    }

    @Story("User logs in with invalid credentials")
    @Test(dataProvider = "invalidLogin", groups = {"loginCombinations"}, description = "Should not be able to log in with invalid user, invalid password or combination of both")
    public void unsuccessfulLoginWithWrongCredentialsTest(String email, String password, String testScenario) {
        page.locator(LoginPage.signInNavLink).click();
        Assert.assertEquals(page.locator(LoginPage.loginHeading).innerText(), "Login");
        page.locator(LoginPage.inputEmail).fill(email);
        page.locator(LoginPage.inputPassword).fill(password);
        page.locator(LoginPage.clickLoginButton).click();

// Extract texts if present
        boolean isEmailErrorVisible = page.locator(emailError).isVisible();
        boolean isPasswordErrorVisible = page.locator(passwordError).isVisible();
        boolean isInvalidEmailOrPasswordErrorVisible = page.locator(invalidEmailOrPasswordError).isVisible();

// Optional print
        if (isEmailErrorVisible) {
            System.out.println("Email Error: " + page.locator(emailError).innerText());
        }
        if (isPasswordErrorVisible) {
            System.out.println("Password Error: " + page.locator(passwordError).innerText());
        }
        if (isInvalidEmailOrPasswordErrorVisible) {
            System.out.println("Invalid email or password: " + page.locator(invalidEmailOrPasswordError).innerText());
        }
        // Assertion: At least one of them must be visible
        Assert.assertTrue(isEmailErrorVisible || isPasswordErrorVisible || isInvalidEmailOrPasswordErrorVisible,
                "Expected at least one validation error, but none appeared.");
        System.out.println(testScenario);
    }

}
