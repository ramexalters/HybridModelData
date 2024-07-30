package Driver_factory;

import java.io.File;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;

import CommonFunctions.FunctionLibrary;
import Utilities.ExcelFileUtil;

public class DriverScript {
	WebDriver driver;
	String inputpath="./FileInput/xlformaven.xlsx";
	String outputpath="./FileOutput/Hybridmavenoutput.xlsx";
	String TCSheet="MasterTestCases";
	ExtentReports reports;
	ExtentTest logger;
	public  void startTest() throws Throwable
	{
		String Module_Status="";
		String Module_New="";
		//create object for file util class
		ExcelFileUtil xl= new ExcelFileUtil(inputpath);
		//iterate all rows in TCSheet
		int rc=xl.rowCount(TCSheet);
		for(int i=1;i<=rc;i++)
		{
			System.out.println("asdfgh");
			if(xl.getCellData(TCSheet, i, 2).equalsIgnoreCase("Y"))
			{
				//Read module cell from TCSheet
				String TCModule=xl.getCellData(TCSheet, i, 1);
				System.out.println("lkjhg");
				//Define html reports
				reports= new ExtentReports("./target/Reports/"+TCModule+FunctionLibrary.generateDate()+".html");
				logger=reports.startTest(TCModule);
				//Iterate all rows in TCModule
				for(int j=1;j<=xl.rowCount(TCModule);j++)
				{
					//Read cell form TCModule
					String Description=xl.getCellData(TCModule, j, 0);
					String Object_Type=xl.getCellData(TCModule, j, 1);
					String Ltype=xl.getCellData(TCModule, j, 2);
					String Lvalue=xl.getCellData(TCModule, j, 3);
					String Test_Data=xl.getCellData(TCModule, j, 4);
					try {
						if(Object_Type.equalsIgnoreCase("startBrowser"))
						{
							driver=FunctionLibrary.startBrowser();
							logger.log(LogStatus.INFO, Description);
						}
						if(Object_Type.equalsIgnoreCase("openUrl"))
						{
							FunctionLibrary.openUrl();
							logger.log(LogStatus.INFO, Description);
						}if(Object_Type.equalsIgnoreCase("waitForElement"))
						{
							FunctionLibrary.waitForElement(Ltype, Lvalue, Test_Data);
							logger.log(LogStatus.INFO, Description);
						}
						if(Object_Type.equalsIgnoreCase("typeAction"))
						{
							FunctionLibrary.typeAction(Ltype, Lvalue, Test_Data);
							logger.log(LogStatus.INFO, Description);
						}
						if(Object_Type.equalsIgnoreCase("clickAction"))
						{
							FunctionLibrary.clickAction(Ltype, Lvalue);
							logger.log(LogStatus.INFO, Description);
						}
						if(Object_Type.equalsIgnoreCase("validateTitle"))
						{
							FunctionLibrary.validateTitle(Test_Data);
							logger.log(LogStatus.INFO, Description);
						}
						if(Object_Type.equalsIgnoreCase("closeBrowser"))
						{
							FunctionLibrary.closeBrowser();
							logger.log(LogStatus.INFO, Description);
						}
						if(Object_Type.equalsIgnoreCase("dropDownAction"))
						{
							FunctionLibrary.dropDownAction(Ltype, Lvalue, Test_Data);
							logger.log(LogStatus.INFO, Description);
						}
						if(Object_Type.equalsIgnoreCase("captureStock"))
						{
							FunctionLibrary.captureStock(Ltype, Lvalue);
							logger.log(LogStatus.INFO, Description);
						}
						if(Object_Type.equalsIgnoreCase("stockTable"))
						{
							FunctionLibrary.stockTable();
							logger.log(LogStatus.INFO, Description);
						}
						if(Object_Type.equalsIgnoreCase("capturesupplier"))
						{
							FunctionLibrary.capturesupplier(Ltype, Lvalue);
							logger.log(LogStatus.INFO, Description);
						}
						if(Object_Type.equalsIgnoreCase("supplierTable"))
						{
							FunctionLibrary.supplierTable();
							logger.log(LogStatus.INFO, Description);
						}
						if(Object_Type.equalsIgnoreCase("captureCustomer"))
						{
							FunctionLibrary.captureCustomer(Ltype, Lvalue);
							logger.log(LogStatus.INFO, Description);
						}
						if(Object_Type.equalsIgnoreCase("customerTable"))
						{
							FunctionLibrary.customerTable();
							logger.log(LogStatus.INFO, Description);
						}
						//Write as pass into TCModule cell
						xl.setCellData(TCModule, j, 5, "Pass", outputpath);
						logger.log(LogStatus.PASS, Description);
						Module_Status="true";
						
					}catch (Exception e) {
						System.out.println(e.getMessage());
						//Write as fail into TCModule cell
						xl.setCellData(TCModule, j, 5, "Fail", outputpath);
						logger.log(LogStatus.FAIL, Description);
						Module_New="false";
	File screen=((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
   FileUtils.copyFile(screen, new File("./target/screenshot/"+Description+FunctionLibrary.generateDate()+".png"));
					
					}
					if(Module_Status.equalsIgnoreCase("True"))
					{
						xl.setCellData(TCSheet, i, 3, "Pass", outputpath);
					}
					reports.endTest(logger);
					reports.flush();
				}
				    if(Module_New.equalsIgnoreCase("False"))
				    {
				    	xl.setCellData(TCSheet, i, 3, "Fail", outputpath);
				    }
			}else
			{
				xl.setCellData(TCSheet, i, 3, "Blocked", outputpath);
			}
		}
	}
	

}
