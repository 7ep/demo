Directories and files at the top level:

Directories
-----------
desktop_app  :   a project to demonstrate testing of desktop apps
docs         :   documents related to this application
gradle       :   necessary wrapper files and some capabilities related to certain tests
jenkins      :   holds the Jenkinsfile for CI/CD
src          :   has the application's source code and all test code

Files
-----
.gitattributes          :    certain configuration settings for Git
.gitignore              :    files and directories we don't want stored in Git
build.gradle            :    the primary Gradle configuration file
gradlew / gradlew.bat   :    runs a "wrapper" version of gradle so that
                             it is not necessary to have gradle installed to run it
LICENSE                 :    the license for the application
Pipfile, Pipefile.lock  :    used by Pipenv to list needed dependencies
DIRECTORY_README.txt    :    this file
README.md               :    a description of the project and how to use it