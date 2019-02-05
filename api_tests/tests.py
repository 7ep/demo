import unittest
import requests

SERVER = "localhost"
URL = "http://%s:8080" % SERVER


class TestStringMethods(unittest.TestCase):

    # hitting this endpoint will reset the database.
    @staticmethod
    def __reset_database():
        requests.get("%s/demo/flyway" % URL)

    # adding a couple integers
    def test_math_api_happy_path(self):
        r = requests.post("%s/demo/math" % URL, data = {'item_a': '9', 'item_b': '7'})
        self.assertTrue("Result: 16" in r.text)

    # does it handle negatives well?
    def test_math_api_negative_numbers(self):
        r = requests.post("%s/demo/math" % URL, data = {'item_a': '-9', 'item_b': '-7'})
        self.assertTrue("Result: -16" in r.text)

    # Only handles integers.  Will fail with decimals.
    def test_math_api_negative_numbers(self):
        r = requests.post("%s/demo/math" % URL, data = {'item_a': '-9.1', 'item_b': '-7.1'})
        self.assertTrue("Error: only accepts integers" in r.text)

    # does it handle zero well?
    def test_math_api_with_zero(self):
        r = requests.post("%s/demo/math" % URL, data = {'item_a': '9', 'item_b': '0'})
        self.assertTrue("Result: 9" in r.text)

    # provide non-digits as input, should return an error message in the response.
    def test_math_api_with_non_numeric(self):
        r = requests.post("%s/demo/math" % URL, data = {'item_a': 'nine', 'item_b': 'seven'})
        self.assertTrue("Error: only accepts integers" in r.text)


    # register with a good password and an empty database
    def test_register_api(self):
        self.__reset_database()
        r = requests.post("%s/demo/register" % URL, data = {'username': 'alice', 'password': 'B65S3xNW8vXQHyjYnD72L3mejc'})
        self.assertTrue("wasSuccessfullyRegistered=true,status=SUCCESSFULLY_REGISTERED" in r.text)

    # register, then login
    def test_login_api(self):
        self.__reset_database()
        requests.post("%s/demo/register" % URL, data = {'username': 'alice', 'password': 'B65S3xNW8vXQHyjYnD72L3mejc'})
        r = requests.post("%s/demo/login" % URL, data = {'username': 'alice', 'password': 'B65S3xNW8vXQHyjYnD72L3mejc'})
        self.assertTrue("Result: access granted" in r.text)

    # test that we get the correct result if we try to register a book
    def test_register_book(self):
        self.__reset_database()
        r = requests.post("%s/demo/registerbook" % URL, data = {'book': 'alice in wonderland'})
        self.assertTrue("Result: SUCCESS" in r.text)

    # test that we get the correct result if we try to register a book that's already registered
    def test_register_book_already_registered(self):
        self.__reset_database()
        requests.post("%s/demo/registerbook" % URL, data = {'book': 'alice in wonderland'})
        r = requests.post("%s/demo/registerbook" % URL, data = {'book': 'alice in wonderland'})
        self.assertTrue("Result: ALREADY_REGISTERED_BOOK" in r.text)

    # test that we get the correct result if we try to register a borrower
    def test_register_borrower(self):
        self.__reset_database()
        r = requests.post("%s/demo/registerborrower" % URL, data = {'borrower': 'alice'})
        self.assertTrue("Result: SUCCESS" in r.text)

    # test that we get the correct result if we try to register a borrower that's already registered
    def test_register_borrower_already_registered(self):
        self.__reset_database()
        requests.post("%s/demo/registerborrower" % URL, data = {'borrower': 'alice'})
        r = requests.post("%s/demo/registerborrower" % URL, data = {'borrower': 'alice'})
        self.assertTrue("Result: ALREADY_REGISTERED_BORROWER" in r.text)

    # test that we can lend a book
    def test_create_book_loan(self):
        self.__reset_database()
        requests.post("%s/demo/registerborrower" % URL, data = {'borrower': 'alice'})
        requests.post("%s/demo/registerbook" % URL, data = {'book': 'alice in wonderland'})
        r = requests.post("%s/demo/lend" % URL, data = {'borrower': 'alice', 'book': 'alice in wonderland'})
        self.assertTrue("Result: SUCCESS" in r.text)

    # test that we get the correct result if we try to lend a book already lent.
    def test_create_book_loan_already_lent(self):
        self.__reset_database()
        requests.post("%s/demo/registerborrower" % URL, data = {'borrower': 'alice'})
        requests.post("%s/demo/registerbook" % URL, data = {'book': 'alice in wonderland'})
        requests.post("%s/demo/lend" % URL, data = {'borrower': 'alice', 'book': 'alice in wonderland'})
        r = requests.post("%s/demo/lend" % URL, data = {'borrower': 'alice', 'book': 'alice in wonderland'})
        self.assertTrue("Result: BOOK_CHECKED_OUT" in r.text)



if __name__ == '__main__':
    unittest.main()


