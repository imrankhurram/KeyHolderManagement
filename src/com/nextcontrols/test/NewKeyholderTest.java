package com.nextcontrols.test;

import java.util.concurrent.TimeUnit;

import junit.framework.TestCase;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

public class NewKeyholderTest extends TestCase{
	private WebDriver driver;
	private String baseUrl;
	private boolean acceptNextAlert = true;
	private StringBuffer verificationErrors = new StringBuffer();

	@Before
	public void setUp() throws Exception {
		driver = new FirefoxDriver();
		baseUrl = "http://localhost:8081/";
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
	}

	@Test
  public void testNewKeyholder() throws Exception {
    driver.get(baseUrl + "/keyholders/Login.jsf");
    driver.findElement(By.id("j_idt7:username")).clear();
    driver.findElement(By.id("j_idt7:username")).sendKeys("akhurram");
    driver.findElement(By.id("j_idt7:password")).clear();
    driver.findElement(By.id("j_idt7:password")).sendKeys("adnaan_823");
    driver.findElement(By.name("j_idt7:j_idt20")).click();
    driver.findElement(By.id("frmWebsitesPage:tbWebsiteList:0:viewWebsite")).click();
    driver.findElement(By.id("frmDepartmentsPage:tbDepartmentList:0:viewKeyholders")).click();
//    driver.findElement(By.xpath("//a[@onclick=\"mojarra.jsfcljs(document.getElementById('frmquotationPage'),{'frmquotationPage:tbDepartmentList:1:j_idt31':'frmquotationPage:tbDepartmentList:1:j_idt31'},'');return false\"]")).click();
    driver.findElement(By.id("addKeyholderBtn")).click();
//    try{
    int count=0;
    while (count < 4){ 
    	try { 
  
    		driver.findElement(By.id("addkeyholderName")).clear();
    		driver.findElement(By.id("addkeyholderName")).sendKeys("zzz");
    		count=count+4;
    	} catch (StaleElementReferenceException e){ 
    		e.toString(); 
    		System.out.println("Trying to recover from a stale element :" + e.getMessage()); 
    		count = count+1; 
    	}
    	
    }
    driver.findElement(By.id("addtelephoneNo")).clear();
    driver.findElement(By.id("addtelephoneNo")).sendKeys("33");
    driver.findElement(By.id("addsmsNo")).clear();
    driver.findElement(By.id("addsmsNo")).sendKeys("44");
//    driver.findElement(By.id("j_idt97:0:newEmail")).clear();
//    driver.findElement(By.id("j_idt97:0:newEmail")).sendKeys("kool.khurram@gmail.com");
    driver.findElement(By.xpath("//div[@id='tbAddKeyholdersList:0:ena']/div[2]")).click();
    driver.findElement(By.xpath("//div[@id='tbAddKeyholdersList:0:vc']/div[2]")).click();
    driver.findElement(By.xpath("//div[@id='tbAddKeyholdersList:2:ena']/div[2]")).click();
    driver.findElement(By.xpath("//div[@id='tbAddKeyholdersList:2:sms']/div[2]")).click();
    driver.findElement(By.id("saveKeyholderBtn")).click();
//    }
//    catch(StaleElementReferenceException ex){
//    
//    	
//    }
    ///////////////////Asserting results
//	boolean buttonPresent=false;
//	By element = By.id("tbKeyholdersList:0:editKeyholder");
//	int i = 1;
//	int counter = 1;
//	boolean flagElementExistence = true;
//	String keyholderName = "";
//	while (flagElementExistence) {
//		 count = 0;
//		while (count < 4) {
//			try {
//				keyholderName = driver.findElement(
//						By.id("tbKeyholdersList:" + (i - 1)
//								+ ":contactname")).getText();
//				if (!"".equals(keyholderName)) {
//					count = count + 4;
//				} else
//					count = count + 1;
//				// System.out.println("text : "+keyholderName+"--"+"i:"+i);
//
//			} catch (StaleElementReferenceException e) {
//				e.toString();
//				// System.out.println("Trying to recover from a stale element :"
//				// + e.getMessage());
//				count = count + 1;
//			} catch (NoSuchElementException e) {
//				flagElementExistence = false;
//				count = count + 4;
//			}
//
//		}
//		if (i >= 6) {
//			((JavascriptExecutor) driver).executeScript(
//					"arguments[0].scrollTop = arguments[1];",
//					driver.findElement(By
//							.className("ui-datatable-scrollable-body")),
//					(40 * counter));
//			((JavascriptExecutor) driver).executeScript(
//					"arguments[0].scrollTop = arguments[1];",
//					driver.findElements(
//							By.className("ui-datatable-scrollable-body"))
//							.get(1), (40 * counter));
//			counter++;
//
//		}
//
//		try {
//			driver.findElement(element);
//			 System.out.println("text name: "+keyholderName);
//			if ("zzz".equals(keyholderName)) {
//				buttonPresent=true;
//			}
//		} catch (NoSuchElementException e) {
//			flagElementExistence = false;
//		}
//		element = By.id("tbKeyholdersList:" + i + ":editKeyholder");
//		i++;
//
//	}
//	
//    Assert.assertTrue(buttonPresent);
   
  }

	@After
	public void tearDown() throws Exception {
		driver.quit();
		String verificationErrorString = verificationErrors.toString();
		if (!"".equals(verificationErrorString)) {
			fail(verificationErrorString);
		}
	}

	private boolean isElementPresent(By by) {
		try {
			driver.findElement(by);
			return true;
		} catch (NoSuchElementException e) {
			return false;
		}
	}

	private boolean isAlertPresent() {
		try {
			driver.switchTo().alert();
			return true;
		} catch (NoAlertPresentException e) {
			return false;
		}
	}

	private String closeAlertAndGetItsText() {
		try {
			Alert alert = driver.switchTo().alert();
			String alertText = alert.getText();
			if (acceptNextAlert) {
				alert.accept();
			} else {
				alert.dismiss();
			}
			return alertText;
		} finally {
			acceptNextAlert = true;
		}
	}
}
