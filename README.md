Testing system prototype for university IS
==

Prototype of stand-alone system for automated functional testing of web app of Masaryk University Information System.

The prototype is based on tools **Selenium**, **TestNG**, **Ant** and **Jenkins**.


Description of source folders
--
* source/
- documents/ ............................ Test documentation
- htmltests/ ............................ HTML Selenese test suites
- jenkins-screens/ ...................... Screenshots of Jenkins settings
- sample-reports/ ....................... Output reports from sample testing
- testng/ ............................... TestNG (JAVA) test suites



Scheme of the stand-alone system
--
The scheme of a stand-alone system for automated functional testing of web applications. Orange boundary is virtual machine on which is present operation system, web browsers and testing tools. Developer prepares on his computer set of tests and uploads them to the repository of source files. Then developer sets up Jenkins, it executes update of source files from repository and starts testing via Selenium. Selenium launches web browser and operates with web application. Finally Jenkins processes output results after finishing of tests execution and sends message with information about results to appropriate persons.

[<img src="http://www.bretick.cz/uploads/images/work01/2012-01-system-testovani-pro-univerzitni-is-ukazka08en.jpg"/>]


