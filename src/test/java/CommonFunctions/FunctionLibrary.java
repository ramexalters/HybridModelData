package CommonFunctions;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Date;
import java.util.Properties;

import org.openqa.selenium.By;
import org.openqa.selenium.ElementClickInterceptedException;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.bidi.browsingcontext.Locator;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.Reporter;

import net.bytebuddy.asm.MemberSubstitution.Substitution.Chain.Step.ForField.Write;

public class FunctionLibrary 
{
	public static WebDriver driver;
	public static Properties conpro;
	 //Method for launch browser
	public static WebDriver startBrowser() throws Throwable
	
	{
		conpro=new Properties();
		conpro.load(new FileInputStream("./PropertyFiles/Environment.Properties"));
		if(conpro.getProperty("Browser").equalsIgnoreCase("chrome"))
		{
			driver=new ChromeDriver();
			driver.manage().window().maximize();
		}else if(conpro.getProperty("Browser").equalsIgnoreCase("firefox"))
		{
			driver=new FirefoxDriver();
		}
		return driver;
	}
  //method for launch url
	public static void openUrl()
	{
		driver.get(conpro.getProperty("Url"));
	}
	//method for wait for any webelement
	public static void waitForElement(String LocatorType,String LocatorValue,String TestData)
	{
		WebDriverWait mywait=new WebDriverWait(driver, Duration.ofSeconds(Integer.parseInt(TestData)));
		if(LocatorType.equalsIgnoreCase("xpath"))
		{
			mywait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(LocatorValue)));
		}
		if(LocatorType.equalsIgnoreCase("name"))
		{
			mywait.until(ExpectedConditions.visibilityOfElementLocated(By.name(LocatorValue)));
		}
		if(LocatorType.equalsIgnoreCase("id"))
		{
			mywait.until(ExpectedConditions.visibilityOfElementLocated(By.id(LocatorValue)));
		}
	}
	//method for any testbox
	public static void typeAction(String LocatorTYpe,String LocatorValue,String TestData)
	{
		if(LocatorTYpe.equalsIgnoreCase("id"))
		{
			driver.findElement(By.id(LocatorValue)).clear();
			driver.findElement(By.id(LocatorValue)).sendKeys(TestData);
		}
		if(LocatorTYpe.equalsIgnoreCase("name"))
		{
			driver.findElement(By.name(LocatorValue)).clear();
			driver.findElement(By.name(LocatorValue)).sendKeys(TestData);
		}
		if(LocatorTYpe.equalsIgnoreCase("xpath"))
		{
			driver.findElement(By.xpath(LocatorValue)).clear();
			driver.findElement(By.xpath(LocatorValue)).sendKeys(TestData);
		}
	}
	//methods for any link buttons radio buttions images and links
	public static void clickAction(String LocatorType,String LocatorValue)
	{
		if(LocatorType.equalsIgnoreCase("id"))
		{
			driver.findElement(By.id(LocatorValue)).sendKeys(Keys.ENTER);
		}
		if(LocatorType.equalsIgnoreCase("name"))
		{
			driver.findElement(By.name(LocatorValue)).click();
		}
		if(LocatorType.equalsIgnoreCase("xpath"))
		{
			driver.findElement(By.xpath(LocatorValue)).click();
		}
		
	}
	//Method for validate Title
	public static void validateTitle(String ExpTitle)
	{
		
		String ActTitile = driver.getTitle();
		try {
			Assert.assertEquals(ExpTitle, ActTitile,"Title is not matching");
			
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
	//Method for closebrowser
	public static void closeBrowser()
	{
		driver.quit();
	}
	//method fot dategenerator
	public static String generateDate()
	{
		Date dt = new Date();
		DateFormat df = new SimpleDateFormat("YYYY_MM_dd hh_mm");
		return df.format(dt);
	}
	//Method for list boxes
	public static void dropDownAction(String Ltype,String Lvalue,String TestData)
	{
		if(Ltype.equalsIgnoreCase("xpath"))
		{
			int value= Integer.parseInt(TestData);
			Select element = new Select(driver.findElement(By.xpath(Lvalue)));
			element.selectByIndex(value);
		}
		if(Ltype.equalsIgnoreCase("name"))
		{
			int value= Integer.parseInt(TestData);
			Select element = new Select(driver.findElement(By.name(Lvalue)));
			element.selectByIndex(value);
		}
		if(Ltype.equalsIgnoreCase("id"))
		{
			int value= Integer.parseInt(TestData);
			Select element = new Select(driver.findElement(By.id(Lvalue)));
			element.selectByIndex(value);
		}
	}
   //method for c apture for stock number in note pad
	public static void captureStock(String Ltype,String Lvalue) throws Throwable
	{
		String stocknum="";
		if(Ltype.equalsIgnoreCase("xpath"))
		{
			stocknum=driver.findElement(By.xpath(Lvalue)).getAttribute("value");
		}
		if(Ltype.equalsIgnoreCase("id"))
		{
			stocknum=driver.findElement(By.id(Lvalue)).getAttribute("value");
		}
		if(Ltype.equalsIgnoreCase("name"))
		{
			stocknum=driver.findElement(By.name(Lvalue)).getAttribute("value");
		}
		//capture notepad folder into caoture file
		FileWriter fw = new FileWriter("./CaptureData/StockNumbe.txt");
		BufferedWriter bw = new BufferedWriter(fw);
		bw.write(stocknum);
		bw.flush();
		bw.close();
		
	}
	//method for verify stock table
	public static void stockTable() throws Throwable
	{
		FileReader fr = new FileReader("./CaptureData/StockNumbe.txt");
		BufferedReader br = new BufferedReader(fr);
		String Exp_data =br.readLine();
		if(!driver.findElement(By.xpath(conpro.getProperty("Search-TextBox"))).isDisplayed())
			driver.findElement(By.xpath(conpro.getProperty("Search-Panel"))).click();
		driver.findElement(By.xpath(conpro.getProperty("Search-TextBox"))).clear();
		driver.findElement(By.xpath(conpro.getProperty("Search-TextBox"))).sendKeys(Exp_data);
		driver.findElement(By.xpath(conpro.getProperty("Search-button"))).click();
		Thread.sleep(3000);
		String Act_Data = driver.findElement(By.xpath("//table[@class='table ewTable']/tbody/tr[1]/td[8]/div/span/span")).getText();
		Reporter.log(Exp_data+"      "+Act_Data,true);
		try {
		Assert.assertEquals(Act_Data, Exp_data,"Stock number Not Matching");
		}catch (AssertionError a) {
			System.out.println(a.getMessage());
	}
 }
	//method for capture supplier number into notepad
	public static void capturesupplier(String Ltype,String Lvalue) throws Throwable
	{
		String suppliernum="";
		if(Ltype.equalsIgnoreCase("xpath"))
		{
			suppliernum=driver.findElement(By.xpath(Lvalue)).getAttribute("value");
		}
		if(Ltype.equalsIgnoreCase("id"))
		{
			suppliernum=driver.findElement(By.id(Lvalue)).getAttribute("value");
		}
		if(Ltype.equalsIgnoreCase("name"))
		{
			suppliernum=driver.findElement(By.name(Lvalue)).getAttribute("value");
		}
		FileWriter fw=new FileWriter("./CaptureData/supnumber.txt");
			BufferedWriter bw=new BufferedWriter(fw);
			bw.write(suppliernum);
			bw.flush();
			bw.close();
	}
	//method for read capture supplier number from notepad
	public static void supplierTable() throws Throwable
	{
		FileReader fr = new FileReader("./CaptureData/supnumber.txt");
		BufferedReader br=new BufferedReader(fr);
		String Exp_Data = br.readLine();
		if(!driver.findElement(By.xpath(conpro.getProperty("Search-TextBox"))).isDisplayed())
			driver.findElement(By.xpath(conpro.getProperty("Search-Panel"))).click();
		driver.findElement(By.xpath(conpro.getProperty("Search-TextBox"))).clear();
		driver.findElement(By.xpath(conpro.getProperty("Search-TextBox"))).sendKeys(Exp_Data);
		driver.findElement(By.xpath(conpro.getProperty("Search-button"))).click();
		Thread.sleep(4000);
		String Act_Data =driver.findElement(By.xpath("//table[@class='table ewTable']/tbody/tr[1]/td[6]/div/span/span")).getText();
		Reporter.log(Exp_Data+"        "+Act_Data,true);
		try {
			Assert.assertEquals(Act_Data, Exp_Data,"Supplier Number Is Not Matching");
		} catch (Exception e) {
			System.out.println(e.getMessage());
			
		}
	}
	//method for capture for customer number into notepad
	public static void captureCustomer(String Ltype,String Lvalue) throws Throwable
	{
		String customernum="";
		if(Ltype.equalsIgnoreCase("xpath"))
		{
			customernum=driver.findElement(By.xpath(Lvalue)).getAttribute("value");
		}
		if(Ltype.equalsIgnoreCase("id"))
		{
			customernum=driver.findElement(By.id(Lvalue)).getAttribute("value");
		}
		if(Ltype.equalsIgnoreCase("name"))
		{
			customernum=driver.findElement(By.name(Lvalue)).getAttribute("value");
		}
		FileWriter fw=new FileWriter("./CaptureData/cusnumber.txt");
		BufferedWriter bw=new BufferedWriter(fw);
		bw.write(customernum);
		bw.flush();
		bw.close();
	}
	//method for read customer number from notepad
	public static void customerTable() throws Throwable
	{
		FileReader fr=new FileReader("./CaptureData/cusnumber.txt");
		BufferedReader br=new BufferedReader(fr);
		String Exp_Data=br.readLine();
		if(!driver.findElement(By.xpath(conpro.getProperty("Search-TextBox"))).isDisplayed());
		driver.findElement(By.xpath(conpro.getProperty("Search-Panel"))).click();
		driver.findElement(By.xpath(conpro.getProperty("Search-TextBox"))).clear();
		driver.findElement(By.xpath(conpro.getProperty("Search-TextBox"))).sendKeys(Exp_Data);
		driver.findElement(By.xpath(conpro.getProperty("Search-button"))).click();
		Thread.sleep(4000);
		String Act_Data =driver.findElement(By.xpath("//table[@class='table ewTable']/tbody/tr[1]/td[5]/div/span/span")).getText();
		Reporter.log(Exp_Data+"        "+Act_Data,true);
		try {
			Assert.assertEquals(Act_Data, Exp_Data,"Customer Number Is Not Matching");
		} catch (Exception e) {
			System.out.println(e.getMessage());
			
		}
	}
}
