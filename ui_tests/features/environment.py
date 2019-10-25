import requests
from selenium import webdriver
from selenium.webdriver.common.proxy import Proxy, ProxyType

SERVER = "localhost"
URL = "http://%s:8080" % SERVER


def before_all(context):
    __open_browser(context)


def __open_browser(context):
    chrm = context.config.userdata['chromedriver_path']
    
    try:
        # if there is a proxy, we'll use it.  Otherwise, we won't.
        requests.get("http://localhost:8888", timeout=0.01)

        # if there was no exception, we continue here.
        PROXY = "localhost:8888"

        proxy = Proxy()
        proxy.proxy_type = ProxyType.MANUAL
        proxy.http_proxy = PROXY

        capabilities = webdriver.DesiredCapabilities.CHROME
        proxy.add_to_capabilities(capabilities)
        
        if (chrm):
            context.driver = webdriver.Chrome(desired_capabilities=capabilities, executable_path=chrm)
        else:
            context.driver = webdriver.Chrome(desired_capabilities=capabilities)
        return context.driver
    except:
        if (chrm):
            context.driver = webdriver.Chrome(executable_path=chrm)
        else:
            # adding the service args as described below will cause Chromedriver
            # to create a log of the communication between it and the Chrome
            # browser.  It's eye-opening. 
            #
            # for instance:
	        #   [1568045962.076][INFO]: [e18882b1f2abbda89f232f777f98f686] COMMAND TypeElement {
	        #      "id": "0.47079920350295135-1",
	        #      "sessionId": "e18882b1f2abbda89f232f777f98f686",
	        #      "text": "Byron",
	        #      "value": [ "B", "y", "r", "o", "n" ]
	        #   }
            #context.driver = webdriver.Chrome(service_args=["--verbose","--logepath=C:\\temp\\qc1.log"])
            context.driver = webdriver.Chrome()
        return context.driver


def before_scenario(context, scenario):
    __reset_database()


def after_all(context):
    __close_browser(context)


def __close_browser(context):
    context.driver.close()


def __reset_database():
    requests.get("%s/demo/flyway" % URL)
