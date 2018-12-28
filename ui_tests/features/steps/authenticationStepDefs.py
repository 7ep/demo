from behave import *
from selenium import webdriver
from selenium.webdriver.common.keys import Keys
import time
import os

@given('^I am not registered$')
def step_impl(context):
  os.popen("pg_restore --host localhost --port 5432 --username postgres --dbname training --role postgres --no-password --clean db_sample_files/sample_db_v1.dump").read()
  pass

@when('^I register with a username of "(.*)" and a password of "(.*)"$')
def step_impl(context):
  pass

@then('^it indicates I am successfully registered$')
def step_impl(context):
  pass


@given('^I am registered as "(.*)"$')
def step_impl(context):
  os.popen("pg_restore --host localhost --port 5432 --username postgres --dbname training --role postgres --no-password --clean db_sample_files/one_person_in_table_already_v1.dump").read()
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
  os.popen("pg_restore --host localhost --port 5432 --username postgres --dbname training --role postgres --no-password --clean db_sample_files/one_person_in_table_already_v1.dump").read()
  pass

@when('^I login with those credentials$')
def step_impl(context):
  pass

@then('^the system allows secure access$')
def step_impl(context):
  pass

@given('^There is no user with the username "(.*)"$')
def step_impl(context):
  os.popen("pg_restore --host localhost --port 5432 --username postgres --dbname training --role postgres --no-password --clean db_sample_files/sample_db_v1.dump").read()
  pass

@when('^I login with that username and any password$')
def step_impl(context):
  pass

@then('^the system denies me access$')
def step_impl(context):
  pass