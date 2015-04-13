package tests;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverBackedSelenium;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.testng.Assert;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.Reporter;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;

import com.thoughtworks.selenium.Selenium;
 
/** 
 * Base configuration class for functional testing by Selenium and TestNG. 
 * 
 * Soft assertions are based on http://seleniumexamples.com/blog/guide/using-soft-assertions-in-testng/.
 * 
 * For any problems you can visit these google groups:
 * http://groups.google.com/group/selenium-users,
 * http://groups.google.com/group/testng-users,
 * http://groups.google.com/group/testng-xslt-users.
 * 
 * @author Bretislav Mazoch, bretick@mail.muni.cz
 * @version 2011-12-14
 */
public class TestBase {

	public static final String DATE_FORMAT_NOW = "yyyy-MM-dd HH:mm:ss";
	public static final String DATE_FORMAT_NOW2 = "yyyy-MM-dd-HH-mm-ss";
	public static final String EMPTY_STRING = "";

	public static Selenium selenium;
	public static WebDriver driver;	

	public static String suiteName;
	public static String baseUrl;	
	public static Browser browserDriver;
	
	private static Map<ITestResult, List<Throwable>> verificationFailuresMap = new HashMap<ITestResult, List<Throwable>>();
	private static int logCounter = 0;
	private static int screenshotCounter = 0;

	
	public enum Browser {
		// for Internet Explorer driver set up see http://www.sevenforums.com/tutorials/63141-internet-explorer-protected-mode-turn-off.html and http://code.google.com/p/selenium/issues/detail?id=2018
		MOZILLA_FIREFOX, INTERNET_EXPLORER;  
	}
	

	

	/************************* BEFORE/AFTER SUITE METHODS *************************/	
	
	 /**
	    * Initilizates Selenium.
	    * 
	    * @param contextArg XML context to get parameters.
	    */
	@BeforeSuite
	public void startSelenium(ITestContext contextArg) throws Exception {

		// takes information from suite XMLs
		suiteName = contextArg.getCurrentXmlTest().getSuite().getName();
		browserDriver = Browser.valueOf(contextArg.getCurrentXmlTest().getParameter("browserDriver")); 
		baseUrl = contextArg.getCurrentXmlTest().getParameter("baseUrl");
  
		switch (browserDriver) {
		case MOZILLA_FIREFOX: { driver = new FirefoxDriver(); break; }
		case INTERNET_EXPLORER: { driver = new InternetExplorerDriver(); break; }
		default: { driver = new FirefoxDriver(); break;}
		}
		selenium = new WebDriverBackedSelenium(driver, baseUrl);
			
		// gets system properties
		Properties systemProps = System.getProperties();
		String system = systemProps.getProperty("user.name") + " / " + "Java " + systemProps.getProperty("java.version") + " (" + systemProps.getProperty("java.vm.vendor") + ") / " + systemProps.getProperty("os.name") + " " + systemProps.getProperty("os.version") + " (" + systemProps.getProperty("os.arch") + ")";
		
		// reporter output information about suite... to allow HTML need to add parameter disable-output-escaping="yes" and commented <div></div> per-line-constraints in report output in testng-results.xsl
		Reporter.log("<div id='" + suiteName + "-container' style='margin-bottom: 10px; padding-bottom: 5px; border-bottom: 1px dashed #cecece;'><strong>" + suiteName + "</strong> (<a href='#" + suiteName + "-container' onclick='showSolution(\"" + suiteName + "\", this);'>Show/Hide extended information</a>)</div><div id='" + suiteName + "' style='display: none;'>"
					+ "<div style='text-align: center; padding: 10px; border-top: 1px solid #000; border-bottom: 1px solid #000;'><strong>############### Suite: " + suiteName + " ###############</strong></div>" 
					+ "<h3>Suite information:</h3><div style='width: 98%; background: #f5f5f5; padding: 10px;'><strong>info about system</strong> = " + system + "<br /><strong>suite.name</strong> = " + suiteName + "<br /><strong>selenium.url</strong> = " + baseUrl + "<br /><strong>selenium.browserDriver</strong> = " + browserDriver + "</div><h3>Reporter log:</h3>"); 
	}
	
	
	 /**
	    * Stops Selenium.
	    */	
	@AfterSuite
	public void stopSelenium() throws Exception {		
		// reporter output information about suite 
		Reporter.log("<div style='margin-bottom: 100px; text-align: center; padding: 10px; border-top: 1px solid #000; border-bottom: 1px solid #000;'><strong>############### Suite: " + suiteName + " ###############</strong></div></div>"); 		
		selenium.stop();	
	}
	
	
	
