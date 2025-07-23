package testCases;

import org.testng.Assert;
import org.testng.annotations.Test;

import pageObjects.HomePage;
import pageObjects.LoginPage;
import pageObjects.MyAccountPage;
import testBase.BaseClass;

public class TC002_LoginTest extends BaseClass{
	
	@Test(groups={"Sanity","master"})
	public void verify_login() 
	{
		logger.info("******* Statrting TC_002_LoginTest *******");
		
		try 
		{
		//HomePage
		HomePage hp =new HomePage(driver);
		hp.clickMyAccount();
		hp.clickLogin();
		
		//LoginPage
		LoginPage lp =new LoginPage(driver);
		lp.setEmail(p.getProperty("email"));
		lp.setPassword(p.getProperty("password"));
		lp.clickLogin();
		
		//MyAccountPAge
		MyAccountPage macc =new MyAccountPage(driver);
		boolean targetPage =macc.isMyAccountPageExists();

		Assert.assertEquals(targetPage, true,"Login failed");
		}
		catch(Exception e) 
		{
			Assert.fail();
		}
		logger.info("******* Finished TC_002_LoginTest *******");

     }
}
