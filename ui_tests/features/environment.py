from selenium import webdriver
import subprocess
import os

def before_all(context):

    context.driver = webdriver.Chrome()

def before_scenario(context, scenario):
    __reset_database()

def after_all(context):
    context.driver.close()

def __reset_database():
    print ("\n********************************")
    print ("         RUNNING RESTORE...     ")
    cmd = ['pg_restore', '--username=postgres', '--dbname', 'training', '--no-password', '--clean', '../db_sample_files/initial_empty_state_v2.dump']
    FNULL = open(os.devnull, 'w')
    subprocess.call(cmd, stdout=FNULL, stderr=subprocess.STDOUT)
    print ("          ...  FINISHED         ")
    print ("********************************\n")