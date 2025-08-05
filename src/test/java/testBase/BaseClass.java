package testBase;

import java.io.FileInputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;
import java.util.Properties;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.annotations.*;

public class BaseClass {

    public static WebDriver driver;
    public Properties config;

    @BeforeClass
    public void loadConfig() {
        try {
            config = new Properties();
            FileInputStream fis = new FileInputStream("src/test/resources/config.properties");
            config.load(fis);
        } catch (Exception e) {
            System.err.println("❌ Failed to load config.properties: " + e.getMessage());
            throw new RuntimeException("Configuration load failed", e);
        }
    }

    @BeforeMethod
    public void setup() {
        String runMode = System.getProperty("runMode", "local");  // default: local
        String hubURL = System.getProperty("hubURL");             // passed via Jenkins
        String baseURL = config.getProperty("baseURL");

        try {
            if ("grid".equalsIgnoreCase(runMode)) {
                // If hubURL not passed via system property, fallback to config.properties
                if (hubURL == null || hubURL.isEmpty()) {
                    hubURL = config.getProperty("hubURL");
                }

                if (hubURL == null || hubURL.isEmpty()) {
                    throw new IllegalArgumentException("❌ 'hubURL' not defined. Pass via -DhubURL or config.properties.");
                }

                ChromeOptions options = new ChromeOptions();
                driver = new RemoteWebDriver(new URL(hubURL), options);
                System.out.println("✅ Running tests on Selenium Grid: " + hubURL);
            } else {
                driver = new ChromeDriver();
                System.out.println("✅ Running tests on local ChromeDriver");
            }

            driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
            driver.manage().window().maximize();
            driver.get(baseURL);

        } catch (MalformedURLException e) {
            throw new RuntimeException("❌ Invalid Grid URL: " + hubURL, e);
        } catch (Exception e) {
            throw new RuntimeException("❌ WebDriver setup failed: " + e.getMessage(), e);
        }
    }

    @AfterMethod
    public void tearDown() {
        if (driver != null) {
            driver.quit();
            System.out.println("🔄 Browser closed");
        }
    }
}
