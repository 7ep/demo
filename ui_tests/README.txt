We need a few things installed before continuing here.

First, you will need Python 3.7 for your system.
Next you will need pipenv, a tool that makes it easy to get the dependencies you need.

To get pipenv:

first download get-pip.py from https://bootstrap.pypa.io/get-pip.py, and run

    python get-pip.py

Then,

    pip install pipenv

And in the demo directory,

    pipenv install

In order to run the Selenium tests with these instructions, you need Chrome and a driver for the browser.
Download Chromedriver (check http://chromedriver.chromium.org/) and make sure it's in your path or in the ui_tests directory.
