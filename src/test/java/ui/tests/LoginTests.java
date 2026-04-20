package ui.tests;

import io.qameta.allure.Story;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import ui.utils.DataSources;

import static ui.locators.LoginPage.*;

public class LoginTests extends BaseTest {

    @DataProvider(name = "validLogin")
    public Object[][] validUsers() {
        return DataSources.excel(
                "src/test/resources/data/loginTestDataPW.xlsx",
                "validUsers"
        );
    }

    @DataProvider(name = "invalidLogin")
    public Object[][] invalidUsers() {
        return DataSources.excel(
                "src/test/resources/data/loginTestDataPW.xlsx",
                "invalidUsers"
        );
    }

    @Test(dataProvider = "validLogin",groups = {"loginCombinations"},description = "Should log in with valid user and password")
    @Story("User logs in with valid credentials")
    public void successfulLoginTest(String email, String password, String testScenario) {

        pages.getLoginPage().navigateToLogin();
        Assert.assertTrue(pages.getLoginPage().isVisible(loginHeading), "Login page is not visible");
        pages.getLoginPage().login(email, password);
        Assert.assertEquals(pages.getLoginPage().getText(accountTitle),"My account","Account title mismatch");
        Assert.assertTrue(pages.getLoginPage().getText(loggedUserName).contains("Jane Doe"), "Logged-in user name mismatch");
        System.out.println(testScenario);
    }

    @Test(
            dataProvider = "invalidLogin",
            groups = {"loginCombinations"},
            description = "Should not be able to log in with invalid credentials"
    )
    @Story("User logs in with invalid credentials")
    public void unsuccessfulLoginTest(String email,String password,String testScenario)
    {
        pages.getLoginPage().navigateToLogin();
        Assert.assertTrue(pages.getLoginPage().isVisible(loginHeading),"Login page is not visible");
        pages.getLoginPage().login(email, password);
        pages.getLoginPage().waitForLoginError();
        Assert.assertTrue(pages.getLoginPage().isLoginErrorVisible(), "Login error message was not visible");
        System.out.println(testScenario);
    }
}
