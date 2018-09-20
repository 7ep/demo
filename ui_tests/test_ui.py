from selenium import webdriver
from selenium.webdriver.common.keys import Keys
import time
import os

try:
    os.remove("../database.txt")
except OSError:
    pass

driver = webdriver.Chrome()

driver.get("http://localhost:8888/demo")
title = driver.title
assert "Web Demo" in title

username = driver.find_element_by_id("username")
username.clear()
username.send_keys("someuser")

password = driver.find_element_by_id("password")
password.clear()
password.send_keys("somepass")

register_button = driver.find_element_by_id("say-hello-button")
time.sleep(1)
register_button.click()


title = driver.title
assert "Registered Page" in title

time.sleep(1)

driver.close()
