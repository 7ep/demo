Feature: A librarian may lend a book from the library

    As a librarian,
    I would like to lend a book
    so that patrons may enjoy it outside the library.

#    Narrative: A county library system has many books available for borrowing.  If you
#    live in that county, and you have registered for a library card, you are able
#    to borrow books.  The system shall provide the capability to lend out books.
#    The librarians are in control of lending out books.

    Scenario: A registered patron checks out a book
        Given a borrower, "alice", is registered
        And a book, "Specification By Example" is available for borrowing
        When they try to check out the book on "January 31, 2018"
        Then the system indicates the book is loaned to them on that date

    Scenario: A non-registered person should not be able to borrow a book
        Given an individual, "bob", is not registered
        When they try to check out a book, "BDD in Action" that is available
        Then the system indicates that they are not registered

    Scenario: A registered borrower cannot borrow a non-available book
        Given a borrower, "alice", is registered
        And and a book, "Specification By Example" is already checked out to "bob"
        When they try to check out the book
        Then the system indicates that the book is not available

    Scenario: a borrower can borrow more than one book
        Given a borrower, "alice", has one book, "The DevOps Handbook", already borrowed
        When they borrow another book
        Then they have two books currently borrowed

    Scenario: a book can only be loaned to one person
        Given a borrower, "alice", has one book, "The DevOps Handbook", already borrowed
        When another borrower, "bob" tries to borrow that book
        Then they cannot borrow it because it is already checked out

