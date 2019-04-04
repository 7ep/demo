Feature: Librarians may list and search borrowers

    As a librarian,
    I want to be able to list and search borrowers,
    So that I can review the borrowers of my library

    Scenario: Can list all the borrowers
        Given a library with the following borrowers registered: "a", "b", "c"
        When a librarian lists all the registered borrowers
        Then that list is returned

    Scenario: Can search a borrower by name
        Given a borrower, "alice", is currently registered in the system
        When a librarian searches by that name
        Then the system returns its full data

    Scenario: Can search a borrower by id
        Given a borrower, "alice", with id of 1, is currently registered in the system
        When a librarian searches by that id
        Then the system returns its full data

    Scenario: Receives an appropriate message if search by id finds nothing
        Given no borrowers are registered in the system
        When a librarian searches for a borrower by id 1
        Then the system reports that there are no borrowers with that id

    Scenario: Receives an appropriate message if search by title finds nothing
        Given no borrowers are registered in the system
        When a librarian searches for a borrower by name of "alice"
        Then the system reports that there are no borrowers found with that name

    Scenario: Receive an appropriate message if listing all borrowers, but no borrowers in library
        Given no borrowers are registered in the system
        When a librarian lists all the borrowers
        Then the system reports that there are no borrowers in the system

