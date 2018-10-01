Demo - demonstrates an application and tests

dependencies: Java 8.

To build with tests:
    On Linux
    ./gradlew clean build

    On Windows
    gradlew clean build

To run:
    On Linux
    ./gradlew appRun

    On Windows
    gradlew appRun



For convenience, there is a docker-compose file which will start up
Jenkins. The path to the Jenkinsfile is jenkins/Jenkinsfile.  In order
to run Jenkins you will need both Docker and docker-compose installed.

Also, see the ui_tests directory to see what can be done with a little
Python and Selenium, using Behave to run BDD tests.
