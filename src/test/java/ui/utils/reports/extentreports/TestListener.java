package ui.utils.reports.extentreports;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.markuputils.ExtentColor;
import com.aventstack.extentreports.markuputils.Markup;
import com.aventstack.extentreports.markuputils.MarkupHelper;
import com.microsoft.playwright.Page;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;
import ui.tests.BaseTest;
import ui.utils.reports.extentreports.ExtentManager;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;

public class TestListener implements ITestListener {

    private static final ExtentReports extent = ExtentManager.createInstance();
    private static final ThreadLocal<ExtentTest> extentTest = new ThreadLocal<>();

    private static String getScreenshotName(String methodName) {
        Date d = new Date();
        return methodName + "_" + d.toString().replace(":", "_").replace(" ", "_") + ".png";
    }

    // Safe getter
    private ExtentTest getTest() {
        return extentTest.get();
    }

    @Override
    public void onTestStart(ITestResult result) {

        String testName;

        Object[] parameters = result.getParameters();

        if (parameters != null && parameters.length > 0) {
            String scenario = String.valueOf(parameters[parameters.length - 1]);
            testName = result.getTestClass().getName()
                    + " :: " + result.getMethod().getMethodName()
                    + " - " + scenario;

            result.setTestName(testName);
            result.getMethod().setDescription(scenario);
        } else {
            testName = result.getTestClass().getName()
                    + " :: " + result.getMethod().getMethodName();
        }

        ExtentTest test = extent.createTest(testName);
        extentTest.set(test);
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        ExtentTest test = getTest();
        if (test != null) {
            String logText = "<b>Test Method " + result.getMethod().getMethodName() + " Successful</b>";
            test.log(Status.PASS, MarkupHelper.createLabel(logText, ExtentColor.GREEN));
        }
    }

    @Override
    public void onTestFailure(ITestResult result) {

        ExtentTest test = getTest();
        String methodName = result.getMethod().getMethodName();
        String exception = result.getThrowable() != null
                ? result.getThrowable().toString()
                : "No Exception";

        if (test != null) {
            test.fail("<details><summary><b><font color=red>Exception</font></b></summary>"
                    + exception.replaceAll(",", "<br>") + "</details>");
        }

        // screenshot (safe)
        String screenshotPath = takePlaywrightScreenshot(methodName);

        if (test != null && screenshotPath != null) {
            try {
                test.fail("<b><font color=red>Screenshot</font></b>",
                        MediaEntityBuilder.createScreenCaptureFromPath(screenshotPath).build());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if (test != null) {
            String logText = "<b>Method " + methodName + " Failed</b>";
            test.log(Status.FAIL, MarkupHelper.createLabel(logText, ExtentColor.RED));
        }
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        ExtentTest test = getTest();
        if (test != null) {
            String logText = "<b>Test Method " + result.getMethod().getMethodName() + " Skipped</b>";
            test.log(Status.SKIP, MarkupHelper.createLabel(logText, ExtentColor.YELLOW));
        }
    }

    @Override
    public void onTestFailedWithTimeout(ITestResult result) {
        onTestFailure(result);
    }

    @Override
    public void onStart(ITestContext context) {
        // optional setup
    }

    @Override
    public void onFinish(ITestContext context) {
        extent.flush();
    }

    /**
     * Playwright screenshot helper (SAFE)
     */
    public String takePlaywrightScreenshot(String methodName) {

        try {
            Page page = BaseTest.getPage(); // IMPORTANT: must be static accessor

            if (page == null) return null;

            String screenshotName = getScreenshotName(methodName);
            String directory = System.getProperty("user.dir") + "/extent/screenshots/";

            new File(directory).mkdirs();

            String path = directory + screenshotName;

            byte[] screenshotBytes = page.screenshot(
                    new Page.ScreenshotOptions().setFullPage(true)
            );

            try (FileOutputStream fos = new FileOutputStream(path)) {
                fos.write(screenshotBytes);
            }

            return "screenshots/" + screenshotName;

        } catch (IOException | RuntimeException e) {
            e.printStackTrace();
            return null;
        }
    }
}