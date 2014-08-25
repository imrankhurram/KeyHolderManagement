package com.nextcontrols.test;

import static org.junit.Assert.fail;

import java.util.concurrent.TimeUnit;

import junit.framework.Assert;
import junit.framework.TestCase;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.Select;

public class NewCallListTest extends TestCase{
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
  public void testNewCallList() throws Exception {
    driver.get(baseUrl + "/keyholders/Login.jsf");
    driver.findElement(By.id("j_idt7:username")).clear();
    driver.findElement(By.id("j_idt7:username")).sendKeys("akhurram");
    driver.findElement(By.id("j_idt7:password")).clear();
    driver.findElement(By.id("j_idt7:password")).sendKeys("adnaan_823");
    driver.findElement(By.name("j_idt7:j_idt20")).click();
//    driver.findElement(By.xpath("//a[@onclick=\"mojarra.jsfcljs(document.getElementById('frmquotationPage'),{'frmquotationPage:tbDepartmentList:1:j_idt31':'frmquotationPage:tbDepartmentList:1:j_idt31'},'');return false\"]")).click();
    driver.findElement(By.id("frmWebsitesPage:tbWebsiteList:0:viewWebsite")).click();
    driver.findElement(By.id("frmDepartmentsPage:tbDepartmentList:0:viewKeyholders")).click();
    driver.findElement(By.id("showCallListPage")).click();
    driver.findElement(By.id("addCallListBtn")).click();
    int count=0;
    while(count<4){
    	try{
			 driver.findElement(By.name("addKeyHldsList:selectListType_editableInput")).clear();
			 driver.findElement(By.name("addKeyHldsList:selectListType_editableInput")).sendKeys("listtypeTesting");
			 count=count+4;
    	} catch (StaleElementReferenceException e){ 
    		e.toString(); 
    		System.out.println("Trying to recover from a stale element :" + e.getMessage()); 
    		count = count+1; 
    	}
    	
    }
	 count=0;
    while (count < 4){ 
    	try { 
    		 driver.findElement(By.id("addKeyHldsList:listDescription")).click();
    		 driver.findElement(By.id("addKeyHldsList:listDescription")).clear();
    		 driver.findElement(By.id("addKeyHldsList:listDescription")).sendKeys("this is a list type for testing");
//    		 driver.findElement(By.id("addKeyHldsList:listDescription")).click();
    		 driver.findElement(By.id("addKeyHldsList:listName")).clear();
    		 driver.findElement(By.id("addKeyHldsList:listName")).sendKeys("zzzList");
    		 new Select(driver.findElement(By.id("addKeyHldsList:startTimeHour"))).selectByVisibleText("01");
    		 new Select(driver.findElement(By.id("addKeyHldsList:startTimeMinutes"))).selectByVisibleText("01");
    		 new Select(driver.findElement(By.id("addKeyHldsList:endTimeHour"))).selectByVisibleText("19");
    		 new Select(driver.findElement(By.id("addKeyHldsList:endTimeMinutes"))).selectByVisibleText("18");
    		 driver.findElement(By.id("addKeyHldsList:startDate_input")).click();
    		 driver.findElement(By.linkText("6")).click();
    		 driver.findElement(By.id("addKeyHldsList:endDate_input")).click();
    		 driver.findElement(By.linkText("8")).click();
    		 driver.findElement(By.id("addKeyHldsList:saveCallListBtn")).click();
    		count=count+4;
    	} catch (StaleElementReferenceException e){ 
    		e.toString(); 
    		System.out.println("Trying to recover from a stale element :" + e.getMessage()); 
    		count = count+1; 
    	}
    	
    }
    ///////////////////Asserting results
//    driver.findElement(By.id("showHolidayCallListsBtn")).click();
// 	boolean buttonPresent=false;
// 	By element = By.id("tbKeyholderCallList:0:editCallList");
// 	int i = 1;
// 	int counter = 1;
// 	boolean flagElementExistence = true;
// 	String keyholderName = "";
// 	while (flagElementExistence) {
// 		 count = 0;
// 		while (count < 4) {
// 			try {
// 				keyholderName = driver.findElement(
// 						By.id("tbKeyholderCallList:" + (i - 1)
// 								+ ":listname")).getText();
// 				if (!"".equals(keyholderName)) {
// 					count = count + 4;
// 				} else
// 					count = count + 1;
// 				// System.out.println("text : "+keyholderName+"--"+"i:"+i);
//
// 			} catch (StaleElementReferenceException e) {
// 				e.toString();
// 				// System.out.println("Trying to recover from a stale element :"
// 				// + e.getMessage());
// 				count = count + 1;
// 			} catch (NoSuchElementException e) {
// 				flagElementExistence = false;
// 				count = count + 4;
// 			}
//
// 		}
// 		if (i >= 6) {
// 			((JavascriptExecutor) driver).executeScript(
// 					"arguments[0].scrollTop = arguments[1];",
// 					driver.findElement(By
// 							.className("ui-datatable-scrollable-body")),
// 					(40 * counter));
// 			((JavascriptExecutor) driver).executeScript(
// 					"arguments[0].scrollTop = arguments[1];",
// 					driver.findElements(
// 							By.className("ui-datatable-scrollable-body"))
// 							.get(1), (40 * counter));
// 			counter++;
//
// 		}
//
// 		try {
// 			driver.findElement(element);
// 			// System.out.println("text name: "+keyholderName);
// 			if ("zzzList".equals(keyholderName)) {
// 				buttonPresent=true;
// 			}
// 		} catch (NoSuchElementException e) {
// 			flagElementExistence = false;
// 		}
// 		element = By.id("tbKeyholderCallList:" + i + ":editCallList");
// 		i++;
//
// 	}
// 	
//     Assert.assertTrue(buttonPresent);
   
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
