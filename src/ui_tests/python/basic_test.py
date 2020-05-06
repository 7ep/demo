import pytest
import time
import json
from selenium import webdriver
from selenium.webdriver.common.by import By
from selenium.webdriver.common.action_chains import ActionChains
from selenium.webdriver.support import expected_conditions
from selenium.webdriver.support.wait import WebDriverWait
from selenium.webdriver.common.keys import Keys
from selenium.webdriver.support.select import Select
from selenium.webdriver.common.desired_capabilities import DesiredCapabilities
import requests
from selenium.webdriver.common.proxy import Proxy, ProxyType
from hamcrest import *
import time

SERVER = "localhost"
URL = "http://%s:8080" % SERVER
HOMEPAGE = "%s/demo/library.html" % URL
RESET_DATABASE = "%s/demo/flyway" % URL
DEFAULT_BORROWER = "alice"

class TestBasic():

  def setup_class(self):
    self.driver = webdriver.Chrome()
    self.vars = {}
  
  def teardown_class(self):
    self.driver.quit()
  
  def test_basic_lend_book(self):
    self.driver.get("http://localhost:8080/demo/library.html")
    self.driver.find_element(By.CSS_SELECTOR, ".button-form:nth-child(4) > input").click()
    self.driver.find_element(By.LINK_TEXT, "Return").click()
    self.driver.find_element(By.ID, "register_book").click()
    self.driver.find_element(By.ID, "register_book").send_keys("some book")
    self.driver.find_element(By.ID, "register_book_submit").click()
    self.driver.find_element(By.LINK_TEXT, "Return").click()
    self.driver.find_element(By.ID, "register_borrower").click()
    self.driver.find_element(By.ID, "register_borrower").send_keys("some borrower")
    self.driver.find_element(By.ID, "register_borrower_submit").click()
    self.driver.find_element(By.LINK_TEXT, "Return").click()
    self.driver.find_element(By.ID, "lend_book").click()
    self.driver.find_element(By.ID, "lend_book").send_keys("some book")
    self.driver.find_element(By.ID, "lend_borrower").send_keys("some borrower")
    self.driver.find_element(By.ID, "lend_book_submit").click()
    assert self.driver.find_element(By.ID, "result").text == "SUCCESS"

  # TDD for a UI element that makes choosing a book easier
  #
  # Put more concretely - I want there to exist some UI widget
  # so that when I want to lend a book, I am easily able to
  # search the book by name.  A nice UI experience
  # would be some kind of input box that shows all the books, paged,
  # and filters the possibilities immediately as the user types.
  # let's make that, TDD-style.
  def test_list_select_pom(self):
    self.driver.get(RESET_DATABASE)
    library = LibraryPageObjectModel(self.driver)
    library.register_book("another book")
    library.register_borrower(DEFAULT_BORROWER)

    # Enter in part of a borrower's name, then select the person from a list
    self.driver.get(HOMEPAGE)
    self.driver.find_element(By.ID, "lend_book").send_keys(Keys.ARROW_UP)

    # and select a book in a si
    # and lend them the book...
    select = Select(self.driver.find_element(By.ID, "lend_book"))
    assert select.first_selected_option.text == "another book"




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

class LibraryPageObjectModel:

    def __init__(self, driver):
        self.driver = driver

    def register_user(self, username, password):
        self.driver.get(HOMEPAGE)

        reg = Registration(driver)
        reg.enter_username(username)
        reg.enter_password(password)
        reg.enter()

    def login_user(self, username, password):
        self.driver.get(HOMEPAGE)

        login = Login(self.driver)
        login.enter_username(username)
        login.enter_password(password)
        login.enter()

    def add_numbers(self, a, b):
        self.driver.get(HOMEPAGE)

        sums = Summation(self.driver)
        sums.enter_addend_a(a)
        sums.enter_addend_b(b)
        sums.enter()

    def register_borrower(self, name):
        self.driver.get(HOMEPAGE)

        bor = BorrowerRegister(self.driver)
        bor.register_borrower(name)
        bor.enter()

    def register_book(self, title):
        self.driver.get(HOMEPAGE)

        br = BookRegister(self.driver)
        br.register_book(title)
        br.enter()

    def lend_book(self, title, borrower):
        self.driver.get(HOMEPAGE)

        bl = BookLend(self.driver)
        bl.enter_book(title)
        bl.enter_borrower(borrower)
        bl.enter()

    def result(self):
        return Result(self.driver)



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
    library = LibraryPageObjectModel(driver)
    library.register_book(book)
    library.register_borrower(borrower)
    library.lend_book(book, borrower)
    result = library.result()
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
