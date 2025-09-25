package ui.tests;

import io.qameta.allure.Story;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import ui.locators.LoginPage;
import ui.utils.DataSources;
import ui.utils.PlaywrightMethods;

public class LoginTests extends BaseTest {
    @DataProvider(name = "validLogin")
    public Object[][] validUsers() {
        return DataSources.excel("src/test/resources/data/loginTestDataPW.xlsx", "validUsers");
    }

    @DataProvider(name = "invalidLogin")
    public Object[][] invalidUsers() {
        return DataSources.excel("src/test/resources/data/loginTestDataPW.xlsx", "invalidUsers");
    }

    @Test(dataProvider = "validLogin", groups = {"loginCombinations"},
            description = "Should log in with valid user and password")
    @Story("User logs in with valid credentials")
    public void successfulLoginTest(String email, String password, String testScenario) {
       Assert.assertEquals(loginPage.getText(LoginPage.loginHeading), "Login");

        loginPage.login(email, password);

        Assert.assertEquals(loginPage.getText(LoginPage.accountTitle), "My account");
        Assert.assertEquals(loginPage.getText(LoginPage.loggedUserName).trim(), "Natasha Kostovska");

        System.out.println(testScenario);
    }

    @Test(dataProvider = "invalidLogin", groups = {"loginCombinations"},
            description = "Should not be able to log in with invalid user, invalid password or combination of both")
    @Story("User logs in with invalid credentials")
    public void unsuccessfulLoginWithWrongCredentialsTest(String email, String password, String testScenario) {
        loginPage = new LoginPage(page);

        Assert.assertEquals(loginPage.getText(LoginPage.loginHeading), "Login");

        loginPage.login(email, password);
        PlaywrightMethods.waitTime(2000);

        boolean isEmailErrorVisible = loginPage.isVisible(LoginPage.emailError);
        boolean isPasswordErrorVisible = loginPage.isVisible(LoginPage.passwordError);
        boolean isInvalidEmailOrPasswordErrorVisible = loginPage.isVisible(LoginPage.invalidEmailOrPasswordError);

        if (isEmailErrorVisible) {
            System.out.println("Email Error: " + loginPage.getText(LoginPage.emailError));
        } else if (isPasswordErrorVisible) {
            System.out.println("Password Error: " + loginPage.getText(LoginPage.passwordError));
        } else if (isInvalidEmailOrPasswordErrorVisible) {
            System.out.println("Invalid email or password: " + loginPage.getText(LoginPage.invalidEmailOrPasswordError));
        }

        Assert.assertTrue(
                isEmailErrorVisible || isPasswordErrorVisible || isInvalidEmailOrPasswordErrorVisible,
                "Expected at least one validation error, but none appeared."
        );

        System.out.println(testScenario);
    }
}
