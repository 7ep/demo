import requests
from selenium import webdriver
from selenium.webdriver.common.proxy import Proxy, ProxyType
from hamcrest import *
import time

SERVER = "localhost"
URL = "http://%s:8080" % SERVER
HOMEPAGE = "%s/demo/library.html" % URL
RESET_DATABASE = "%s/demo/flyway" % URL

# used as a simple object during console runs,
# to experiment.  typical incantation is:
#   context = Object()
class Object(object):
    pass


# all the important capabilities for the Login function
class Login:
    def __init__(self, driver):
        self.driver = driver

    def enter_username(self, text):
        login_username_field = self.driver.find_element_by_id("login_username")
        login_username_field.send_keys(text)

    def enter_password(self, text):
        login_password_field = self.driver.find_element_by_id("login_password")
        login_password_field.send_keys(text)

    def enter(self):
        login_button = self.driver.find_element_by_id("login_submit")
        login_button.click()


# all the important capabilities for the Registration function
class Registration:
    def __init__(self, driver):
        self.driver = driver

    def enter_username(self, text):
        register_username_field = self.driver.find_element_by_id("register_username")
        register_username_field.send_keys(text)

    def enter_password(self, text):
        register_password_field = self.driver.find_element_by_id("register_password")
        register_password_field.send_keys(text)

    def enter(self):
        register_button = self.driver.find_element_by_id("register_submit")
        register_button.click()


# all the important capabilities for the Book Registration function
class BookRegister:
    def __init__(self, driver):
        self.driver = driver

    def register_book(self, text):
        register_book_field = self.driver.find_element_by_id("register_book")
        register_book_field.send_keys(text)

    def enter(self):
        register_button = self.driver.find_element_by_id("register_book_submit")
        register_button.click()


# all the important capabilities for the Borrower Registration function
class BorrowerRegister:
    def __init__(self, driver):
        self.driver = driver

    def register_borrower(self, text):
        register_borrower_field = self.driver.find_element_by_id("register_borrower")
        register_borrower_field.send_keys(text)

    def enter(self):
        register_button = self.driver.find_element_by_id("register_borrower_submit")
        register_button.click()


# all the important capabilities for the Book Lending function
class BookLend:
    def __init__(self, driver):
        self.driver = driver

    def enter_book(self, text):
        book_field = self.driver.find_element_by_id("lend_book")
        book_field.send_keys(text)

    def enter_borrower(self, text):
        borrower_field = self.driver.find_element_by_id("lend_borrower")
        borrower_field.send_keys(text)

    def enter(self):
        lend_button = self.driver.find_element_by_id("lend_book_submit")
        lend_button.click()


# all the important capabilities for the Summation function
class Summation:
    def __init__(self, driver):
        self.driver = driver

    def enter_addend_a(self, text):
        addend_a = self.driver.find_element_by_id("addend_a")
        addend_a.send_keys(text)

    def enter_addend_b(self, text):
        addend_b = self.driver.find_element_by_id("addend_b")
        addend_b.send_keys(text)

    def enter(self):
        lend_button = self.driver.find_element_by_id("math_submit")
        lend_button.click()

# all the important capabilities for the Result page
class Result:
    def __init__(self, driver):
        self.driver = driver

    def get_result_text(self):
        return self.driver.find_element_by_id("result").text



def register_user(driver, username, password):
    driver.get(HOMEPAGE)

    reg = Registration(driver)
    reg.enter_username(username)
    reg.enter_password(password)
    reg.enter()

def login_user(driver, username, password):
    driver.get(HOMEPAGE)

    login = Login(driver)
    login.enter_username(username)
    login.enter_password(password)
    login.enter()

def add_numbers(driver, a, b):
    driver.get(HOMEPAGE)

    sums = Summation(driver)
    sums.enter_addend_a(a)
    sums.enter_addend_b(b)
    sums.enter()

def register_borrower(driver, name):
    driver.get(HOMEPAGE)

    bor = BorrowerRegister(driver)
    bor.register_borrower(name)
    bor.enter()

def register_book(driver, title):
    driver.get(HOMEPAGE)

    br = BookRegister(driver)
    br.register_book(title)
    br.enter()

def lend_book(driver, title, borrower):
    driver.get(HOMEPAGE)

    bl = BookLend(driver)
    bl.enter_book(title)
    bl.enter_borrower(borrower)
    bl.enter()



# opens a browser, returns a handle for it
def start_testing():
    return open_browser()


# an example of page-object-model testing
def register_and_login():
    driver = start_testing()
    username = "bob"
    password = "fWd8SNtALsKScD9xYUm5Jb"

    driver.get(RESET_DATABASE)
    driver.get(HOMEPAGE)

    reg = Registration(driver)
    reg.enter_username(username)
    reg.enter_password(password)
    reg.enter()

    driver.get(HOMEPAGE)

    login = Login(driver)
    login.enter_username(username)
    login.enter_password(password)
    login.enter()

    result = Result(driver)
    result_text = result.get_result_text()
    assert_that(result_text, contains_string('access granted'))

    driver.close()

# an example of a more abstracted page-object-model
def full_lend_book():
    driver = start_testing()
    driver.get(RESET_DATABASE)
    book = "alice in wonderland"
    borrower = "bob"
    register_book(driver, book)
    register_borrower(driver, borrower)
    lend_book(driver, book, borrower)
    result = Result(driver)
    assert_that(result.get_result_text(), equal_to("SUCCESS"))
    driver.close()



def open_browser():
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

        driver = webdriver.Chrome(desired_capabilities=capabilities)
        return driver
    except:
        # if we got an exception while trying to hit a proxy URL,
        # it probably means the proxy isn't available, so we run
        # without a proxy, as follows.
        driver = webdriver.Chrome()
        return driver
