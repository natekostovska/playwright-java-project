package reports.extentreports;

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

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;

import static ui.tests.BaseTest.page;

public class TestListener implements ITestListener {

    private static ExtentManager ExtentManager;
    private static final ExtentReports extent = ExtentManager.createInstance();
    private static final ThreadLocal<ExtentTest> extentTest = new ThreadLocal<>();

    private static String getScreenshotName(String methodName) {
        Date d = new Date();
        return methodName + "_" + d.toString().replace(":", "_").replace(" ", "_") + ".png";
    }

    @Override
    public void onTestStart(ITestResult result) {
        Object[] parameters = result.getParameters();
        ExtentTest test;

        if (parameters.length > 0) {
            String scenario = (String) parameters[parameters.length - 1];
            String testName = result.getTestClass().getName() + " :: " + result.getMethod().getMethodName() + " - " + scenario;
            result.setTestName(testName);
            result.getMethod().setDescription(scenario);
            test = extent.createTest(testName);
        } else {
            String testName = result.getTestClass().getName() + " :: " + result.getMethod().getMethodName();
            test = extent.createTest(testName);
        }

        extentTest.set(test);
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        String logText = "<b>Test Method " + result.getMethod().getMethodName() + " Successful</b>";
        Markup markup = MarkupHelper.createLabel(logText, ExtentColor.GREEN);
        extentTest.get().log(Status.PASS, markup);
    }

    @Override
    public void onTestFailure(ITestResult result) {
        String methodName = result.getMethod().getMethodName();
        String exception = result.getThrowable() != null ? result.getThrowable().toString() : "No Exception";

        extentTest.get().fail("<details><summary><b><font color=red>Exception Occurred, click to expand</font></b></summary>"
                + exception.replaceAll(",", "<br>") + "</details> \n");

        // Capture and attach Playwright screenshot
        String screenshotPath = takePlaywrightScreenshot(methodName);
        if (screenshotPath != null) {
            extentTest.get().fail("<b><font color=red>Screenshot of failure</font></b>",
                    MediaEntityBuilder.createScreenCaptureFromPath(screenshotPath).build());
        }

        String logText = "<b>Method " + methodName + " Failed</b>";
        extentTest.get().log(Status.FAIL, MarkupHelper.createLabel(logText, ExtentColor.RED));
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        String logText = "<b>Test Method " + result.getMethod().getMethodName() + " Skipped</b>";
        Markup m = MarkupHelper.createLabel(logText, ExtentColor.YELLOW);
        extentTest.get().log(Status.SKIP, m);
    }

    @Override
    public void onTestFailedWithTimeout(ITestResult result) {
        onTestFailure(result);
    }

    @Override
    public void onStart(ITestContext context) {
        // Optional: add system info here
    }

    @Override
    public void onFinish(ITestContext context) {
        if (extent != null) {
            extent.flush();
        }
    }

    /**
     * Takes a screenshot using Playwright's Page and saves it to disk.
     *
     * @param methodName The name of the test method
     * @return relative path to screenshot or null if failed
     */
    public String takePlaywrightScreenshot(String methodName) {
        if (page == null) return null;

        String screenshotName = getScreenshotName(methodName);
        String directory = System.getProperty("user.dir") + "/extent/screenshots/";
        new File(directory).mkdirs();
        String path = directory + screenshotName;

        try {
            byte[] screenshotBytes = page.screenshot(new Page.ScreenshotOptions().setFullPage(true));
            try (FileOutputStream fos = new FileOutputStream(path)) {
                fos.write(screenshotBytes);
            }
            return "../screenshots/" + screenshotName; // for HTML relative path
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}
