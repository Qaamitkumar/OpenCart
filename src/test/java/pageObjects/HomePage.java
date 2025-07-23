package pageObjects;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class HomePage extends BasePage {

	
	//constructor
	public HomePage(WebDriver  driver)
	{
		super(driver);
	}
	
	//My account link 
	@FindBy(xpath="//span[normalize-space()='My Account']")
	WebElement lnkMyaccount;
	
	@FindBy(xpath="//a[normalize-space()='Register']")
	WebElement lnkRegister;
	
	
	@FindBy(linkText = "Login") //login link added in step 5
	WebElement linkLogin;
	
	public void clickMyAccount()
	{
		lnkMyaccount.click();
	}
	
	public void clickRegister()
	{
		lnkRegister.click();
	}
	
	

	public void clickLogin() {
		// TODO Auto-generated method stub
		linkLogin.click();
	}
	
}
