package testBase;

import java.io.File;
import java.io.FileNotFoundException;
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
public Logger logger;  //log4j
public Properties p;
	
    @BeforeClass(groups={"Sanity","Regression","master"})
    @Parameters({"os","browser"})
	public void setup(String os,String br) throws IOException
	{
    	
    	//loading config.propeties file
    	FileReader file= new FileReader("./src//test//resources//config.properties");
    	p= new Properties();
    	p.load(file);
    	
    	//logs
    	logger = LogManager.getLogger(this.getClass());
    	
    	//running or launching browsers on selenium grid setup
    	if(p.getProperty("execution_env").equalsIgnoreCase("remote"))
    	{
    		DesiredCapabilities capabilities = new DesiredCapabilities();
    		//capabilities.setPlatform(Platform.WIN11);
    		//capabilities.
    		
    		
    		//os
    		if(os.equalsIgnoreCase("windows"))
    		{
    			capabilities.setPlatform(Platform.WINDOWS);
    		}
    		else if(os.equalsIgnoreCase("mac"))
    		{
    			capabilities.setPlatform(Platform.MAC);
    		}else if (os.equalsIgnoreCase("linux")) {
    		    capabilities.setPlatform(Platform.LINUX);
    		}else
    		{
    			System.out.println("No matching OS found");
    			return;
    		}
    		
    		//browser
    		switch(br.toLowerCase())
    		{
    		case "chrome":capabilities.setBrowserName("chrome");break;
    		case "edge":capabilities.setBrowserName("MicrosoftEdge");break;
    		case "firefox":capabilities.setBrowserName("firefox");break;
    		default:System.out.println("No matching browser");return;
    		}
    		
    		driver =new RemoteWebDriver(new URL("http://192.168.1.10:4444/wd/hub"),capabilities);
    	}
    	
    	
    	//running test on local env
    	
    	
    	if(p.getProperty("execution_env").equalsIgnoreCase("local"))
    	{
    		switch(br.toLowerCase()) 
        	{
        	   case "chrome":driver = new ChromeDriver(); break;
        	   case "edge":driver = new EdgeDriver();break;
        	   case "firefox":driver = new FirefoxDriver();break;
        	   default:System.out.println("Invalid browser name...");return;
        	   
        	}
    	}
    	
    	
    	
    	driver.manage().deleteAllCookies();
    	driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
    	//driver.get("https://tutorialsninja.com/demo/");
    	driver.get(p.getProperty("appURl")); ///reading URL from properties file
    	driver.manage().window().maximize();
	}
    
    @AfterClass(groups={"Sanity","Regression","master"})
	public void tearDown()
	{
		driver.quit();
	}
    
    public String randomeString() {
		// TODO Auto-generated method stub
		String generatedstring = RandomStringUtils.randomAlphabetic(5);
    	return generatedstring;
		
	}
	public String randomeNumber() {
		// TODO Auto-generated method stub
		String generatednum = RandomStringUtils.randomNumeric(10);
    	return generatednum;
		
	}
	public String randomeAlpaNumeric() {
		// TODO Auto-generated method stub
		String generatedstring = RandomStringUtils.randomAlphabetic(4);
		String generatednum = RandomStringUtils.randomNumeric(4);
    	return(generatedstring+"@"+generatednum);
		
	}
	
	public String captureScreen(String tname) throws IOException 
	{
		String timeStamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());// time stamp
		
		TakesScreenshot takesScreenshot = (TakesScreenshot)driver;
		File sourceFile = takesScreenshot.getScreenshotAs(OutputType.FILE);
		
		String targetFilePath = System.getProperty("user.dir")+"\\screenshots\\"+tname+"_"+timeStamp + ".png";
		File targetFile =new File(targetFilePath);
		
		sourceFile.renameTo(targetFile);
		
		return targetFilePath;
		
	}
    
}
