Feature: Librarians may add and delete borrowers

    As a librarian,
    I want to be able to add and delete borrowers,
    So that I have control over who is allowed to borrow from the library

    Scenario: Can successfully add a borrower
        Given a borrower, "alice", is not currently registered in the system
        When a librarian registers that borrower
        Then the system has the borrower registered

    Scenario: Can successfully remove a borrower
        Given a borrower, "alice", is currently registered in the system
        When a librarian deletes that borrower
        Then the system does not have the borrower registered

    Scenario: Cannot add a borrower that already exists
        Given a borrower, "alice", is currently registered in the system
        When a librarian registers that borrower
        Then the system reports an error indicating that the borrower is already registered

    Scenario: Cannot remove a borrower that doesn't exist
        Given a borrower, "alice", is not currently registered in the system
        When a librarian deletes that borrower
        Then the system reports an error indicating that the borrower cannot be deleted because he or she was never registered

    Scenario: Can delete a borrower that has a loan
        Given a book is currently loaned out to "alice"
        When a librarian deletes that borrower
        Then the system does not have the borrower registered

     Scenario: If a borrower is deleted, then their loan is too
         Given a book is loaned to "alice"
         When a librarian deletes that borrower
         Then the loan is deleted as well