	/************************* SOFT ASSERTION *************************/	
	
	/**
	    * Returns current date and time in specific format.
	    * @param dateFormatArg Specific date and time format.
	    * @return string formatted date and time.
	    */		
	public static String now(String dateFormatArg) {
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat(dateFormatArg);
		return sdf.format(cal.getTime());
	}
	
	
	 /**
	    * Returns information about current failure (page title, page location, additional information).
	    * 
	    * @param additionalInfoArg Custom additional information about failure.
	    * @return string of information about failure.
	    */		
	public static String failureMessage (String additionalInfoArg) {		
		String result;
		if (additionalInfoArg.isEmpty()) {
			result = "\n\nFailure on page (title, link):\n" + driver.getTitle() + "\n" + driver.getCurrentUrl() + "\n\n";
		} else {
			result = "\n\nFailure on page (title, link):\n" + driver.getTitle() + "\n" + driver.getCurrentUrl() + "\n\nAdditional information:\n" + additionalInfoArg + "\n\n";
		}		
		return result;			
	} 	
	
	
	 /**
	    * Reports to output specific message in form of list item (&lt;ol&gt;&lt;/ol&gt; defined in methods onTestStart, onTestSuccess and onTestFailure).
	    * 
	    * @param messageArg Message to be reported by reporter output.
	    */		
	public static void reporterFormatedLog (String messageArg) {	
		if (!messageArg.isEmpty()) {
			String rowColor;
			if (logCounter % 2 == 0) {
				rowColor = "#fcfcfc";
			} else {
				rowColor = "#f8f8f8";				
			}			
			Reporter.log("<li style='background-color: " + rowColor + "; padding: 4px; border-top: 1px solid #dbdbdb; font-size: 90%;'>" + messageArg + "</li>");
			logCounter++;
		}
	}
		
	
	 /**
	    * Reports to output information about current failure (page title, page location, additional information) and captures screenshot.
	    */		
	public static void reporterFailureMessage () {		
		String info, resultReporter;		
		resultReporter = "<div style='background-color: #ffcccc; padding: 5px;'><table style='font-size: 90%;' border='0'><tr><td width='160'>";
		
		// taking screenshot
		String filePath = "test-reports/screenshots/" + now(DATE_FORMAT_NOW2) + "-screenshot-" + screenshotCounter + ".png";		
		try {
			File scrFile = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
			try {
				FileUtils.copyFile(scrFile, new File(filePath));
				resultReporter = resultReporter + "<a target='_blank' href='../../" + filePath + "'><img src='../../" + filePath + "' width='150' height='200' /></a>";		
				info = "<br /><br />Screenshot saved at:<br /><a target='_blank' href='../../" + filePath + "'>../../" + filePath + "</a> <em>(local link, preview image)</em><br /><a target='_blank' href='../artifact/testng/" + filePath + "'>../artifact/testng/" + filePath + "</a> <em>(Jenkins link)</em>";
				screenshotCounter++;
			} catch (IOException e) {
				info = "<br /><br />Failure occured when capturing screenshot.";
				Reporter.log("IOException when manipulating with screenshot file");
				e.printStackTrace();
			}				
		} catch (WebDriverException e) {
			info = "<br /><br />Failure has occured when capturing screenshot.";
			e.printStackTrace();
		}			
		        		
		resultReporter = resultReporter + "</td><td>Failure on page (title, link):<br />" + driver.getTitle() + "<br /><a target='_blank' href='" + driver.getCurrentUrl() + "'>" + driver.getCurrentUrl() + "</a>" + info + "</td></tr></table></div>";
		Reporter.log(resultReporter);		
	} 	
	
	
	
	
    public static void fail() {
    	reporterFormatedLog(EMPTY_STRING);
    	reporterFailureMessage(); 
    	Assert.fail(EMPTY_STRING);
    }	
	
    public static void fail(String message) {
    	reporterFormatedLog(message);
    	reporterFailureMessage(); 
    	Assert.fail(failureMessage(message));
    }
    
