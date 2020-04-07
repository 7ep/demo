from behave import given, when, then
from hamcrest import *

@when('I calculate ackermann\'s function with an m of {param_m_val:d} and an n of {param_n_val:d}')
def step_impl(context, param_m_val, param_n_val):
    __calculate_ackermann(context, param_m_val, param_n_val)
    pass


def __calculate_ackermann(context, param_m_val, param_n_val):
    driver = context.driver
    driver.get("http://localhost:8080/demo")
    param_m = driver.find_element_by_id("ack_param_m")
    param_m.clear()
    param_m.send_keys(param_m_val)
    param_n = driver.find_element_by_id("ack_param_n")
    param_n.clear()
    param_n.send_keys(param_n_val)
    submit_button = driver.find_element_by_id("calculate_ackermann")
    submit_button.click()


@then('Ackermann\'s function indicates that the result is {result_val}')
def step_impl(context, result_val):
    result = context.driver.find_element_by_id('result')
    assert_that(result.text, contains_string(result_val))

