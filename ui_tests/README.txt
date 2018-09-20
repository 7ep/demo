In order to run the Selenium tests, you need a driver for the browser.

For Chromedriver:

First, run this to see what is the latest release:

  curl https://chromedriver.storage.googleapis.com/LATEST_RELEASE 

It will output something like:

  2.42

Then, use that to create a URL, in the following template:

  https://chromedriver.storage.googleapis.com/RELEASE_VALUE_HERE/chromedriver_win32.zip


for example:

  curl https://chromedriver.storage.googleapis.com/2.42/chromedriver_win32.zip --output chromedriver_win32.zip

unzip the chromedriver into this directory.

