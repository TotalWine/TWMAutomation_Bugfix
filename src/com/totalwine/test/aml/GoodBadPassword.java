package com.totalwine.test.aml;

/*
 * User Types Workflow
 * Workflow:
 * 	1. Login with the following username/password combinations for test account: rajatsud@yahoo.com/Grapes123@
 * 		a. rajatsudyahoo.com/Grapes123@ - Improperly formatted email address/typo
 * 		b. rajatsud@yahoo.com/GRAPES123@ - All caps password
 * 		c. rajatsud@yahoo.com/Grapes123 - Missing special character in password
 * 		d. rajatsud@yahoo.com/grapes123@ - Missing caps in password
 * 		d. RAJATSUD@YAHOO.COM/Grapes123@ - All caps email
 * 		e. rajatsud@yahoo.com/Grapes123@ - Correct 
 *		
 * Technical Modules:
 * 	1. BeforeMethod (Test Pre-requisites):
 * 			Invoke webdriver
 * 			Maximize browser window
 * 	2. Test (Workflow)
 * 	3. AfterMethod
 * 			Take screenshot
 * 			Close webdriver
 * 	4. AfterClass
 * 			Quit webdriver
 */
//@author=rsud
import org.testng.*;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.openqa.selenium.By;
import org.testng.annotations.BeforeMethod;

import com.relevantcodes.extentreports.LogStatus;
import com.totalwine.test.actions.SiteAccess;
import com.totalwine.test.config.ConfigurationFunctions;
import com.totalwine.test.pages.PageGlobal;
import com.totalwine.test.trials.Browser;


public class GoodBadPassword extends Browser {
	
	private String IP="71.193.51.0";
	private String AMLPageHeading = "div.ahp-heading";
	
	@BeforeMethod
	  public void setUp() throws Exception {
	    driver.manage().window().maximize();
	  }  
	
	@DataProvider(name="UserPwdParameters")
    public Object[][] createData() {
    	Object[][] retObjArr=ConfigurationFunctions.getTableArray(ConfigurationFunctions.resourcePath,"aml", "goodbadpwdBF");
        return(retObjArr);
    }
	
	@Test (dataProvider = "UserPwdParameters")
	public void GoodBadPasswordTest (String email,String pwd,String valid) throws InterruptedException {
		logger=report.startTest("Good/Bad Password Combinations");
		SiteAccess.ActionAccessSite(driver, IP);
	    
	    //Access the sign in modal
	    driver.findElement(PageGlobal.TopNavAccount).click();
	    Thread.sleep(2000);
	    driver.findElement(By.cssSelector("a.btn.btn-red.acc-link.analyticsSignIn")).click();
	    
	    //Enter the email/password combination
	    driver.switchTo().frame("iframe-signin-overlay");
	    driver.findElement(By.id("j_username")).clear();
	    driver.findElement(By.id("j_username")).sendKeys(email);
	    driver.findElement(By.id("j_password")).clear();
	    driver.findElement(By.id("j_password")).sendKeys(pwd);
	    driver.findElement(By.xpath("//button[@type='button']")).click();
	    Thread.sleep(6000);
	    
	    //Validate the outcome
	    if (valid.equals("E")) {
	    	Assert.assertEquals(driver.findElements(By.cssSelector(AMLPageHeading)).isEmpty(), true);
	    	Assert.assertEquals(driver.findElement(By.cssSelector("p.error-msg")).getText(), 
	    			"Please enter a valid email address in the format example@domain.com"); //Invalid email format validation
	    	logger.log(LogStatus.PASS, "Invalid email format checked successfully");
	    }
	    else if (valid.equals("Y")) {
	    	Assert.assertEquals(driver.findElement(By.cssSelector(AMLPageHeading)).getText(), "Account Home");
	    	logger.log(LogStatus.PASS, "Account homepage display for valid login credentials");
	    }
	    else {
	    	Assert.assertEquals(driver.findElements(By.cssSelector(AMLPageHeading)).isEmpty(), true);
	    	Assert.assertEquals(driver.findElement(By.cssSelector("p.error-msg")).getText(), 
	    			"Sorry, the user name or password entered is incorrect. Please try again."); // Invalid password validation
	    	logger.log(LogStatus.PASS, "Error notification for invalid login credentials");
	    }
	}

}
