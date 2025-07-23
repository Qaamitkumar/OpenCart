package testCases;

import org.testng.Assert;
import org.testng.annotations.Test;

import pageObjects.HomePage;
import pageObjects.LoginPage;
import pageObjects.MyAccountPage;
import testBase.BaseClass;
import utilities.DataProviders;

// Data is valid -- login success -- test pass -- logout
// Data is valid --- login unsuccessful-- test failed

//Data is invalid --- login success -- test fails --- logout
//Data is invalid --- login unsuccessful -- test pass
public class TC003_LoginDDT extends BaseClass {
	
	       @Test(dataProvider="LoginData",dataProviderClass = DataProviders.class,groups="Datadriven")//getting data providers from different class
	       public void verify_loginDDT(String email,String pwd,String exp) throws InterruptedException 
	       {
	    	
	    	logger.info("******* Statrting TC_003_LoginDDT *******");
	    	
	    	try {
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
	   		
	   		if(exp.equalsIgnoreCase("Valid")) 
	   		{
	   			if(targetPage==true) 
	   			{
	   				macc.clickLogout();
	   				Assert.assertTrue(true);
	   				
	   			}
	   			else 
	   			{
	   				Assert.assertTrue(false);
	   			}
	   		}
	   		if(exp.equalsIgnoreCase("Invalid")) 
	   		{
	   			if(targetPage==true) 
	   			{
	   				macc.clickLogout();
	   				Assert.assertTrue(false);
	   				
	   			}
	   			else 
	   			{
	   				Assert.assertTrue(true);
	   			}
	   		}
	    	}catch(Exception e)
	   		{
	   			Assert.fail();
	   			}
	   		Thread.sleep(3000);
	   		logger.info("******* Finished TC_003_LoginDDT *******");
	   		
	       }

}
