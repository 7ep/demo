import unittest
import requests
import subprocess
import os


class TestStringMethods(unittest.TestCase):

    @staticmethod
    def __reset_database():
        cmd = ['pg_restore',
               '--username=postgres',
               '--dbname', 'training',
               '--no-password',
               '--clean', '../db_sample_files/initial_empty_state_v2.dump']
        FNULL = open(os.devnull, 'w')
        subprocess.call(cmd, stdout=FNULL, stderr=subprocess.STDOUT)
        FNULL.close()

    def test_math_api(self):
        r = requests.post("http://localhost:8080/demo/math", data = {'item_a':'9', 'item_b':'7'})
        self.assertTrue("Result: 16" in r.text)

    # register with a good password and an empty database
    def test_register_api(self):
        self.__reset_database()
        r = requests.post("http://localhost:8080/demo/register", data = {'username':'alice', 'password':'B65S3xNW8vXQHyjYnD72L3mejc'})
        self.assertTrue("wasSuccessfullyRegistered=true,status=SUCCESSFULLY_REGISTERED" in r.text)

    # register, then login
    def test_login_api(self):
        self.__reset_database()
        r = requests.post("http://localhost:8080/demo/register", data = {'username':'alice', 'password':'B65S3xNW8vXQHyjYnD72L3mejc'})
        r = requests.post("http://localhost:8080/demo/login", data = {'username':'alice', 'password':'B65S3xNW8vXQHyjYnD72L3mejc'})
        self.assertTrue("Result: access granted" in r.text)


if __name__ == '__main__':
    unittest.main()


