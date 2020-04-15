from behave import given, when, then
from hamcrest import *

URL = 'http://localhost:8080/demo/library.html'


@given('a borrower is registered')
def step_impl(context):
    borrower_name = "some borrower"
    __register_borrower(context, borrower_name)
    context.my_borrower_name = borrower_name

@given('a book is available for borrowing')
def step_impl(context):
    book_title = "some book"
    __register_book(context, book_title)
    context.my_book_title = book_title


@when('they try to check out the book')
def step_impl(context):
    __lend_book(context, context.my_borrower_name, context.my_book_title)


@then('the system indicates success')
def step_impl(context):
    result = context.driver.find_element_by_id('result')
    assert_that(result.text, contains_string('SUCCESS'))


def __register_borrower(context, borrower_name):
    driver = context.driver
    driver.get(URL)
    borrower = driver.find_element_by_id("register_borrower")
    borrower.clear()
    borrower.send_keys(borrower_name)
    submit_button = driver.find_element_by_id("register_borrower_submit")
    submit_button.click()


def __register_book(context, book_title):
    driver = context.driver
    driver.get(URL)
    book = driver.find_element_by_id("register_book")
    book.clear()
    book.send_keys(book_title)
    submit_button = driver.find_element_by_id("register_book_submit")
    submit_button.click()

def __lend_book(context, my_borrower_name, my_book_title):
    driver = context.driver
    driver.get(URL)
    book = driver.find_element_by_id("lend_book")
    book.send_keys(my_book_title)
    borrower = driver.find_element_by_id("lend_borrower")
    borrower.send_keys(my_borrower_name)
    submit_button = driver.find_element_by_id("lend_book_submit")
    submit_button.click()
