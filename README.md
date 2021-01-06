## Demo - demonstrates an application and tests

This is an application by [Coveros](https://www.coveros.com/) to demonstrate good
software practices.  As we say in agile... _Working software over comprehensive 
documentation_ ... but that doesn't mean we can't have pretty good documentation too. 

#### Quick Start:

* Install [Java](https://www.java.com/en/download/) if you don't already have it.
* Clone or [download](https://github.com/7ep/demo/archive/master.zip) this repo.  (if you download, unzip the file to a directory.)
* On the command line in the top directory of this repo, run `gradlew apprun`
* Visit the application with your browser at http://localhost:8080/demo

#### Summary:

Demo consists of a simple web application and tests.  Its goal is to provide 
an environment suitable for demonstration and practice in valuable development
techniques.  Some of the techniques exemplified are:
* [Unit](https://github.com/7ep/demo/blob/master/src/test/java/com/coveros/training/authentication/RegistrationUtilsTests.java) [tests](https://github.com/7ep/demo/blob/master/src/test/java/com/coveros/training/library/LibraryUtilsTests.java) developed by [TDD](https://en.wikipedia.org/wiki/Test-driven_development) using [Junit](https://junit.org/junit5/) as a driver and [Mockito](https://site.mockito.org/) for mocks, with coverage reports.
* [UI tests](https://github.com/7ep/demo/blob/master/src/ui_tests/behave/features/librarian_ui.feature) using [multiple frameworks](https://github.com/7ep/demo/tree/master/src/ui_tests)
* [BDD](https://en.wikipedia.org/wiki/Behavior-driven_development) [tests](https://github.com/7ep/demo/blob/master/src/bdd_test/resources/library/check_out_a_book.feature) using gherkin
  * [Cucumber](https://docs.cucumber.io/) tests, with reports
  * [Behave](https://behave.readthedocs.io/en/latest/) UI tests that use [Selenium](https://www.selenium.dev/) web driver.
* [Integration tests](https://github.com/7ep/demo/blob/master/src/integration_test/java/com/coveros/training/persistence/PersistenceLayerTests.java) that test the [H2 database](https://www.h2database.com/html/main.html)
* [Database versioning](https://github.com/7ep/demo/blob/master/src/main/resources/db/migration/V2__Rest_of_tables_for_auth_and_library.sql), with [Flyway](https://flywaydb.org/)
* Security analysis using [DependencyCheck](https://www.owasp.org/index.php/OWASP_Dependency_Check)
* Hot-swap code with [Gretty](https://github.com/gretty-gradle-plugin/gretty)
* Enhanced type system using [Checker Framework](https://checkerframework.org/)
* See its [architecture](https://github.com/7ep/demo/blob/master/docs/dev_notes/architecture.txt)


Its essential goals:
* Just works, any platform.
* As simple as possible
* Minimal system requirements
* Fast and easy to install and to run
* High test coverage
* Multiple business domains
* Easy to maintain and improve
* Well documented
* High performance
* Illustrates maximum number of techniques
* Easy to get up to speed

#### Table of contents:
1. [Optional dependencies](#optional-dependencies)
1. [Chromedriver installation notes](#chromedriver-installation-notes)
1. [Python installation notes](#python-installation-notes)
1. [To build and run tests](#to-build-and-run-tests)
1. [To run the application](#to-run-the-web-application)
1. [To run API and UI tests](#to-run-api-and-ui-tests)
1. [Summary of relevant Gradle commands](#summary-of-relevant-gradle-commands)
1. [The whole shebang - CI/CD pipeline](#the-whole-shebang---a-cicd-pipeline)

###### Optional Dependencies
If you want API testing and Selenium testing, you will need
to visit these links and download / install the applications found there.
* [Python](https://www.python.org/downloads/)
* [Chromedriver](http://chromedriver.chromium.org/downloads)
* [Chrome internet browser](https://www.google.com/chrome/)

---

#### Chromedriver installation notes
make sure that the [Chromedriver](https://chromedriver.chromium.org/) executable is installed in one of the directories that is 
on your path.  To see your path, type the following in a command line: 

on Windows:

    echo %PATH%  
    
On Mac/Linux:

    echo $PATH
    
If you run the command, `chromedriver` on the command  line, you should get a result similar to this:

    Starting ChromeDriver ...
        
#### Python installation notes
Python can be downloaded [here](https://www.python.org/downloads/)

To run API tests and Selenium tests, an easy way to handle its 
dependencies is to use *pipenv*.  To get this installed, first download
[get-pip.py](https://bootstrap.pypa.io/get-pip.py), and run the following on the command line:

    python get-pip.py
    
Then,

    pip install pipenv
   
And in the demo directory,
    
    pipenv install   
   
#### To build and run tests:
On the command line, run the following:

On Mac/Linux

    ./gradlew check

On Windows

    gradlew check

#### To run the web application:
On the command line, run the following:

On Mac/Linux

    ./gradlew apprun

On Windows

    gradlew apprun
    
Then, head to http://localhost:8080/demo    
    

#### To run API and UI tests:
Note: The app has to be [already running](#to-run-the-web-application) for these tests to pass, and you _need_
to have installed [Python] and [Chromedriver].

In a new terminal, separate from the one where the server is running, run the following:

On Mac/Linux

    ./gradlew runAllTests

On Windows

    gradlew runAllTests    
    
#### Summary of relevant Gradle commands
* gradlew coveros - show a cheat sheet of commands for Demo
* gradlew apprun - runs the application
* gradlew check - runs all tests possible with only dependency being Java 8.  No need for app to be running.
* gradlew runAllTests - runs the whole set of tests**  
* gradlew clean - cleans build products and installs pre-push hook. (see the file in this directory, pre-push)
* gradlew runBehaveTests - runs the UI tests**
* gradlew runApiTests - runs the API tests**
* gradlew generateCucumberReport - runs cucumber and creates a nice-looking HTML report
* gradlew pitest - runs mutation testing (see http://pitest.org/)
* gradlew dependencyCheckAnalyze - analyzes security reports for the dependencies of 
  this project.  See https://www.owasp.org/index.php/OWASP_Dependency_Check
* gradlew sonarqube - runs static analysis using SonarQube.  Sonarqube must be running - check http://localhost:9000
* gradlew integrate - runs the database integration tests
* gradlew startH2Console - Starts a console to examine the H2 database file.  (user and
  password are empty, URL to use is jdbc:h2:./build/db/training)
* gradlew <task 1>...<task N> taskTree - a utility that will show the task tree for a particular task


  ** Requires the app to be running 
     (usually in another terminal) and all optional dependencies installed.
    
#### The whole shebang - a CI/CD pipeline

Details on building out a CI/CD pipeline are found in the "docs/ci_and_cd" directory.
For example, to set it all up on a [local Windows box](https://raw.githubusercontent.com/7ep/demo/master/docs/ci_and_cd/ci_and_cd_for_localhost.txt) 

###### Features of the pipeline:
* A fully functioning and documented demonstration CI/CD pipeline using Jenkins.
* BDD tests run with a report generated
* Static analysis quality-gating using SonarQube
* UI tests running on Chrome
* Performance testing with Jmeter
* Security analysis with OWASP's "DependencyCheck"
* Complex commands wrapped simply using Gradle
* Mutation testing with Pitest
* Javadocs built



---

## Screenshots:
![Jenkins pipeline](https://c2.staticflickr.com/8/7889/33202009658_11422b7f20_b.jpg)

![Zap attach proxy](https://c2.staticflickr.com/8/7905/33202009438_8f367e20ec_o.png)

![SonarQube analysis](https://c2.staticflickr.com/8/7823/33202009548_e678128200_b.jpg)

![Running performance tests](https://c2.staticflickr.com/8/7854/47077017751_7e045f68dd_b.jpg)
