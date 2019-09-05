Feature: Librarians may add and delete books

    As a librarian,
    I want to be able to add and delete books,
    So that I have control over the inventory of my library

    Scenario: Can successfully add a book
        Given a book, "The DevOps Handbook", is not currently registered in the system
        When a librarian registers that book
        Then the system has the book registered

    Scenario: Can successfully remove a book
        Given a book, "The DevOps Handbook", is currently registered in the system
        When a librarian deletes that book
        Then the system does not have the book registered

    Scenario: Cannot add a book that already exists
        Given a book, "The DevOps Handbook", is currently registered in the system
        When a librarian registers that book
        Then the system reports an error indicating that the book is already registered

    Scenario: Cannot remove a book that doesn't exist
        Given a book, "The DevOps Handbook", is not currently registered in the system
        When a librarian deletes that book
        Then the system reports an error indicating that the book cannot be deleted because it was never registered

    Scenario: Can delete a book that is loaned out
        Given a book, "The DevOps Handbook", is currently loaned out
        When a librarian deletes that book
        Then the system does not have the book registered

    Scenario: If a book is deleted, then related loans are as well
        Given a book, "The DevOps Handbook", is loaned out
        When a librarian deletes that book
        Then the loan is deleted as well



