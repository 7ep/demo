from selenium import webdriver
import subprocess
import os


# used as a simple object during console runs,
# to experiment.  typical incantation is:
#   context = Object()
class Object(object):
    pass


def __create_context():
    context = Object()
    return context


def before_all(context):
    __open_browser(context)


def __open_browser(context):
    context.driver = webdriver.Chrome()


def before_scenario(context, scenario):
    __reset_database()


def after_all(context):
    __close_browser(context)


def __close_browser(context):
    context.driver.close()


def __reset_database():
    print ("\n********************************")
    print ("         RUNNING RESTORE...     ")
    cmd = ['pg_restore', '--username=postgres', '--dbname', 'training', '--no-password', '--clean', '../db_sample_files/initial_empty_state_v2.dump']
    FNULL = open(os.devnull, 'w')
    subprocess.call(cmd, stdout=FNULL, stderr=subprocess.STDOUT)
    FNULL.close()
    print ("          ...  FINISHED         ")
    print ("********************************\n")