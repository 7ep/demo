from behave import *
from selenium import webdriver
from selenium.webdriver.common.keys import Keys
import time
import os

@given('^I am not registered$')
def step_impl(context):
  pass

@when('^I register with a username of "(.*)" and a password of "(.*)"$')
def step_impl(context):
  pass

@then('^it indicates I am successfully registered$')
def step_impl(context):
  pass


@given('^I am registered as "(.*)"$')
def step_impl(context):
  pass

@when('^I try to register again$')
def step_impl(context):
  pass

@then('^it indicates I am already registered$')
def step_impl(context):
  pass

@then('^it indicates that I used a poor password$')
def step_impl(context):
  pass

@given('^I am registered as "(.*)" with a password of "(.*)"$')
def step_impl(context):
  pass

@when('^I login with those credentials$')
def step_impl(context):
  pass

@then('^the system allows secure access$')
def step_impl(context):
  pass

@given('^There is no user with the username "(.*)"$')
def step_impl(context):
  pass

@when('^I login with that username and any password$')
def step_impl(context):
  pass

@then('^the system denies me access$')
def step_impl(context):
  pass