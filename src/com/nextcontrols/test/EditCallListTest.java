package com.nextcontrols.test;

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
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;

public class EditCallListTest extends TestCase {
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
  public void testEditCallList() throws Exception {
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
    driver.findElement(By.id("showHolidayCallListsBtn")).click();
    /////////////////
	String buttonName = "";
	// By element=By.id("tbKeyholdersList:0:contactname");
	By element = By.id("tbKeyholderCallList:0:editCallList");
	WebElement elmnt = driver.findElement(element);
	// System.out.println("text name: "+element);
	int i = 1;
	int counter = 1;
	boolean flagElementExistence = true;
	String listName = "";
	while (flagElementExistence) {

		int count = 0;
		while (count < 4) {
			try {
				listName = driver.findElement(
						By.id("tbKeyholderCallList:" + (i - 1)
								+ ":listname")).getText();
				if (!"".equals(listName)) {
					count = count + 4;
				} else
					count = count + 1;
				// System.out.println("text : "+keyholderName+"--"+"i:"+i);

			} catch (StaleElementReferenceException e) {
				e.toString();
				// System.out.println("Trying to recover from a stale element :"
				// + e.getMessage());
				count = count + 1;
			} catch (NoSuchElementException e) {
				flagElementExistence = false;
				count = count + 4;
			}

		}
		if (i >= 6) {
			// WebElement elemnt = driver.findElement(element);
			// int elementPosition = elemnt.getLocation().getY();
			// System.out.println("element position: "+elementPosition);
			// String js =
			// driver.findElement(By.className("ui-datatable-scrollable-body"))+".scrollTop = "+elementPosition+";";
			// System.out.println("js: "+js);
			// System.out.println("scrolling at: "+(40*counter));
			((JavascriptExecutor) driver).executeScript(
					"arguments[0].scrollTop = arguments[1];",
					driver.findElement(By
							.className("ui-datatable-scrollable-body")),
					(40 * counter));
//			
			counter++;

		}

		// if("newTesting".equals(driver.findElement(element).getText())){
		// buttonName="tbKeyholdersList:"+(i-1)+":j_idt163";
		// System.out.println("button name: "+buttonName);
		// break;
		// }
		// System.out.println("element: "+element);

		try {
			elmnt = driver.findElement(element);
			// System.out.println("text name: "+keyholderName);
			if ("zzzList".equals(listName)) {
				buttonName = "tbKeyholderCallList:" + (i - 1)
						+ ":editCallList";
				// System.out.println("selected button name: "+buttonName);
			}

		} catch (NoSuchElementException e) {
			flagElementExistence = false;
		}
		element = By.id("tbKeyholderCallList:" + i + ":editCallList");
		i++;

	}
	// System.out.println("button: "+buttonName);
	if (!"".equals(buttonName)) {

		// WebDriverWait wait = new WebDriverWait(driver, 300);
		// wait.until(ExpectedConditions
		// .visibilityOfElementLocated(By.id(buttonName))).click();
		driver.findElement(By.id(buttonName)).click();
		int count = 0;
		while (count < 4) {
			try {
				driver.findElement(By.id("editKeyHldsList:listName")).clear();
				driver.findElement(By.id("editKeyHldsList:listName")).sendKeys(
						"zzz9List");
				driver.findElement(By.id("editKeyHldsList:updateCallListBtn")).click();

				count = count + 4;
			} catch (StaleElementReferenceException e) {
				e.toString();
				System.out
						.println("Trying to recover from a stale element :"
								+ e.getMessage());
				count = count + 1;
			}

		}

	}
	//////////////////////
    
    
//    driver.findElement(By.id("tbKeyholderCallList:2:editCallList")).click();
//    driver.findElement(By.name("editKeyHldsList:editSelectListType_editableInput")).clear();
//    driver.findElement(By.name("editKeyHldsList:editSelectListType_editableInput")).sendKeys("listtypeTesting2");
//    driver.findElement(By.id("editKeyHldsList:updateCallListBtn")).click();
	//////////////////Asserting results
	Thread.sleep(1000);
	((JavascriptExecutor) driver).executeScript(
			"arguments[0].scrollTop = arguments[1];",
			driver.findElement(By
					.className("ui-datatable-scrollable-body")),
			(1));
	
	boolean buttonPresent=false;
	boolean buttonNotPresent=true;
	By assertElement = By.id("tbKeyholderCallList:0:editCallList");
	int assertI = 1;
	int assertCounter = 1;
	boolean assertFlagElementExistence = true;
	String assertCallListName = "";
	while (assertFlagElementExistence) {
		int count = 0;
		while (count < 4) {
			try {
				assertCallListName = driver.findElement(
						By.id("tbKeyholderCallList:" + (assertI - 1)
								+ ":listname")).getText();
				if (!"".equals(assertCallListName)) {
					count = count + 4;
				} else
					count = count + 1;
				// System.out.println("text : "+keyholderName+"--"+"i:"+i);

			} catch (StaleElementReferenceException e) {
				e.toString();
				// System.out.println("Trying to recover from a stale element :"
				// + e.getMessage());
				count = count + 1;
			} catch (NoSuchElementException e) {
				assertFlagElementExistence = false;
				count = count + 4;
			}

		}
		if (assertI >= 6) {
			((JavascriptExecutor) driver).executeScript(
					"arguments[0].scrollTop = arguments[1];",
					driver.findElement(By
							.className("ui-datatable-scrollable-body")),
					(40 * assertCounter));
		
			assertCounter++;

		}

		try {
			driver.findElement(assertElement);
			 System.out.println("text name: "+assertCallListName);
			if ("zzz9List".equals(assertCallListName)) {
				buttonPresent=true;
			}
			if ("zzzList".equals(assertCallListName)) {
				buttonNotPresent=false;
			}
		} catch (NoSuchElementException e) {
			assertFlagElementExistence = false;
		}
		assertElement = By.id("tbKeyholderCallList:" + assertI + ":editCallList");
		assertI++;

	}
	Assert.assertTrue((buttonPresent && buttonNotPresent));
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
