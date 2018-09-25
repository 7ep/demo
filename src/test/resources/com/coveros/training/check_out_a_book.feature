Feature: As a borrower of the library system, I would like to check out a book so that I may enjoy it.

  Narrative: A county library system has many books available for borrowing.  If you
  live in that county, and you have registered for a library card, you are able
  to borrow books.  The system shall provide the capability to lend out books.

  Scenario: A registered borrower checks out a book
    Given a borrower, "alice", is registered and a book, "Specification By Example" is available for borrowing
    When they check out the book
    Then the database indicates the book is loaned to them on that date

  Scenario: A non-registered borrower should not be able to borrow a book
    Given an individual, "bob", is not registered as a borrower
    When they try to check out a book, "Zen And The Art Of Motorcycle Maintenance" that is available
    Then the system indicates that they are not registered

  Scenario: A book was lent out a month ago
    Given today is "01/31/2018"
    And the book, "agile testing", was lent out to "alice" on "01/01/2018"
    When the database is checked for details on that book
    Then it indicates it is checked out to them on that date


