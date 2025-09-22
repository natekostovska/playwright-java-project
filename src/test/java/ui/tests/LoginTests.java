package ui.tests;

import io.qameta.allure.Story;

import org.testng.Assert;
import org.testng.annotations.Ignore;
import ui.utils.DataSources;
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

    @Ignore
    @Story("User logs in with valid credentials")
    @Test(dataProvider = "validLogin", groups = {"loginCombinations"}, description = "Should log in with valid user and password")
    public void successfulLoginTest(String email, String password, String testScenario) {
        navigateToLogin();
        Assert.assertEquals(getText(loginHeading), "Login");
        login(email,password);
        Assert.assertEquals(getText(accountTitle), "My account");
        Assert.assertEquals(getText(loggedUserName).trim(), "Natasha Kostovska");
        System.out.println(testScenario);
    }

    @Story("User logs in with invalid credentials")
    @Test(dataProvider = "invalidLogin", groups = {"loginCombinations"}, description = "Should not be able to log in with invalid user, invalid password or combination of both")
    public void unsuccessfulLoginWithWrongCredentialsTest(String email, String password, String testScenario) {
        navigateToLogin();
        Assert.assertEquals(getText(loginHeading), "Login");
        login(email,password);
        waitTime(2000);

// Extract texts if present
        boolean isEmailErrorVisible = isVisible(emailError);
        boolean isPasswordErrorVisible = isVisible(passwordError);
        boolean isInvalidEmailOrPasswordErrorVisible = isVisible(invalidEmailOrPasswordError);

        if (isEmailErrorVisible) {
            System.out.println("Email Error: " + getText(emailError));
        }
        if (isPasswordErrorVisible) {
            System.out.println("Password Error: " + getText(passwordError));
        }
        if (isInvalidEmailOrPasswordErrorVisible) {
            System.out.println("Invalid email or password: " + getText(invalidEmailOrPasswordError));
        }
        // Assertion: At least one of them must be visible
        Assert.assertTrue(isEmailErrorVisible || isPasswordErrorVisible || isInvalidEmailOrPasswordErrorVisible,
                "Expected at least one validation error, but none appeared.");
        System.out.println(testScenario);
    }

}
