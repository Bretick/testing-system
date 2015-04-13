Testing system prototype for university IS
==

*Prototype of stand-alone system for automated functional testing of web app of Masaryk University Information System* 

The prototype is based on tools **Selenium**, **TestNG**, **Ant** and **Jenkins**.


Description of source folders
--
- documents/ .............................. Tests documentation
- htmltests/ ................................. HTML Selenese test suites
- jenkins-screens/ ...................... Screenshots of Jenkins settings
- sample-reports/ ....................... Output reports from sample testing
- testng/ ..................................... TestNG (JAVA) test suites



Scheme of the stand-alone system
--
The scheme of a stand-alone system for automated functional testing of web applications. Orange boundary is virtual machine on which is present operation system, web browsers and testing tools. Developer prepares on his computer set of tests and uploads them to the repository of source files. Then developer sets up Jenkins, it executes update of source files from repository and starts testing via Selenium. Selenium launches web browser and operates with web application. Finally Jenkins processes output results after finishing of tests execution and sends message with information about results to appropriate persons.
<img src="http://www.bretick.cz/uploads/images/work01/2012-01-system-testovani-pro-univerzitni-is-ukazka08en.jpg"/> 
        

Additional info
--
Sources of this project are part of my final thesis from Bachelor's studies at Masaryk University, the Faculty of Informatics in Brno, Czech Republic. The topic of the thesis is **Web applications functional testing** http://is.muni.cz/th/325233/fi_b/
            

*Abstract of the thesis*

The intention of the work is to explore freely available tools to automate functional testing of web applications and then use a selected combination of tools for testing the agenda People of the Masaryk Univerzity Information System. Another intention lies on creation specific test suite of automated functional tests and designing a solution of independently executed testing system with e-mail notifications about results for included persons. For purpose of better understanding, there are also summarized methods and types of web applications testing.


