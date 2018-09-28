Feature: As a patron of the library system, I would like to check out a book so that I may take it outside the library.

  Narrative: A county library system has many books available for borrowing.  If you
  live in that county, and you have registered for a library card, you are able
  to borrow books.  The system shall provide the capability to lend out books.

  Scenario: A registered patron checks out a book
    Given a borrower, "alice", is registered
    And a book, "Specification By Example" is available for borrowing
    When they try to check out the book on "January 31, 2018"
    Then the system indicates the book is loaned to them on that date

  Scenario: A non-registered borrower should not be able to borrow a book
    Given an individual, "bob", is not registered
    When they try to check out a book, "Zen And The Art Of Motorcycle Maintenance" that is available
    Then the system indicates that they are not registered

  # The following is a poor scenario.
  # How would this have made it past discussion in a collaboration?
  Scenario: A registered borrower cannot borrow a non-available book
    Given a borrower, "alice", is registered
    And and a book, "Specification By Example" is already checked out to "bob"
    When they try to check out the book
    Then the system indicates that the book is not available


