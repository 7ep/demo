Feature: A librarian has a user interface to lend a book

    As a librarian,
    I want a UI for lending a book
    so that it is easy to provide books to borrowers

    # This user story relates to behavior from a UI-centric point of view.
    # Note that we want as few UI tests as possible.  Logic should be tested lower down, this
    # is just to test the UI

    Scenario: A registered patron checks out a book
        Given a borrower is registered
        And a book is available for borrowing
        When they try to check out the book
        Then the system indicates success

