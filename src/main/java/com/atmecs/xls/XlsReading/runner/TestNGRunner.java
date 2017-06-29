package com.atmecs.xls.XlsReading.runner;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

import com.atmecs.falcon.automation.appium.manager.AppiumParallelTest;
import com.atmecs.falcon.automation.appium.manager.Runner;
import com.atmecs.falcon.automation.run.mode.RunModeFactory;
import com.atmecs.falcon.automation.util.logging.LogLevel;
import com.atmecs.falcon.automation.util.logging.LogManager;
import com.atmecs.falcon.automation.util.reporter.ReportLogService;
import com.atmecs.falcon.automation.util.reporter.ReportLogServiceImpl;
import com.atmecs.falcon.automation.util.reporter.TestReportUploadClient;
import com.atmecs.falcon.automation.utils.enums.ERunModeType;
import com.atmecs.falcon.automation.utils.general.PropertyReader;


/**
 * @author 
 */
public class TestNGRunner 
{

	private static ReportLogService report = new ReportLogServiceImpl(TestNGRunner.class);
	private static Boolean isUserProvideTestng = false;

	/**
	 * String Constants
	 * **/
	
	static final String SUITE_FILES="SUITE_FILES";
	static final String MODULE_NAMES="MODULE_NAMES";

	/**
	 * @author atmecs
	 * @Description: reads the values from properties file i.e. user is provided the testng.xml or provided the packages
	 * If the packages are provided checks the packages are provided in the project  
	 * If the packages are available pass the package names separated by "," to testApp function for further process
	 * 
	 * **/
	public static void main(String[] args) throws Exception 
	{
		try 
		{
			
			AppiumParallelTest.runMode = RunModeFactory.getRunMode(getRunModeType());
			LogManager.setLogLevel(LogLevel.INFO);
			
			String packageName = "";
			String userProvidedTestNGFiles=PropertyReader.readEnvOrConfigProperty(SUITE_FILES);
			
			String path =
					System.getProperty("user.dir") + File.separator + "src" + File.separator
					+ "main" + File.separator + "java" + File.separator;

			File file = new File(path);
			TestScriptPackage obj = new TestScriptPackage();
			obj.getPackage(file);
			
			String x = obj.getPack().replaceFirst(",","");
			String[] totalPackages = x.split(",");
			
			ArrayList<String> list = new ArrayList<String>();
			Set<String> moduleSet = new HashSet<String>();
			Map<String, String> moduleMap = new HashMap<String, String>();
			
			for (String pack : totalPackages) 
			{
				moduleSet.add(getModuleNameForPackage(pack));
				moduleMap.put(getModuleNameForPackage(pack), pack);
			}
			
			if (!(userProvidedTestNGFiles.trim().isEmpty() || userProvidedTestNGFiles==null)) 
			{
				isUserProvideTestng=true;
			}
			String userProvidedModuleList= PropertyReader.readEnvOrConfigProperty(MODULE_NAMES); 

			if (userProvidedModuleList.trim().isEmpty() || userProvidedModuleList==null || isUserProvideTestng) 
			{
				if (isUserProvideTestng)
					System.out.println("User provided its own testNG");
				else
				{
					System.out.println("UserProvided Module list is Null : Adding all packages Containig Tests");
					for (String module : moduleSet) 
						packageName +=  "," + moduleMap.get(module);
				}
			}
			else
			{
				list.addAll(moduleSet);
				String[] userProvidedModules = userProvidedModuleList.split(",");
				for (String userProvidedModule : userProvidedModules) 
				{
					if (list.contains(userProvidedModule)) 
					{
						packageName += "," + moduleMap.get(userProvidedModule);
					}
					else
						throw new Exception("The provided package is invalid");
				}
			}
			
			System.out.println("Printing Package name "+packageName);
			Runner.testApp(packageName.replaceFirst(",", ""));
			//ssif(PropertyReader.readEnvOrConfigProperty("upload.result").equalsIgnoreCase("true"))
				//uploadTestNGResultsXml();

		} 
		catch (final Exception e) 
		{
			e.printStackTrace();
		}
	}


	/**
	 *@author 
	 *@exception Error in Network connection 
	 *@Description: upload the generated testng-results.xml to the report server
	 * **/
	public static void uploadTestNGResultsXml() 
	{
		try
		{
			String uploadUrl = PropertyReader.readEnvOrConfigProperty("testreport.uploadurl");
			String testNGResultsXmlFilePath =
					System.getProperty("user.dir") + File.separator + "test-output"
							+ File.separator + "testng-results.xml";
			
			TestReportUploadClient testReportUploadClient = new TestReportUploadClient(uploadUrl);
			report.info("Started Uploading Results to Report Server...");

			String clientName = PropertyReader.readEnvOrConfigProperty("CLIENT_NAME");
			String response =
					testReportUploadClient.upload("1000", clientName, "Mobile", "QA", "Regression",
							"Local", "", "Android", "", testNGResultsXmlFilePath);
			report.info("Response : " + response);

		} 
		catch (Exception e) 
		{
			report.info("Unknown error : Cannot Upload the testng-results.xml " + e.getMessage());
		}
	}

	
	/**
	 * @return returns the module name
	 * e.g. @param = com/atmecs/test/FalconFirst/module
	 *  return = module
	 * */
	public static String getModuleNameForPackage(String p)
	{
		
		String pattern = Pattern.quote(System.getProperty("file.separator"));
		String[] mArr=p.split(pattern);
		String moduleName=mArr[mArr.length - 1];
		return moduleName;
	}



	private static ERunModeType getRunModeType() 
	{
		String runMode = PropertyReader.readEnvOrConfigProperty("RUN_MODE");
		if (runMode.equals("LOCAL")) {
			return ERunModeType.LOCAL_RUN;
		}
		else if (runMode.equals("REMOTE")) 
		{
			return ERunModeType.REMOTE_RUN;
		} 
		else if (runMode.equals("CLOUD")) 
		{
			return ERunModeType.CLOUD_RUN;
		}
		else 
		{
			return ERunModeType.LOCAL_RUN;
		}
	}
}
