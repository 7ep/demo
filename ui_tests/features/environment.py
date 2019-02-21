import requests
from selenium import webdriver
from selenium.webdriver.common.proxy import Proxy, ProxyType

SERVER = "localhost"
URL = "http://%s:8080" % SERVER


# used as a simple object during console runs,
# to experiment.  typical incantation is:
#   context = Object()
class Object(object):
    pass


def __create_context():
    context = Object()
    return context


def before_all(context):
    __open_browser(context)


def __open_browser(context):
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

        context.driver = webdriver.Chrome(desired_capabilities=capabilities)
    except:
        context.driver = webdriver.Chrome()


def before_scenario(context, scenario):
    __reset_database()


def after_all(context):
    __close_browser(context)


def __close_browser(context):
    context.driver.close()


def __reset_database():
    requests.get("%s/demo/flyway" % URL)
