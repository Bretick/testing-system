package tests;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Iterator;
import java.util.ArrayList;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

/**  
 * Class for functional testing Agenda Lide of is.muni.cz by Selenium and TestNG. 
 * 
 * For any problems you can visit these google groups:
 * http://groups.google.com/group/selenium-users,
 * http://groups.google.com/group/testng-users,
 * http://groups.google.com/group/testng-xslt-users.
 * 
 * @author Bretislav Mazoch, bretick@mail.muni.cz
 * @version 2011-12-14
 */
public class AgendaLide extends TestBase {	
	
	public static final String PERSON_SEPARATOR = "\\|";
	public static final String PUBLICATION_SEPARATOR = "\\|";
	public static final String NAME_WORK_SEPARATOR = "\\:\\:";	
	
	/*************** DATAPROVIDERS ***************/	
	
	/** 
	 * Class for data from supervisor tab.
	 */
	public class SupervisorTabData {
		
		private String uco;
		private String doctoralStudents;
		private String doctoralConsultant;
		private String thesesSupervisor;
		private String thesesReader;
			
		SupervisorTabData(String ucoArg, String doctoralStudentsArg, String doctoralConsultantArg, String thesesSupervisorArg, String thesesReaderArg) {	
			this.uco = ucoArg;
			this.doctoralStudents = doctoralStudentsArg;
			this.doctoralConsultant = doctoralConsultantArg;
			this.thesesSupervisor = thesesSupervisorArg;
			this.thesesReader = thesesReaderArg;
		} 		
		
		public String getUco() {
			return this.uco;
		}
		
		public String getDoctoralStudents() {
			return this.doctoralStudents;
		}
				
		public String getDoctoralConsultant() {
			return this.doctoralConsultant;
		}
				
		public String getThesesSupervisor() {
			return this.thesesSupervisor;
		}
				
		public String getThesesReader() {
			return this.thesesReader;
		}				
	}

	
	
	/** 
	 * Class for data from publications tab.
	 */	
	public class PublicationsTabData {
		
		private String uco;
		private String publications;
			
		PublicationsTabData(String ucoArg, String publicationsArg) {	
			this.uco = ucoArg;
			this.publications = publicationsArg;
		} 		
		
		public String getUco() {
			return this.uco;
		}
		
		public String getPublications() {
			return this.publications;
		}			
	}	
	

	
	 /**
	    * Data provider for parameterization of tests. 
	    * 
	    * @return Iterator of objects with given data.
	    */
	 @DataProvider (name="dataOfSupervisorTab") 
	 public Iterator<Object[]> createDataOfSupervisorTab() throws IOException {                 
		 ArrayList<Object[]> myEntries = new ArrayList<Object[]>();  
		 // for processing CSV file see http://selftechy.com/2011/06/19/selenium-parameterization-using-csv-file
		 File inputWorkbook = new File("./data/supervisorTabData.xls");   
	     Workbook w;     
	     String Temp1, Temp2, Temp3, Temp4, Temp5;       
	        try {         
	            	  w = Workbook.getWorkbook(inputWorkbook);     
	            	  Sheet sheet = w.getSheet(0);      
	            	  // data start at second row
	            	  for (int i = 1; i < sheet.getRows(); i++) {      
	            		  // column, row (profile, doctoral students, doctoral consultant, theses supervisor, theses reader)     
	            		  Cell cell = sheet.getCell(0, i);         
	            		  Temp1 = cell.getContents();                 
	            		  cell = sheet.getCell(1, i);      
	            		  Temp2 = cell.getContents();                 
	            		  cell = sheet.getCell(2, i);      
	            		  Temp3 = cell.getContents();                   
	            		  cell = sheet.getCell(3, i);      
	            		  Temp4 = cell.getContents();	                 
	            		  cell = sheet.getCell(4, i);      
	            		  Temp5 = cell.getContents();	
	            		  SupervisorTabData data = new SupervisorTabData(Temp1, Temp2, Temp3, Temp4, Temp5);            		  
	            		  myEntries.add(new Object [] {data, "File: " + inputWorkbook.getPath(), "Row: " + i});       
	            	  }      
	            } 
	        catch (BiffException e) {e.printStackTrace();}      
	        
	      return myEntries.iterator();          
	 }    
	
	
	 
		
	 /**
	    * Data provider for parameterization of tests. 
	    * 
	    * @return Iterator of objects with given data.
	    */
	 @DataProvider (name="dataOfPublicationsTab") 
	 public Iterator<Object[]> createDataOfPublicationsTab() throws IOException {                 
		 ArrayList<Object[]> myEntries = new ArrayList<Object[]>();  
		 // for processing CSV file see http://selftechy.com/2011/06/19/selenium-parameterization-using-csv-file
		 File inputWorkbook = new File("./data/publicationsTabData.xls");   
	     Workbook w;     
	     String Temp1, Temp2;       
	        try {         
	            	  w = Workbook.getWorkbook(inputWorkbook);     
	            	  Sheet sheet = w.getSheet(0);      
	            	  // data start at second row
	            	  for (int i = 1; i < sheet.getRows(); i++) {      
	            		  // column, row (profile, publications)     
	            		  Cell cell = sheet.getCell(0, i);         
	            		  Temp1 = cell.getContents();                 
	            		  cell = sheet.getCell(1, i);      
	            		  Temp2 = cell.getContents();           		  
	            		  PublicationsTabData data = new PublicationsTabData(Temp1, Temp2);   
	            		  myEntries.add(new Object [] {data, "File: " + inputWorkbook.getPath(), "Row: " + i});       
	            	  }      
	        } catch (BiffException e) {e.printStackTrace();}      
	        
	      return myEntries.iterator();          
	 }  
	
	
	
	
	/*************** HELP METHODS ***************/

