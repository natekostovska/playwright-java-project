package ui.tests;

import io.qameta.allure.Story;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import ui.utils.DataSources;
import ui.utils.PlaywrightMethods;

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

    @Test(dataProvider = "validLogin", groups = {"loginCombinations"},
            description = "Should log in with valid user and password")
    @Story("User logs in with valid credentials")
    public void successfulLoginTest(String email, String password, String testScenario) {
       Assert.assertEquals(loginPage.getText(loginHeading), "Login");

        loginPage.login(email, password);

        Assert.assertEquals(loginPage.getText(accountTitle), "My account");
        Assert.assertEquals(loginPage.getText(loggedUserName).trim(), "Natasha Kostovska");

        System.out.println(testScenario);
    }

    @Test(dataProvider = "invalidLogin", groups = {"loginCombinations"},
            description = "Should not be able to log in with invalid user, invalid password or combination of both")
    @Story("User logs in with invalid credentials")
    public void unsuccessfulLoginWithWrongCredentialsTest(String email, String password, String testScenario) {
         Assert.assertEquals(loginPage.getText(loginHeading), "Login");

        loginPage.login(email, password);
        PlaywrightMethods.waitTime(2000);

        boolean isEmailErrorVisible = loginPage.isVisible(emailError);
        boolean isPasswordErrorVisible = loginPage.isVisible(passwordError);
        boolean isInvalidEmailOrPasswordErrorVisible = loginPage.isVisible(invalidEmailOrPasswordError);

        if (isEmailErrorVisible) {
            System.out.println("Email Error: " + loginPage.getText(emailError));
        } else if (isPasswordErrorVisible) {
            System.out.println("Password Error: " + loginPage.getText(passwordError));
        } else if (isInvalidEmailOrPasswordErrorVisible) {
            System.out.println("Invalid email or password: " + loginPage.getText(invalidEmailOrPasswordError));
        }

        Assert.assertTrue(
                isEmailErrorVisible || isPasswordErrorVisible || isInvalidEmailOrPasswordErrorVisible,
                "Expected at least one validation error, but none appeared."
        );

        System.out.println(testScenario);
    }
}
