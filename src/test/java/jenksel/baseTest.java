package jenksel;

import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import org.testng.ITestResult;
import org.testng.annotations.*;

import com.aventstack.extentreports.*;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Date;

import org.apache.commons.io.FileUtils;

public class baseTest {

    protected WebDriver driver;
    protected WebDriverWait wait;
    protected ExtentReports extent;
    protected ExtentTest test;

    @BeforeTest
    public void setupReport() {
        String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String reportDir = "test-output/extent-reports/";
        new File(reportDir).mkdirs(); // Ensure folder exists
        String reportPath = reportDir + "ExtentReport_" + timestamp + ".html";

        ExtentSparkReporter sparkReporter = new ExtentSparkReporter(reportPath);
        sparkReporter.config().setReportName("Automation Test Report");
        sparkReporter.config().setDocumentTitle("Extent Report");

        extent = new ExtentReports();
        extent.attachReporter(sparkReporter);
        extent.setSystemInfo("Environment", "QA");
        extent.setSystemInfo("Tester", "Your Name");
    }

    @BeforeMethod
    public void setupDriver() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--headless"); // Remove this line to run with visible browser
        driver = new ChromeDriver(options);
        driver.manage().window().maximize();

        // WebDriverWait with 10-second timeout
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    @AfterMethod
    public void tearDownMethod(ITestResult result) throws IOException {
        if (result.getStatus() == ITestResult.FAILURE) {
            test.fail("❌ Test Failed: " + result.getThrowable());

            String screenshotPath = takeScreenshot(result.getName());
            test.addScreenCaptureFromPath(screenshotPath);
        } else if (result.getStatus() == ITestResult.SUCCESS) {
            test.pass("✅ Test Passed");
        } else if (result.getStatus() == ITestResult.SKIP) {
            test.skip("⚠️ Test Skipped: " + result.getThrowable());
        }

        if (driver != null) {
            driver.quit();
        }
    }

    @AfterTest
    public void tearDownReport() {
        if (extent != null) {
            extent.flush();
        }
    }

    // Screenshot utility
    public String takeScreenshot(String testName) {
        String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String screenshotDir = "test-output/screenshots/";
        String screenshotPath = screenshotDir + testName + "_" + timestamp + ".png";

        new File(screenshotDir).mkdirs(); // Ensure directory exists

        File src = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        File dest = new File(screenshotPath);

        try {
            FileUtils.copyFile(src, dest);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return screenshotPath;
    }

    // Wait for element by ID
    public WebElement waitForElementById(String id) {
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.id(id)));
    }

    // Safe check for element presence
    public boolean isElementPresentById(String id) {
        try {
            wait.until(ExpectedConditions.presenceOfElementLocated(By.id(id)));
            return true;
        } catch (TimeoutException e) {
            System.out.println("Element with ID '" + id + "' not found within timeout.");
            return false;
        }
    }
}