	 /**
	    * Opens specific personal page.
	    * 
	    * @param ucoArg Person's identification number.
	    */		
	public void openPersonalPage(String ucoArg) throws Exception {
		reporterFormatedLog("Open personal page \"" + ucoArg + "\"");
		selenium.open("/osoba/" + ucoArg);
		verifyTrue(selenium.getTitle().contains("Osobní stránka"), "Verify page title \"Osobní stránka\"");
		assertTrue(selenium.getText("css=div#status").contains("učo " + ucoArg), "Verify personal page by \"učo " + ucoArg + "\"");
	}
	

	/**
	    * Clicks on specific tab of profile page.
	    * 
	    * @param tabArg Value of particular tab (anchor value).
	    */		
	public void clickOnSpecificTabOfProfilePage(String tabArg) throws Exception {
		reporterFormatedLog("Click on specific tab to show \"" + tabArg + "\"");
		selenium.click("css=a[href='" + tabArg + "']");
	}	
		
	
	 /**
	    * Waits for some element with data.
	    * 
	    * @param elementArg Element for which is waiting.
	    */		
	public void waitForElement(String elementArg) throws Exception {
		reporterFormatedLog("Wait for element \"" + elementArg + "\"");
		for (int second = 0;; second++) {
			if (second >= 60) fail("Timeout");
			try { if (selenium.isVisible(elementArg)) break; } catch (Exception e) {}
			Thread.sleep(1000);
		}
	}		

	
	
	 /**
	    * Opens specific personal page and then clicks on tab link to show Supervisor (školitel) information.
	    * 
	    * @param ucoArg Person's specific identication number (učo).
	    */		
	public void openPersonalPageAndClickSupervisorTab(String ucoArg) throws Exception{
		this.openPersonalPage(ucoArg);
		this.clickOnSpecificTabOfProfilePage("#skolitel");
		this.waitForElement("css=div#_skolitel h4:nth(0)");
	}	
	
	
	 /**
	    * Opens specific personal page and then clicks on tab link to show Publications (Publikace) information.
	    * 
	    * @param ucoArg Person's specific identication number (učo).
	    */		
	public void openPersonalPageAndClickPublicationsTab(String ucoArg) throws Exception {
		this.openPersonalPage(ucoArg);
		this.clickOnSpecificTabOfProfilePage("#publikace");
		this.waitForElement("css=div#_publikace ul.vyhl_publ");	
	}	
	
	
	
	/**
	 * Clicks and Waits for page to load.
	 *
	 * @param elementLocatorArg
	 * @param waitPeriodArg
	 */
	public void clickAndWait(String elementLocatorArg, String waitPeriodArg) throws Exception {
		reporterFormatedLog("Click on \"" + elementLocatorArg + "\"");
		selenium.click(elementLocatorArg);
		reporterFormatedLog("Wait for page to load");
		selenium.waitForPageToLoad(waitPeriodArg);
	}
	
	
	/**
	 * Goes back and Waits for page to load.
	 *
	 * @param elementLocatorArg
	 * @param waitPeriodArg
	 */
	public void goBackAndWait(String waitPeriodArg) throws Exception {
		reporterFormatedLog("Go back");
		selenium.goBack();
		reporterFormatedLog("Wait for page to load");
		selenium.waitForPageToLoad(waitPeriodArg);
	}
	
	
	
