package testBase;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Date;
import java.util.Properties;

import org.apache.commons.lang3.RandomStringUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.Platform;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Parameters;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class BaseClass {

    public static WebDriver driver;
    public Logger logger;
    public Properties p;

    @BeforeClass(groups = {"Sanity", "Regression", "master"})
    @Parameters({"os", "browser"})
    public void setup(String os, String br) throws IOException {

        // Load config.properties
        FileReader file = new FileReader("./src/test/resources/config.properties");
        p = new Properties();
        p.load(file);

        // Log4j logger
        logger = LogManager.getLogger(this.getClass());

        String execEnv = p.getProperty("execution_env").toLowerCase();
        String appUrl = p.getProperty("appURl");

        System.out.println("Execution Environment: " + execEnv);
        System.out.println("Target Browser: " + br);
        System.out.println("Target OS: " + os);

        if (execEnv.equals("remote")) {
            // Set desired capabilities
            DesiredCapabilities capabilities = new DesiredCapabilities();

            // OS platform
            switch (os.toLowerCase()) {
                case "windows":
                    capabilities.setPlatform(Platform.WINDOWS);
                    break;
                case "mac":
                    capabilities.setPlatform(Platform.MAC);
                    break;
                case "linux":
                    capabilities.setPlatform(Platform.LINUX);
                    break;
                default:
                    System.out.println("Invalid OS specified in parameters.");
                    return;
            }

            // Browser
            switch (br.toLowerCase()) {
                case "chrome":
                    capabilities.setBrowserName("chrome");
                    break;
                case "firefox":
                    capabilities.setBrowserName("firefox");
                    break;
                case "edge":
                    capabilities.setBrowserName("MicrosoftEdge");
                    break;
                default:
                    System.out.println("Invalid browser specified in parameters.");
                    return;
            }

            // Connect to Selenium Grid
            String gridUrl = p.getProperty("selenium_grid_url", "http://localhost:4444/wd/hub");
            System.out.println("Connecting to Selenium Grid at: " + gridUrl);

            driver = new RemoteWebDriver(new URL(gridUrl), capabilities);

        } else if (execEnv.equals("local")) {
            // Run tests locally
            switch (br.toLowerCase()) {
                case "chrome":
                    driver = new ChromeDriver();
                    break;
                case "firefox":
                    driver = new FirefoxDriver();
                    break;
                case "edge":
                    driver = new EdgeDriver();
                    break;
                default:
                    System.out.println("Invalid browser specified.");
                    return;
            }
        } else {
            System.out.println("Invalid execution_env in config.properties.");
            return;
        }

        // Common driver setup
        driver.manage().deleteAllCookies();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        driver.get(appUrl);
        driver.manage().window().maximize();
    }

    @AfterClass(groups = {"Sanity", "Regression", "master"})
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    // Utility methods
    public String randomeString() {
        return RandomStringUtils.randomAlphabetic(5);
    }

    public String randomeNumber() {
        return RandomStringUtils.randomNumeric(10);
    }

    public String randomeAlpaNumeric() {
        return RandomStringUtils.randomAlphabetic(4) + "@" + RandomStringUtils.randomNumeric(4);
    }

    public String captureScreen(String tname) throws IOException {
        String timeStamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());
        TakesScreenshot ts = (TakesScreenshot) driver;
        File sourceFile = ts.getScreenshotAs(OutputType.FILE);
        String targetPath = System.getProperty("user.dir") + "/screenshots/" + tname + "_" + timeStamp + ".png";
        File targetFile = new File(targetPath);
        sourceFile.renameTo(targetFile);
        return targetPath;
    }
}
