In order to run the Selenium tests, you need a driver for the browser.

Have Python
Install Pip - a package installer for Python
Download Behave, a BDD framework for Python, using Pip
Download Selenium using Pip
Download Chromedriver and make sure it's in your path.


the following was tested using intellij and python 3.7
cheat sheet to playing with Selenium and Python

########################################
#    First, the preliminaries          #
########################################

Make sure that ui_tests is marked as a python module:
click on File -> Project Structure -> Modules
Click on ui_tests and provide its SDK as python 3.7

Start a python console by clicking on Tools -> Python Console

# load up all our functions
runfile('features/environment.py')
runfile('features/steps/authenticationStepDefs.py')

# create a context object (to carry context between methods)
context = __create_context()

########################################
#    Now, a sample run.                #
########################################

# clear the database
__reset_database()

# open the browser
__open_browser(context)

# some activity options (choose one)
__register_user(context, "Byron", "OAh8Wq8CajGZwURSXI8uSS")
__login_user(context, "Byron", "OAh8Wq8CajGZwURSXI8uSS")


# close the browser
__close_browser(context)

Also note that you can run individual lines in the code by putting your
cursor on a line and pressing alt+shift+e