	/*************** TEST METHODS ***************/
	
	 /**
	    * Checks name and topic for block of information: Doctoral students.
	    * 
	    * @param dataArg Object to find out specific person's information.
	    * @param fileInformationArg String of the processed file path.
	    * @param rowInformationArg String of the processed row.
	    */		
	@Test(dataProvider = "dataOfSupervisorTab", description="Checks name and topic for block of information: Doctoral students.")
	public void checkRoleDoctoralStudents(SupervisorTabData dataArg, String fileInformationArg, String rowInformationArg) throws Exception {
					
		String[] studentArray = dataArg.getDoctoralStudents().split(PERSON_SEPARATOR, -1);
		
		// checks count of items
		if(studentArray[0] != "") {					
			this.openPersonalPageAndClickSupervisorTab(dataArg.getUco());
			
			Iterator<String> student = Arrays.asList(studentArray).iterator();
			String cssLocator, message;	
			int listIndex = 0;
			
			for (Iterator<String> item = student; item.hasNext();) {
				// splits current item to person name and work name by given separator
			    String[] nameAndTopic = item.next().split(NAME_WORK_SEPARATOR, -1);
			   
			    cssLocator = "css=div#_skolitel h4:contains('Doktorští studenti') + ul>li>a:nth(" + listIndex + ")";
			    message = "Verify that \"" + cssLocator +  "\" contains \"" + nameAndTopic[0] + "\"";
			    verifyTrue(selenium.getText(cssLocator).contains(nameAndTopic[0]), message);
			    
			    // sometimes is set also topic
			    if(nameAndTopic.length == 2) {
			    	cssLocator = "css=div#_skolitel h4:contains('Doktorští studenti') + ul>li:nth(" + listIndex + ")>ul>li>i";
			    	message = "Verify that \"" + cssLocator +  "\" matches \"" + nameAndTopic[1] + "\"";
				    verifyEquals(selenium.getText(cssLocator), nameAndTopic[1], message);
			    }	
			    
			    listIndex++;
			} // ends for
		} else {
			reporterFormatedLog("No data from dataprovider");
		}		
	}	
	
	
	
