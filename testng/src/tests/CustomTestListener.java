package tests;

import java.util.List;

import org.testng.IInvokedMethod;
import org.testng.IInvokedMethodListener;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;
import org.testng.Reporter;
import org.testng.internal.Utils;

/**  
 * Listener class of testing. 
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
public class CustomTestListener implements IInvokedMethodListener, ITestListener {
	
	 /**
	    * Method from interface IInvokedMethodListener that is invoked after test method invocation.
	    * 
	    * @param method Invoked method.
	    * @param result Test results.
	    */	
	@Override
	public void afterInvocation(IInvokedMethod methodArg, ITestResult resultArg) {
		
		Reporter.setCurrentTestResult(resultArg);
		
		if (methodArg.isTestMethod()) {

			List<Throwable> verificationFailures = TestBase.getVerificationFailures();

			//if there are verification failures...
			if (verificationFailures.size() > 0) {
				
				//set the test to failed
				resultArg.setStatus(ITestResult.FAILURE);

				//if there is an assertion failure add it to verificationFailures
				if (resultArg.getThrowable() != null) {
					verificationFailures.add(resultArg.getThrowable());
				}				
					
				int size = verificationFailures.size();
				//if there's only one failure just set that
				if (size == 1) {
					resultArg.setThrowable(verificationFailures.get(0));
				} else {
					//create a failure message with all failures and stack traces (except last failure)
					StringBuffer failureMessage = new StringBuffer("Multiple failures (").append(size).append("):\n\n");
									
					for (int i = 0; i < size-1; i++) {
						failureMessage.append("Failure ").append(i+1).append(" of ").append(size).append(":\n");
						
						
						Throwable t = verificationFailures.get(i);
						String fullStackTrace = Utils.stackTrace(t, false)[1];
						failureMessage.append(fullStackTrace).append("\n\n\n");
					}
					
					//final failure
					Throwable last = verificationFailures.get(size-1);
					failureMessage.append("Failure ").append(size).append(" of ").append(size).append(":\n");
					failureMessage.append(last.toString());
					
					//set merged throwable
					Throwable merged = new Throwable(failureMessage.toString());
					merged.setStackTrace(last.getStackTrace());
					
					resultArg.setThrowable(merged);
				}
			}
		}
	}
		
	 /**
	    * Method from interface ITestListener that is invoked on test method start and prints test information to reporter output.
	    * 
	    * @param result Test results.
	    */	
    @Override
    public void onTestStart(ITestResult resultArg) {    
    	// unique ID for html tag
    	String tagId = TestBase.suiteName + TestBase.now(TestBase.DATE_FORMAT_NOW2) + resultArg.getMethod().getMethodName() + resultArg.getMethod().getId() + resultArg.getMethod().getCurrentInvocationCount();
    	Reporter.log("<div id='" + tagId + "-container' style='width: 98%; background: #e0e0e0; padding: 10px; margin-bottom: 10px;'><strong>" + resultArg.getMethod().getMethodName() + "</strong> at [" + TestBase.now(TestBase.DATE_FORMAT_NOW) + "] with invocation order " + resultArg.getMethod().getCurrentInvocationCount() + "<br /><br />" + resultArg.getMethod() + "<br /><em>" + resultArg.getMethod().getDescription() + "</em><br /><br />"
        			+ "<a href='#" + tagId + "-container' onclick='showSolution(\"" + tagId + "\", this);'>Show/Hide test method steps</a>"
        			+ "<ol id='" + tagId + "' style='display: none; margin: 0px; padding-left: 30px; margin-top: 10px;'>");        
    }    
 
	 /**
	    * Method from interface ITestListener that is invoked on test method success and prints test method information to reporter output.
	    * 
	    * @param result Test results.
	    */	
    @Override
    public void onTestSuccess(ITestResult resultArg) {        
        Reporter.log("</ol><div style='color: white; background: green; padding: 4px; margin-top: 15px;'><strong>SUCCES</strong></div></div>");
    } 

	 /**
	    * Method from interface ITestListener that is invoked on test method failure and prints test method information to reporter output.
	    * 
	    * @param result Test results.
	    */	
	@Override
	public void onTestFailure(ITestResult resultArg) {  
        Reporter.log("</ol><div style='color: white; background: red; padding: 4px; margin-top: 15px;'><strong>FAILURE</strong></div></div>");	
	}

	 /**
	    * Method from interface ITestListener that is invoked on test method skip and prints test method information to reporter output.
	    * 
	    * @param result Test results.
	    */	
	@Override
	public void onTestSkipped(ITestResult resultArg) {
	    Reporter.log("</ol><div style='background: yellow; padding: 4px; margin-top: 15px;'><strong>SKIPPED</strong></div></div>");	
	}

	 /**
	    * Method from interface ITestListener that is invoked on test start and prints test information to reporter output.
	    * 
	    * @param result Test results.
	    */	
	@Override
	public void onStart(ITestContext resultArg) {
		Reporter.log("<div style='margin-bottom: 10px; text-align: center; padding: 10px; border-top: 1px dashed #777; border-bottom: 1px dashed #777; color: #777;'><strong>Test: " + resultArg.getCurrentXmlTest().getName() + "</strong></div>"); 		
	}

	 /**
	    * Method from interface ITestListener that is invoked on test finish and prints test information to reporter output.
	    * 
	    * @param result Test results.
	    */	
	@Override
	public void onFinish(ITestContext resultArg) {
		Reporter.log("<div style='margin-top: 10px; margin-bottom: 30px; text-align: center; padding: 10px; border-top: 1px dashed #777; border-bottom: 1px dashed #777; color: #777;'><strong>Test: " + resultArg.getCurrentXmlTest().getName() + "</strong></div>"); 				
	}
	
	 /**
	    * Method from interface ITestListener that is invoked on test failure but within success percentage.
	    * 
	    * @param result Test results.
	    */	
	@Override
	public void onTestFailedButWithinSuccessPercentage(ITestResult resultArg) {}

	 /**
	    * Method from interface IInvokedMethodListener that is invoked before test method invocation.
	    * 
	    * @param method Invoked method.
	    * @param result Test results.
	    */	
	@Override
	public void beforeInvocation(IInvokedMethod methodArg, ITestResult resultArg) {}

}
