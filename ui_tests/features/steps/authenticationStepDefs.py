from behave import given, when, then
from hamcrest import *


empty_database = ''
one_user_registered = 'alice password123'

@given('I am not registered')
def step_impl(context):
    __reset_database(empty_database)
    pass


@when('I register with a username of "{username_text}" and a password of "{password_text}"')
def step_impl(context, username_text, password_text):
    __register_user(context, username_text, password_text)
    pass


def __register_user(context, username_text, password_text):
    driver = context.driver
    driver.get("http://localhost:8080/demo")
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
    driver.get("http://localhost:8080/demo")
    username = driver.find_element_by_id("login_username")
    username.clear()
    username.send_keys(username_text)
    password = driver.find_element_by_id("login_password")
    password.clear()
    password.send_keys(password_text)
    submit_button = driver.find_element_by_id("login_submit")
    submit_button.click()


@then('it indicates I am successfully registered')
def step_impl(context):
    result = context.driver.find_element_by_id('result')
    assert_that(result.text, contains_string('wasSuccessfullyRegistered=true'))


@when('I try to register again')
def step_impl(context):
    __register_user(context, context.username, context.password)
    pass


@then('it indicates I am already registered')
def step_impl(context):
    result = context.driver.find_element_by_id('result')
    assert_that(result.text, contains_string('wasSuccessfullyRegistered=false'))


@then('it indicates that I used a poor password')
def step_impl(context):
    result = context.driver.find_element_by_id('result')
    assert_that(result.text, contains_string('INSUFFICIENT_ENTROPY'))


@given('I am registered as "{username}" with a password of "{password}"')
def step_impl(context, username, password):
    __reset_database(username + ' ' + password)
    context.username = username
    context.password = password
    pass


@when('I login with those credentials')
def step_impl(context):
    __login_user(context, context.username, context.password)
    pass


@then('the system allows secure access')
def step_impl(context):
    result = context.driver.find_element_by_id('result')
    assert_that(result.text, contains_string('access granted'))


@given('There is no user with the username "{username}"')
def step_impl(context, username):
    __reset_database(empty_database)
    context.username = username
    pass


@when('I login with that username and any password')
def step_impl(context):
    __login_user(context, context.username, "anypassword")
    pass


@then('the system denies me access')
def step_impl(context):
    result = context.driver.find_element_by_id('result')
    assert_that(result.text, contains_string('access denied'))


def __reset_database(contents):
    with open('..\\authentication.txt', 'w') as f:
        f.write(contents)