	 /**
	    * Checks name and topic for block of information: Doctoral students' consultant.
	    * 
	    * @param dataArg Object to find out specific person's information.
	    * @param fileInformationArg String of the processed file path.
	    * @param rowInformationArg String of the processed row.
	    */		
	@Test(dataProvider = "dataOfSupervisorTab", description="Checks name and topic for block of information: Doctoral students' consultant.")
	public void checkRoleDoctoralConsultant(SupervisorTabData dataArg, String fileInformationArg, String rowInformationArg) throws Exception {

		String[] consultantArray = dataArg.getDoctoralConsultant().split(PERSON_SEPARATOR, -1);
		
		// checks count of items
		if(consultantArray[0] != "") {					
			this.openPersonalPageAndClickSupervisorTab(dataArg.getUco());
			
			Iterator<String> consultant = Arrays.asList(consultantArray).iterator();
			String cssLocator, message;	
			int listIndex = 0;
			
			for (Iterator<String> item = consultant; item.hasNext();) {
				// splits current item to person name and work name by given separator
			    String[] nameAndTopic = item.next().split(NAME_WORK_SEPARATOR, -1);
			   
			    cssLocator = "css=div#_skolitel h4:contains('Konzultant doktorských studentů') + ul>li>a:nth(" + listIndex + ")";
			    message = "Verify that \"" + cssLocator +  "\" contains \"" + nameAndTopic[0] + "\"";
			    verifyTrue(selenium.getText(cssLocator).contains(nameAndTopic[0]), message);
			    
			    // sometimes is set also topic
			    if(nameAndTopic.length == 2) {
			    	cssLocator = "css=div#_skolitel h4:contains('Konzultant doktorských studentů') + ul>li:nth(" + listIndex + ")>ul>li>i";
			    	message = "Verify that \"" + cssLocator +  "\" matches \"" + nameAndTopic[1] + "\"";
				    verifyEquals(selenium.getText(cssLocator), nameAndTopic[1], message);
			    }	
			    
			    listIndex++;
			} // ends for
		} else {
			reporterFormatedLog("No data from dataprovider");
		}		
	}
	
	
	 /**
	    * Checks name and topic for block of information: Theses - Supervisor.
	    * 
	    * @param dataArg Object to find out specific person's information.
	    * @param fileInformationArg String of the processed file path.
	    * @param rowInformationArg String of the processed row.
	    */		
	@Test(dataProvider = "dataOfSupervisorTab", description="Checks name and topic for block of information: Theses - Supervisor.")
	public void checkRoleThesesSupervisor(SupervisorTabData dataArg, String fileInformationArg, String rowInformationArg) throws Exception {

		String[] supervisorArray = dataArg.getThesesSupervisor().split(PERSON_SEPARATOR, -1);
		
		// checks count of items
		if(supervisorArray[0] != "") {		
			this.openPersonalPageAndClickSupervisorTab(dataArg.getUco());

			Iterator<String> supervisor = Arrays.asList(supervisorArray).iterator();	
			String cssLocator, message;	
			int count = 1;
			int listIndex = 0;
			
			for (Iterator<String> item = supervisor; item.hasNext();) {
				// splits current item to person name and work name by given separator
				String[] nameAndTopic = item.next().split(NAME_WORK_SEPARATOR, -1);				

				// distinguishes which part of the list is current
				String part = EMPTY_STRING;
				if (count > 10) {
					part = "dl + p + ";	
					// resets listIndex counter
					if(count == 11) { listIndex = 0; }
					// clicks switch for more items 	
				    reporterFormatedLog("Click on switch for more results");
					selenium.click("css=div#_skolitel h5:contains('Vedoucí') + dl + p a[class*='rozbal']");	
				}				
				
				cssLocator = "css=div#_skolitel h5:contains('Vedoucí') + " + part + "dl dd:nth(" + Integer.toString(listIndex) + ") a";
				message = "Verify that \"" + cssLocator +  "\" matches \"" + nameAndTopic[0] + "\"";
				verifyEquals(selenium.getText(cssLocator), nameAndTopic[0], message);
				cssLocator = "css=div#_skolitel h5:contains('Vedoucí') + " + part + "dl dt:nth(" + Integer.toString(listIndex) + ") a";
				message = "Verify that \"" + cssLocator +  "\" matches \"" + nameAndTopic[1] + "\"";
				verifyEquals(selenium.getText(cssLocator), nameAndTopic[1], message);
								
				// checking correct redirect to page with specific work name and person name
				this.clickAndWait(cssLocator, "30000");
				cssLocator = "css=h1#nadpis";				
				message ="Verify that \"" + cssLocator +  "\" contains \"Archiv závěrečné práce " + nameAndTopic[0] + "\"";
				verifyTrue(selenium.getText(cssLocator).contains("Archiv závěrečné práce " + nameAndTopic[0]), message);
				cssLocator = "css=div#metadata>h3";				
				message ="Verify that \"" + cssLocator +  "\" contains \"" + nameAndTopic[0] + "\"";
				verifyTrue(selenium.getText(cssLocator).contains(nameAndTopic[0]), message);
				cssLocator = "css=div#metadata>h2";
				message ="Verify that \"" + cssLocator +  "\" matches \"" + nameAndTopic[1] + "\"";
				verifyEquals(selenium.getText(cssLocator), nameAndTopic[1], message);
				this.goBackAndWait("30000");

				count++;
				listIndex++;
			} // ends for
		} else {
			reporterFormatedLog("No data from dataprovider");
		}	
		
	}	
	
	
	
