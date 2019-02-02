from selenium import webdriver
import requests

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
    context.driver = webdriver.Chrome()


def before_scenario(context, scenario):
    __reset_database()


def after_all(context):
    __close_browser(context)


def __close_browser(context):
    context.driver.close()


def __reset_database():
    requests.get("%s/demo/dbutils" % URL)