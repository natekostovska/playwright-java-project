package reports.allurereports;

import com.microsoft.playwright.Page;
import io.qameta.allure.Allure;
import io.qameta.allure.Attachment;
import io.qameta.allure.model.Status;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;
import ui.tests.BaseTest;

import java.io.ByteArrayInputStream;
import java.util.Date;

public class AllureTestListener  implements ITestListener {

    public static String getScreenshotName(String methodName) {
        Date d = new Date();
        return methodName + "_" + d.toString().replace(":", "_").replace(" ", "_") + ".png";
    }

    @Attachment(value = "{0}", type = "text/html")
    public static String attachHtml(String html) {
        return html;
    }

    @Override
    public void onTestStart(ITestResult result) {
        // Get parameters from DataProvider (if any)
        Object[] parameters = result.getParameters();

        // Check if the test is parameterized (i.e., it's using a DataProvider)
        if (parameters.length > 0) {
            String testScenario = (String) parameters[parameters.length - 1]; // Assume testScenario is the last parameter
            // Dynamically modify the test name with the testScenario value
            String newTestName = "Started test: " +result.getMethod().getMethodName() + " - " + testScenario;
            // Note: TestNG doesn't have a method to directly change the name in the ITestResult
            // However, we will rely on Allure to track the updated name as part of the logs
            Allure.step(newTestName);
            result.setTestName(newTestName); // This is mainly for logging purposes
        }
        else {
            // Using HTML to add a formatted message to Allure for test start
            String logText = "Started test: " + result.getTestClass().getName() + " :: " + result.getMethod().getMethodName();
            // Add step to Allure with the formatted start message
            Allure.step(logText);}
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        String logText = "Test Method " + result.getMethod().getMethodName() + " was successful.";
        Allure.step(logText, Status.PASSED);
    }

    @Override
    public void onTestFailure(ITestResult result) {
        String methodName = result.getMethod().getMethodName();
        String exceptionMessage = result.getThrowable() != null ? result.getThrowable().getMessage() : "No exception";

        Allure.step("Test method " + methodName + " failed. Exception: " + exceptionMessage, Status.FAILED);

        Page page = BaseTest.page;

        if (page != null) {
            try {
                byte[] screenshotBytes = page.screenshot(new Page.ScreenshotOptions().setFullPage(true));
                Allure.addAttachment(getScreenshotName(methodName), "image/png", new ByteArrayInputStream(screenshotBytes), "png");
            } catch (Exception e) {
                Allure.addAttachment("Screenshot failed", "text/plain", "Could not capture screenshot: " + e.getMessage());
            }
        } else {
            Allure.addAttachment("Page is null", "text/plain", "No Playwright page instance available.");
        }
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        String logText = "Test method " + result.getMethod().getMethodName() + " was skipped";
        Allure.step(logText, Status.SKIPPED);
    }

    @Override
    public void onTestFailedWithTimeout(ITestResult result) {
        onTestFailure(result);
    }

    @Override
    public void onFinish(ITestContext context) {
        System.out.println("Test suite finished: " + context.getName());
    }
}
