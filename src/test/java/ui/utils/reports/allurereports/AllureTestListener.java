package ui.utils.reports.allurereports;

import com.microsoft.playwright.Page;
import io.qameta.allure.Allure;
import io.qameta.allure.model.Status;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;
import ui.tests.BaseTest;

import java.io.ByteArrayInputStream;
import java.util.Date;

public class AllureTestListener implements ITestListener {

    public static String getScreenshotName(String methodName) {
        Date d = new Date();
        return methodName + "_" + d.toString().replace(":", "_").replace(" ", "_") + ".png";
    }

    @Override
    public void onTestStart(ITestResult result) {

        Object[] parameters = result.getParameters();

        if (parameters != null && parameters.length > 0) {
            String scenario = String.valueOf(parameters[parameters.length - 1]);

            String testName = result.getMethod().getMethodName() + " - " + scenario;

            result.setTestName(testName);

            Allure.step("Started test: " + testName);
        } else {
            String testName = result.getMethod().getMethodName();
            Allure.step("Started test: " + testName);
        }
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        Allure.step(
                "Test Method " + result.getMethod().getMethodName() + " was successful",
                Status.PASSED
        );
    }

    @Override
    public void onTestFailure(ITestResult result) {

        String methodName = result.getMethod().getMethodName();
        String exceptionMessage = result.getThrowable() != null
                ? result.getThrowable().toString()
                : "No exception";

        Allure.step(
                "Test failed: " + methodName + " | " + exceptionMessage,
                Status.FAILED
        );

        // SAFE: get Playwright page from BaseTest ThreadLocal
        Page page = BaseTest.getPage();

        if (page != null) {
            try {
                byte[] screenshot = page.screenshot(
                        new Page.ScreenshotOptions().setFullPage(true)
                );

                Allure.addAttachment(
                        getScreenshotName(methodName),
                        "image/png",
                        new ByteArrayInputStream(screenshot),
                        "png"
                );

            } catch (Exception e) {
                Allure.addAttachment(
                        "Screenshot failed",
                        "text/plain",
                        e.getMessage()
                );
            }
        } else {
            Allure.addAttachment(
                    "Page is null",
                    "text/plain",
                    "No Playwright page available in thread context"
            );
        }
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        Allure.step(
                "Test skipped: " + result.getMethod().getMethodName(),
                Status.SKIPPED
        );
    }

    @Override
    public void onTestFailedWithTimeout(ITestResult result) {
        onTestFailure(result);
    }

    @Override
    public void onFinish(ITestContext context) {
        System.out.println("Test suite finished: " + context.getName());
    }

    /**
     * Optional video attachment (safe version)
     */
    public void addVideoToAllure(String videoPath) {
        try {
            Allure.addAttachment("Test Video", "video/mp4", videoPath);
        } catch (Exception e) {
            Allure.addAttachment(
                    "Video attachment failed",
                    "text/plain",
                    e.getMessage()
            );
        }
    }
}