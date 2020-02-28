from behave import given, when, then
from hamcrest import *

empty_database = ''
one_user_registered = 'alice password123'
URL = 'http://localhost:8080/demo/library.html'
DEFAULT_USERNAME = 'alice'
DEFAULT_PASSWORD = 'asdfkljhasdfishdfksaljdfh'


@given('I am not registered')
def step_impl(context):
    pass


@when('I register with a valid username and password')
def step_impl(context):
    __register_user(context, DEFAULT_USERNAME, DEFAULT_PASSWORD)
    pass


@then('it indicates I am successfully registered')
def step_impl(context):
    result = context.driver.find_element_by_id('result')
    assert_that(result.text, contains_string('successfully registered: true'))


@given('I am registered as a user')
def step_impl(context):
    context.username = DEFAULT_USERNAME
    context.password = DEFAULT_PASSWORD
    __register_user(context, context.username, context.password)
    pass


@when('I login')
def step_impl(context):
    __login_user(context, context.username, context.password)
    pass


@then('the system allows secure access')
def step_impl(context):
    result = context.driver.find_element_by_id('result')
    assert_that(result.text, contains_string('access granted'))


def __register_user(context, username_text, password_text):
    driver = context.driver
    driver.get(URL)
    username = driver.find_element_by_id("register_username")
    username.clear()
    username.send_keys(username_text)
    password = driver.find_element_by_id("register_password")
    password.clear()
    password.send_keys(password_text)
    submit_button = driver.find_element_by_id("register_submit")
    submit_button.click()


def __login_user(context, username_text, password_text):
    driver = context.driver
    driver.get(URL)
    username = driver.find_element_by_id("login_username")
    username.clear()
    username.send_keys(username_text)
    password = driver.find_element_by_id("login_password")
    password.clear()
    password.send_keys(password_text)
    submit_button = driver.find_element_by_id("login_submit")
    submit_button.click()