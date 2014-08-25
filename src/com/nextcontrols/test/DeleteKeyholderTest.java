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

public class DeleteKeyholderTest extends TestCase {
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
	public void testDeleteKeyholder() throws Exception {
		driver.get(baseUrl + "/keyholders/Login.jsf");
		driver.findElement(By.id("j_idt7:username")).clear();
		driver.findElement(By.id("j_idt7:username")).sendKeys("akhurram");
		driver.findElement(By.id("j_idt7:password")).clear();
		driver.findElement(By.id("j_idt7:password")).sendKeys("adnaan_823");
		driver.findElement(By.name("j_idt7:j_idt20")).click();
//		driver.findElement(
//				By.xpath("//a[@onclick=\"mojarra.jsfcljs(document.getElementById('frmquotationPage'),{'frmquotationPage:tbDepartmentList:1:j_idt31':'frmquotationPage:tbDepartmentList:1:j_idt31'},'');return false\"]"))
//				.click();
		driver.findElement(By.id("frmWebsitesPage:tbWebsiteList:0:viewWebsite")).click();
	    driver.findElement(By.id("frmDepartmentsPage:tbDepartmentList:0:viewKeyholders")).click();
		String buttonName = "";
		// By element=By.id("tbKeyholdersList:0:contactname");
		By element = By.id("tbKeyholdersList:0:deleteKeyholder");
		WebElement elmnt = driver.findElement(element);
		// System.out.println("text name: "+element);
		int i = 1;
		int counter = 1;
		boolean flagElementExistence = true;
		String keyholderName = "";
		while (flagElementExistence) {

			int count = 0;
			while (count < 4) {
				try {
					keyholderName = driver.findElement(
							By.id("tbKeyholdersList:" + (i - 1)
									+ ":contactname")).getText();
					if (!"".equals(keyholderName)) {
						count = count + 4;
					} else
						count = count + 1;
//					 System.out.println("text : "+keyholderName+"--"+"i:"+i);

				} catch (StaleElementReferenceException e) {
					e.toString();
					// System.out.println("Trying to recover from a stale element :"
					// + e.getMessage());
					count = count + 1;
				} catch (NoSuchElementException e) {
					flagElementExistence = false;
					count=count+4;
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
				((JavascriptExecutor) driver).executeScript(
						"arguments[0].scrollTop = arguments[1];",
						driver.findElements(
								By.className("ui-datatable-scrollable-body"))
								.get(1), (40 * counter));
				counter++;

			}

			// if("newTesting".equals(driver.findElement(element).getText())){
			// buttonName="tbKeyholdersList:"+(i-1)+":j_idt163";
			// System.out.println("button name: "+buttonName);
			// break;
			// }
//			 System.out.println("element: "+element);

			try {
				elmnt = driver.findElement(element);
//				 System.out.println("text name: "+keyholderName);
				if ("zzz9".equals(keyholderName)){
					buttonName = "tbKeyholdersList:" + (i - 1)
							+ ":deleteKeyholder";
//					System.out.println("selected button name: "+buttonName);
				}

			} catch (NoSuchElementException e) {
				flagElementExistence = false;
			}
			element = By.id("tbKeyholdersList:" + i + ":deleteKeyholder");
			i++;

		}
//		System.out.println("button: "+buttonName);
		if (!"".equals(buttonName)) {
			 
			// WebDriverWait wait = new WebDriverWait(driver, 300);
			// wait.until(ExpectedConditions
			// .visibilityOfElementLocated(By.id(buttonName))).click();
			driver.findElement(By.id(buttonName)).click();
			int count = 0;
			while (count < 4) {
				try {
					driver.findElement(By.id("deleteKeyholderBtn")).click();
//					System.out.println(count);
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
		/////////////////////Asserting 
//		
//		int testI = 1;
//		boolean testFlagElementExistence = true;
//		String testKeyholderName = "";
//		boolean isNewPresent=false;
//		while (testFlagElementExistence) {
//			int count = 0;
//			while (count < 4) {
//				try {
//					testKeyholderName = driver.findElement(
//							By.id("tbKeyholdersList:" + (testI - 1)
//									+ ":contactname")).getText();
//					if ("zzz9".equals(keyholderName)) {
//						isNewPresent= true;
//					}
//					if (!"".equals(testKeyholderName)) {
//						count = count + 4;
//					} else
//						count = count + 1;
//				} catch (StaleElementReferenceException e) {
//					e.toString();
//					count = count + 1;
//				} catch (NoSuchElementException e) {
//					testFlagElementExistence = false;
//					count = count + 4;
//				}
//
//			}
//		
//
//		}
//		
//		Assert.assertFalse(isNewPresent);
		Thread.sleep(1000);
		boolean isDeleted=false;
		try{
			driver.findElement(By.id(buttonName));
		}catch (NoSuchElementException e) {
			isDeleted = true;
		}
		Assert.assertTrue(isDeleted);
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
