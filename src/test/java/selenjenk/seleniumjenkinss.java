package selenjenk;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

public class seleniumjenkinss {

    WebDriver driver;

    @BeforeTest
    public void setup() {
        // Set Chrome options for Jenkins/CI environments
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--no-sandbox"); // Bypass OS security model
        options.addArguments("--disable-dev-shm-usage"); // Overcome limited resource problems
        options.addArguments("--headless"); // Run in headless mode for CI/CD
        driver = new ChromeDriver(options);
        
        // Maximize browser window
        driver.manage().window().maximize();
    }

    @Test
    public void google() throws Exception {
        driver.get("https://www.google.co.in/");
        driver.findElement(By.name("q")).sendKeys("Indiapost", Keys.ENTER);
        System.out.println("Google Search Title: " + driver.getTitle());
        Thread.sleep(2000);  // Ideally, replace with WebDriverWait
    }

    @Test
    public void testGoogle() {
        driver.get("https://www.facebook.com/");
        System.out.println("Facebook Title: " + driver.getTitle());
    }

    @Test
    public void testIndiapost() {
        driver.get("https://www.hdwallpapers.in/");
        System.out.println("HD Wallpapers Title: " + driver.getTitle());
    }

    @AfterTest
    public void tearDown() {
        if (driver != null) {
            driver.quit(); // Close the browser after tests
        }
    }
}