Feature: Librarians may list and search books

    As a librarian,
    I want to be able to list and search books,
    So that I can review the inventory of my library

    Scenario: Can list all the books
        Given a library with the following books registered: a, b, c
        When a librarian lists all the registered books
        Then the whole list of books is returned

    Scenario: Can search a book by title
        Given a book, "The DevOps Handbook", is currently registered in the system
        When a librarian searches by that title
        Then the system returns the book's full data

    Scenario: Can search a book by id
        Given a book, "The DevOps Handbook", is currently registered in the system
        When a librarian searches by its id
        Then the system returns the book's full data

    Scenario: Should return an empty result if search by id finds nothing
        Given no books are registered in the system
        When a librarian searches for a book by id 1
        Then the system returns an empty result for the book

    Scenario: Should return an empty result if search by title finds nothing
        Given no books are registered in the system
        When a librarian searches for a book by title of "The DevOps Handbook"
        Then the system returns an empty result for the book

    Scenario: Receive an appropriate message if listing all books, but no books in library
        Given no books are registered in the system
        When a librarian lists all the registered books
        Then the system reports that there are no books in the system

    Scenario: Can obtain a list of available books
        Given some books are checked out
        When a librarian lists the available books
        Then the system responds with only the available books



