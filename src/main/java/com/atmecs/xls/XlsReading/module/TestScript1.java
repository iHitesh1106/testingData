package com.atmecs.xls.XlsReading.module;

import java.util.Set;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.testng.annotations.Test;
import com.atmecs.falcon.automation.appium.manager.UserBaseTest;
import io.appium.java_client.MobileElement;
import io.appium.java_client.TouchAction;
import io.appium.java_client.android.AndroidElement;


public class TestScript1 extends UserBaseTest 
{

	/**
	 * The following test is sample login test
	 * **/
	
	@Test(dataProvider="logindata",dataProviderClass=ExelUtils.class)
	public void testLogin(String userName, String password) 
	{
		report.info("Passing UserName");
		AndroidElement emailTest = (AndroidElement) driver.findElement(By.id("atmecs.com.logintask:id/email"));
		emailTest .sendKeys(userName);
		
		report.info("Passing password");
		AndroidElement passWord = (AndroidElement) driver.findElement(By.id("atmecs.com.logintask:id/password"));
		passWord.sendKeys(password);
		
		report.info("Logged In");
		AndroidElement login = (AndroidElement) driver.findElement(By.xpath("//android.widget.Button[@index='0']"));
		login.click();
		
		report.info("LogOut");
		AndroidElement logOut = (AndroidElement) driver.findElement(By.xpath("//android.widget.Button[@index='2']"));
		logOut.click();
		
	}
	
	
}
