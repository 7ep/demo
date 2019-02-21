import os
import time
from selenium import webdriver

try:
    os.remove("../database.txt")
except OSError:
    pass

driver = webdriver.Chrome()

driver.get("http://localhost:8888/demo")
title = driver.title
assert "Web Demo" in title

username = driver.find_element_by_id("login_username")
username.clear()
username.send_keys("someuser")

password = driver.find_element_by_id("login_password")
password.clear()
password.send_keys("somepass")

register_button = driver.find_element_by_id("login_submit")
time.sleep(1)
register_button.click()

title = driver.title
try:
    assert "Registered Page" in title
except AssertionError:
    print("assertion failed")
    driver.close()
    exit()

time.sleep(1)

driver.close()
