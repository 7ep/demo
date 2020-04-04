from behave import given, when, then
from hamcrest import *

@when('I calculate the {n:d}th fibonacci number')
def step_impl(context, n):
    __calculate_fibonacci(context, n)
    pass


def __calculate_fibonacci(context, n):
    driver = context.driver
    driver.get("http://localhost:8080/demo")
    param_n = driver.find_element_by_id("fib_param_n")
    param_n.clear()
    param_n.send_keys(n)
    submit_button = driver.find_element_by_id("calculate_fibonacci")
    submit_button.click()


@then('the Fibonacci function indicates that the result is {result_val}')
def step_impl(context, result_val):
    result = context.driver.find_element_by_id('result')
    assert_that(result.text, contains_string(result_val))

