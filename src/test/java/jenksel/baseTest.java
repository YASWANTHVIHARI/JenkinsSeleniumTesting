package jenksel;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;

import org.testng.ITestResult;
import org.testng.annotations.*;

import com.aventstack.extentreports.*;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.io.FileUtils;

public class baseTest {

	protected WebDriver driver;
	protected ExtentReports extent;
	protected ExtentTest test;

	@BeforeTest
	public void setupReport() {
		String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
		String reportPath = "test-output/ExtentReport_" + timestamp + ".html";

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
		options.addArguments("--headless"); // remove this if you want visible browser
		driver = new ChromeDriver(options);
		driver.manage().window().maximize();
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
}