	public static List<Throwable> getVerificationFailures() {
		List<Throwable> verificationFailures = verificationFailuresMap.get(Reporter.getCurrentTestResult());
		return verificationFailures == null ? new ArrayList<Throwable>() : verificationFailures;
	}
	
	private static void addVerificationFailure(Throwable e) {
		List<Throwable> verificationFailures = getVerificationFailures();
		verificationFailuresMap.put(Reporter.getCurrentTestResult(), verificationFailures);
		verificationFailures.add(e);
	}	
	
	
	/****************** ASSERTS ******************/
    public static void assertTrue(boolean condition) {    	
    	assertTrue(condition, EMPTY_STRING);  	
    }
    
    public static void assertTrue(boolean condition, String message) {     
    	try {
    		reporterFormatedLog(message);
    		Assert.assertTrue(condition);
    	} catch(Throwable e) {
    		reporterFailureMessage();  
    		Assert.assertTrue(condition, failureMessage(message));
    	}      	
    }
    
    public static void assertFalse(boolean condition) {   	
    	assertFalse(condition, EMPTY_STRING);  	
    }
    
    public static void assertFalse(boolean condition, String message) {    	
    	try {
    		reporterFormatedLog(message);
    		Assert.assertFalse(condition);
    	} catch(Throwable e) {
    		reporterFailureMessage();    
    		Assert.assertFalse(condition, failureMessage(message));
    	}      	
    }
    
    public static void assertEquals(boolean actual, boolean expected) {	
    	assertEquals(actual, expected, EMPTY_STRING);  	
    }
    
    public static void assertEquals(Object actual, Object expected) {
    	assertEquals(actual, expected, EMPTY_STRING);  	
    }
    
    public static void assertEquals(Object[] actual, Object[] expected) {
    	assertEquals(actual, expected, EMPTY_STRING);  	
    }
    
    public static void assertEquals(Object actual, Object expected, String message) {
    	try {
        	reporterFormatedLog(message);
    		Assert.assertEquals(actual, expected);
    	} catch(Throwable e) {
    		reporterFailureMessage(); 
    		Assert.assertEquals(actual, expected, failureMessage(message));
    	}   
    }
    
    
    /****************** VERIFIES ******************/
    public static void verifyTrue(boolean condition) {
    	try {
    		assertTrue(condition);
    	} catch(Throwable e) {
    		addVerificationFailure(e);
    	}
    }
    
    public static void verifyTrue(boolean condition, String message) {
    	try {
    		assertTrue(condition, message);
    	} catch(Throwable e) {
    		addVerificationFailure(e);
    	}
    }
    
    public static void verifyFalse(boolean condition) {
    	try {
    		assertFalse(condition);
		} catch(Throwable e) {
    		addVerificationFailure(e);
		}
    }
    
    public static void verifyFalse(boolean condition, String message) {
    	try {
    		assertFalse(condition, message);
    	} catch(Throwable e) {
    		addVerificationFailure(e);
    	}
    }
    
    public static void verifyEquals(boolean actual, boolean expected) {
    	try {
    		assertEquals(actual, expected);
		} catch(Throwable e) {
    		addVerificationFailure(e);
		}
    }
    
    public static void verifyEquals(boolean actual, boolean expected, String message) {
    	try {
    		assertEquals(actual, expected, message);
		} catch(Throwable e) {
    		addVerificationFailure(e);
		}
    }

    public static void verifyEquals(Object actual, Object expected) {
    	try {
    		assertEquals(actual, expected);
		} catch(Throwable e) {
    		addVerificationFailure(e);
		}
    }

    public static void verifyEquals(Object actual, Object expected, String message) {
    	try {
    		assertEquals(actual, expected, message);
		} catch(Throwable e) {
    		addVerificationFailure(e);
		}
    }
    
    public static void verifyEquals(Object[] actual, Object[] expected) {
    	try {
    		assertEquals(actual, expected);
		} catch(Throwable e) {
    		addVerificationFailure(e);
		}
    }
    
    public static void verifyEquals(Object[] actual, Object[] expected, String message) {
    	try {
    		assertEquals(actual, expected, message);
		} catch(Throwable e) {
    		addVerificationFailure(e);
		}
    }

	
}
