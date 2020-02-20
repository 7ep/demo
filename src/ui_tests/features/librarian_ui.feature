Feature: A librarian may lend a book from the library

    As a librarian,
    I would like to lend a book
    so that patrons may enjoy it outside the library.

    # This user story relates to behavior from a UI-centric point of view.
    # Note that we want as few UI tests as possible.  Logic should be tested lower down, this
    # is just to test the UI

    Scenario: A registered patron checks out a book
        Given a borrower, "alice", is registered
        And a book, "Specification By Example" is available for borrowing
        When they try to check out the book
        Then the system indicates success

