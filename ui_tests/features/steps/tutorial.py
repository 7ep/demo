from behave import *
from selenium import webdriver
from selenium.webdriver.common.keys import Keys
import time
import os


@given('we have behave installed')
def step_impl(context):
  pass

@when('we implement a test')
def step_impl(context):
  assert True is not False

@then('behave will test it for us')
def step_impl(context):
  assert context.failed is False

@given('we have a web app available to run')
def step_impl(context):
  pass

@when('we register to this web app with existing user')
def step_impl(context):
  driver = webdriver.Chrome()

  driver.get("http://localhost:8888/demo")
  title = driver.title
  assert "Web Demo" in title

  username = driver.find_element_by_id("register_username")
  username.clear()
  username.send_keys("alice")

  password = driver.find_element_by_id("register_password")
  password.clear()
  password.send_keys("whatever")

  register_button = driver.find_element_by_id("register_submit")
  time.sleep(1)
  register_button.click()
  context.driver = driver


@then('it indicates already registered')
def step_impl(context):
  result = context.driver.find_element_by_id('result')
  assert "ALREADY_REGISTERED" in result.text
  context.driver.close()


  
