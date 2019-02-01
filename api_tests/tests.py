import unittest
import requests

SERVER = "localhost"
URL = "http://%s:8080" % SERVER


class TestStringMethods(unittest.TestCase):

    @staticmethod
    def __reset_database():
        r = requests.get("%s/demo/dbclear" % URL)

    def test_math_api(self):
        r = requests.post("%s/demo/math" % URL, data = {'item_a': '9', 'item_b': '7'})
        self.assertTrue("Result: 16" in r.text)

    # register with a good password and an empty database
    def test_register_api(self):
        self.__reset_database()
        r = requests.post("%s/demo/register" % URL, data = {'username': 'alice', 'password': 'B65S3xNW8vXQHyjYnD72L3mejc'})
        self.assertTrue("wasSuccessfullyRegistered=true,status=SUCCESSFULLY_REGISTERED" in r.text)

    # register, then login
    def test_login_api(self):
        self.__reset_database()
        r = requests.post("%s/demo/register" % URL, data = {'username': 'alice', 'password': 'B65S3xNW8vXQHyjYnD72L3mejc'})
        r = requests.post("%s/demo/login" % URL, data = {'username': 'alice', 'password': 'B65S3xNW8vXQHyjYnD72L3mejc'})
        self.assertTrue("Result: access granted" in r.text)


if __name__ == '__main__':
    unittest.main()


