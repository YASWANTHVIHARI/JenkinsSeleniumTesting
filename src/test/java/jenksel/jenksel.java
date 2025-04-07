package jenksel;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.testng.annotations.Test;

public class jenksel extends baseTest {

    @Test
    public void googleSearchTest() throws Exception {
        test = extent.createTest("Google Search Test");
        driver.get("https://www.google.com/");
        test.info("Opened Google");
        driver.findElement(By.name("q")).sendKeys("India Post", Keys.ENTER);
        test.info("Searched for India Post");
        Thread.sleep(2000);
    }

    @Test
    public void facebookTitleTest() {
        test = extent.createTest("Facebook Title Test");
        driver.get("https://www.facebook.com/");
        test.info("Opened Facebook");
    }

    @Test
    public void wallpaperSiteTest() {
        test = extent.createTest("HD Wallpapers Site Test");
        driver.get("https://www.hdwallpapers.in/");
        test.info("Opened HD Wallpapers site");
    }

    @Test
    public void failingTest() {
        test = extent.createTest("Intentional Failing Test");
        driver.get("https://www.google.com/");
        test.info("Opened Google");

        // will wait up to 10 seconds
        waitForElementById("nonExistentElement").click();
    }
}