	 /**
	    * Checks name and topic for block of information: Theses - Reader.
	    * 
	    * @param dataArg Object to find out specific person's information.
	    * @param fileInformationArg String of the processed file path.
	    * @param rowInformationArg String of the processed row.
	    */		
	@Test(dataProvider = "dataOfSupervisorTab", description="Checks name and topic for block of information: Theses - Reader.")
	public void checkRoleThesesReader(SupervisorTabData dataArg, String fileInformationArg, String rowInformationArg) throws Exception {
		
		String[] readerArray = dataArg.getThesesReader().split(PERSON_SEPARATOR, -1);
		
		// checks count of items
		if(readerArray[0] != "") {		
			this.openPersonalPageAndClickSupervisorTab(dataArg.getUco());

			Iterator<String> reader = Arrays.asList(readerArray).iterator();	
			String cssLocator, message;	
			int count = 1;
			int listIndex = 0;
			
			for (Iterator<String> item = reader; item.hasNext();) {
				// splits current item to person name and work name by given separator
				String[] nameAndTopic = item.next().split(NAME_WORK_SEPARATOR, -1);				

				// distinguishes which part of the list is current
				String part = "";
				if (count > 10) {
					part = "dl + p + ";	
					// resets listIndex counter
					if(count == 11) { listIndex = 0; }
					// clicks switch for more items 	
					reporterFormatedLog("Click on switch for more results");
					selenium.click("css=div#_skolitel h5:contains('Oponent') + dl + p a[class*='rozbal']");	
				}				
				
				cssLocator = "css=div#_skolitel h5:contains('Oponent') + " + part + "dl dd:nth(" + Integer.toString(listIndex) + ") a";				
				message = "Verify that \"" + cssLocator +  "\" matches \"" + nameAndTopic[0] + "\"";
				verifyEquals(selenium.getText(cssLocator), nameAndTopic[0], message);
				cssLocator = "css=div#_skolitel h5:contains('Oponent') + " + part + "dl dt:nth(" + Integer.toString(listIndex) + ") a";				
				message = "Verify that \"" + cssLocator +  "\" matches '" + nameAndTopic[1] + "\"";
				verifyEquals(selenium.getText(cssLocator), nameAndTopic[1], message);
						
				// checking correct redirect to page with specific work name and person name
				this.clickAndWait(cssLocator, "30000");
				cssLocator = "css=h1#nadpis";				
				message ="Verify that \"" + cssLocator +  "\" contains \"Archiv závěrečné práce " + nameAndTopic[0] + "\"";
				verifyTrue(selenium.getText(cssLocator).contains("Archiv závěrečné práce " + nameAndTopic[0]), message);
				cssLocator = "css=div#metadata>h3";				
				message ="Verify that \"" + cssLocator +  "\" contains \"" + nameAndTopic[0] + "\"";
				verifyTrue(selenium.getText(cssLocator).contains(nameAndTopic[0]), message);
				cssLocator = "css=div#metadata>h2";
				message ="Verify that \"" + cssLocator +  "\" matches \"" + nameAndTopic[1] + "\"";
				verifyEquals(selenium.getText(cssLocator), nameAndTopic[1], message);
				this.goBackAndWait("30000");				

				count++;
				listIndex++;
			} // ends for
		} else {
			reporterFormatedLog("No data from dataprovider");
		}
	}		
	

	
	 /**
	    * Checks specific person's publications.
	    * 
	    * @param dataArg Object to find out specific person's information.
	    * @param fileInformationArg String of the processed file path.
	    * @param rowInformationArg String of the processed row.
	    */		
	@Test(dataProvider = "dataOfPublicationsTab", description="Checks specific person's publications.")
	public void checkPublications(PublicationsTabData dataArg, String fileInformationArg, String rowInformationArg) throws Exception {

		String[] publicationArray = dataArg.getPublications().split(PUBLICATION_SEPARATOR, -1);
		
		// checks count of items
		if(publicationArray[0] != "") {		
			this.openPersonalPageAndClickPublicationsTab(dataArg.getUco());

			Iterator<String> publications = Arrays.asList(publicationArray).iterator();	
			String cssLocator, message, publication;	
			int listIndex = 0;
			
			for (Iterator<String> item = publications; item.hasNext();) {
				publication = item.next();
				cssLocator = "css=div#_publikace ul.vyhl_publ>li:nth(" + Integer.toString(listIndex) + ") div.publikace_zahlavi a";				
				message = "Verify that \"" + cssLocator +  "\" matches \"" + publication + "\"";
				verifyEquals(selenium.getText(cssLocator), publication, message);
				
				// checking correct redirect to page with specific publication
				this.clickAndWait(cssLocator, "30000");
				cssLocator = "css=h1#nadpis";				
				message ="Verify that \"" + cssLocator +  "\" matches \"" + publication + "\"";
				verifyEquals(selenium.getText(cssLocator), publication, message);
				this.goBackAndWait("30000");
				

				listIndex++;
			} // ends for
		} else {
			reporterFormatedLog("No data from dataprovider");
		}
		
	}
	
	
}
