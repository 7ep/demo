Feature: Librarians may list and search borrowers

    As a librarian,
    I want to be able to list and search borrowers,
    So that I can review the borrowers of my library

    Scenario: Can list all the borrowers
        Given a library with the following borrowers registered: a, b, c
        When a librarian lists all the registered borrowers
        Then the whole list of borrowers is returned

    Scenario: Can search a borrower by name
        Given a borrower, "alice", is currently registered in the system
        When a librarian searches by that name
        Then the system returns the borrower's full data

    Scenario: Can search a borrower by id
        Given a borrower, "alice", is currently registered in the system
        When a librarian searches by that id
        Then the system returns the borrower's full data

    Scenario: Should return an empty result if search by id finds nothing
        Given no borrowers are registered in the system
        When a librarian searches for a borrower by id 1
        Then the system returns an empty result for the borrower

    Scenario: Should return an empty result if search by title finds nothing
        Given no borrowers are registered in the system
        When a librarian searches for a borrower by name of "alice"
        Then the system returns an empty result for the borrower

    Scenario: Receive an appropriate message if listing all borrowers, but no borrowers in library
        Given no borrowers are registered in the system
        When a librarian lists all the borrowers
        Then the system returns an empty list of borrowers

