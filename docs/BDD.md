BDD / TDD exercise v1.0
=======================

Contents
--------

- [The feature file and glue code](#the-feature-file-and-glue-code)
- [Unit testing and test-driven development (TDD)](#unit-testing-and-test-driven-development-tdd)
- [Developing the database code using TDD](#developing-the-database-code-using-tdd)
- [Back to the business layer](#back-to-the-business-layer)
- [Cycling with the glue code](#cycling-with-the-glue-code)
- [The second scenario](#the-second-scenario)
- [Moving on to the API](#moving-on-to-the-api)
- [Developing the UI](#developing-the-ui)


Note: There is a video file showing a run-through of a slightly
earlier version of this file at BDD_video.mp4 in this same
directory - take a look!



The feature file and glue code
------------------------------

We want to add a feature, an ability to return a library book.  Here's 
how that could look.  A quick caveat: This is a simulation of what an 
entire sprint could entail.  Unlike what we'll see in this exercise, 
there are many points during the sprint where the team must make decisions 
and will inevitably run into walls.  Be aware I tried to remove such 
slowdowns in the name of efficient teaching.

First, the BDD feature file - the user story and scenarios.  The presumption 
is that the team had a 3 amigos meeting (someone from development, from 
testing, and from the business) and determined that the feature consisted 
of the following from conversation.

Copy and paste the following text into: 

    src/bdd_test/resources/library/return_a_book.feature

Note that with Intellij, there's no need to save your work as you proceed, 
it handles that for you automatically.

```gherkin
Feature: A librarian may return books that were borrowed

    As a librarian,
    I want to mark books as returned,
    so that they can be checked out again.
    
        Scenario: Happy Path - Able to make a returned book available for borrowing
            Given a borrower had checked out a book, "The Devops Handbook",
            When I enter that book as returned
            Then it is available to be borrowed
        
        Scenario: Negative case - Not able to return a non-borrowed book
            Given a book, "The Devops Handbook" is available for borrowing,
            When I enter that book as returned
            Then I am presented an error about it already being available
```


This is a good point to commit our work.  We'll control git from the terminal,
either in Intellij (the Terminal tab is at the bottom) or simply using a terminal
provided by the operating system at the correct directory (at the root of the
Demo directory)

run:

```bash
$ git status
```

it should show something like this:

```bash
Untracked files:
  (use "git add <file>..." to include in what will be committed)
        src/bdd_test/resources/library/return_a_book.feature
```

An important part of the development process is reviewing your work at each commit.  Intellij provides this capability fully.  There is a tab, "commit", that you can open and examine the files that have changed or been added since our last commit.  Double-clicking on files here will show a comparison view to see what has changed.  It is crucial when developing to take your time at this point and think hard about whether each change was most optimal.  I generally find many opportunities to improve things at this point when coding.

Presuming everything is fine, let's add all the files, like this:

```bash
$ git add .
```

And then we'll commit this work locally using this command:

```bash
$ git commit -m "New feature file - returning a book"
```

Now that's done, run the command, 

```bash
$ gradlew librarybdd
```
    
The build should succeed, but if you scroll back a bit, you will see 
some complaints from the program about missing step definitions. The 
messages start with this line:

>"You can implement missing steps with the snippets below:"

That's because we don't have any glue code (also known as "step definitions") 
for our feature file.  We're going to fix that soon, but first, open the 
following report file with a browser like Chrome:

    build/reports/bdd/library/index.html  

Look at the report generated.  It shows the pending feature at the bottom.

Let's create a new step definition file, in the following file (You can 
find this directory in Intellij and create the class by right-clicking 
on "library" and selecting New > Java Class, and entering a name of 
ReturnBookStepDefs):

    src/bdd_test/java/com/coveros/training/library/ReturnBookStepDefs.java

You will see a new file with content like this:

```java
package com.coveros.training.library;

public class ReturnBookStepDefs {
}
```


copy in all the pending steps that were suggested to us when we ran Cucumber (that is, when we ran the command "gradlew librarybdd"):

```java
@Given("a borrower had checked out a book, {string},")
public void a_borrower_had_checked_out_a_book(String string) {
    // Write code here that turns the phrase above into concrete actions
    throw new cucumber.api.PendingException();
    ...
    ...
    (and so on....)
```

You will need to copy the text from the command-line output, not from here.  Also, you will need to import some classes for everything to compile.  Intellij will actually recommend imports for the @Given, @When, and @Then text.  You can have it add the imports by putting your cursor on that text and pressing alt+enter, then selecting "import class". At the end you should get three imports at the top of the file (order doesn't matter):

```java
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class ReturnBookStepDefs {
```

Run the Cucumber program again:

```bash
gradlew librarybdd
```

It tells us that we have pending and skipped test steps, rather than undefined steps like we had before:

```bash
    ...........P--P--
    Pending scenarios:
    src/bdd_test/resources/com/coveros/training/return_a_book.feature:7 # Happy Path - Able to make a returned book available for borrowing
    src/bdd_test/resources/com/coveros/training/return_a_book.feature:12 # Negative case - Not able to return a non-borrowed book

    50 Scenarios (2 pending, 48 passed)
    152 Steps (4 skipped, 2 pending, 146 passed)
    0m4.971s

cucumber.api.PendingException: TODO: implement me
    at com.coveros.training.ReturnBookStepDefs.a_borrower_had_checked_out_a_book(ReturnBookStepDefs.java:12)
    at ?.a borrower had checked out a book, "The Devops Handbook", (src/bdd_test/resources/com/coveros/training/return_a_book.feature:8)
    ...
```

What we're seeing is that Cucumber has recognized that there are indeed 
step definitions (glue code), but they are throwing PendingException.

We already have some of the functionality pre-existing for certain steps.  

For example, we can work with a real database, H2, and we can check out a 
book. We can update our step-definitions file with that information. 

Here is the code to initialize an empty database.  Place this somewhere in 
the ReturnBookStepDefs class:

```java
/**
 * Set up the databases, clear them, initialize the Library Utility with them.
 */
private void initializeEmptyDatabaseAndUtility() {
    pl.cleanAndMigrateDatabase();
    libraryUtils = new LibraryUtils();
}   
```

Here is the code for the step to check out a book.  Replace the method, "a_borrower_had_checked_out_a_book" with this code:

```java
@Given("a borrower had checked out a book, {string},")
public void aBorrowerHadCheckedOutABook(String bookTitle) {
    initializeEmptyDatabaseAndUtility();
    libraryUtils.registerBook(bookTitle);
    myBook = libraryUtils.searchForBookByTitle(bookTitle);
    libraryUtils.registerBorrower(DEFAULT_BORROWER);
    myBorrower = libraryUtils.searchForBorrowerByName(DEFAULT_BORROWER);
    libraryUtils.lendBook(myBook, myBorrower, BORROW_DATE);
}
```

We need to add some class-level variables that are necessary.  These go at the top of the class:

```java
private static final Date BORROW_DATE = Date.valueOf(LocalDate.of(2018, Month.JANUARY, 1));
private static final Date RETURN_DATE = Date.valueOf(LocalDate.of(2018, Month.JANUARY, 2));
private static final String DEFAULT_BORROWER = "Alice";
private Book myBook = Book.createEmpty();
private Borrower myBorrower = Borrower.createEmpty();
private LibraryUtils libraryUtils = LibraryUtils.createEmpty();
private PersistenceLayer pl = new PersistenceLayer();
```

Like before, it will be necessary to import some classes for this to function properly.  Put the cursor on words colored red and press alt-enter.  The only tricky parts here are that Date need java.sql.Date imported and Book needs the import from Coveros.  At the end, here are the imports that should show at the top (order doesn't matter):

```java
import com.coveros.training.library.domainobjects.Book;
import com.coveros.training.library.domainobjects.Borrower;
import com.coveros.training.persistence.PersistenceLayer;
import java.sql.Date;
import java.time.LocalDate;
import java.time.Month;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
```

Now, at this point we have implemented one of the steps of the first scenario:


  Given a borrower had checked out a book, "The Devops Handbook",  


Run Cucumber:  

```bash
gradlew librarybdd
```

Review the report again.  Note that the step "Given a borrower had checked out..." 
is showing as green, indicating that the next step, "When I enter that book.." 
is next to do.

Let's commit our work: 

```bash
git add .
git commit -m "New Step Definitions file"
```

We can move to the next step, "When I enter that book as returned", which does 
indeed use code that won't exist yet.  Copy in this code carefully (just replace 
the inner parts):

```java
@When("I enter that book as returned")
public void i_enter_that_book_as_returned() {
    libraryUtils.returnBook(myBook, RETURN_DATE);
}
```


The method returnBook doesn't yet exist, which is why the IDE is highlighting 
it in red.  Put the mouse cursor on it and press alt+enter, and Intellij will
recommend creating the method.  Do so.  It will take you to the location it 
got added and will look like this:

```java
public void returnBook(Book myBook, Date returnDate) {
}
```



Unit testing and test-driven development (TDD)
-----------------------------------------------

Let's now create a unit test for that method:

Go to LibraryUtilsTests.java.  To do this, you can either press ctrl+n and 
type `LibraryUtilsTests`, or click on the menu bar and select `Navigate > Class`
and type in `LibraryUtilsTests`.  Once there, scroll down to the bottom of the 
file, but before the last curly brace, and add this test:

```java
  @Test
  public void testShouldReturnBook() {
    libraryUtils.returnBook(DEFAULT_BOOK, RETURN_DATE);
  }
```

Add RETURN_DATE at the top of the file by duplicating the line for BORROW_DATE 
and making it the next day, like this:

```java
private final static Date BORROW_DATE = Date.valueOf(LocalDate.of(2018, Month.JANUARY, 1));
private final static Date RETURN_DATE = Date.valueOf(LocalDate.of(2018, Month.JANUARY, 2));
```

Head back to the test at the bottom of the file.  What do we want this to return 
when we run it?  maybe it should return a LibraryActionResult, like some of the 
other methods.  Let's change it then:

```java
  @Test
  public void testShouldReturnBook() {
    LibraryActionResults result = libraryUtils.returnBook(DEFAULT_BOOK, RETURN_DATE);
    Assert.assertEquals(LibraryActionResults.BOOK_RETURNED, result);
  }
```

You will find this adds new errors highlighted by the IDE.  The problems you are 
seeing are:

1) there is no such enumeration BOOK_RETURNED. 
2) Our test assumes "returnBook" returns a LibraryActionResult, but currently 
   it returns void

To fix that:

1) Add a new entry to LibraryActionResults (navigate there by holding Control 
   and clicking on LibraryActionResults)

```java
BOOK_RETURNED, // successfully returned a book
```
     
2) In LibraryUtils, returnBook should return a LibraryActionResults:

```java
public LibraryActionResults returnBook(Book myBook, Date returnDate) {
    return LibraryActionResults.BOOK_RETURNED;
}
```

Now if you run the unit test, it should pass.  Run that by clicking the 
arrow to the left of the test in the IDE.

This is a good point to commit our work. 

```bash
git add .
git commit -m "initial stab at returnBook()"
```

A common mnemonic to remind us of good testing practices used with unit 
testing is right BICEP - standing for: 

* Make sure the calculation is _right_
* Boundaries
* Inverse
* Cross-check
* Errors
* Performance

Let's add a unit test that uses the inverse - a test that we should not return a 
book if it wasn't loaned.

```java
  @Test
  public void testShouldNotReturnBookNotLoaned() {
    LibraryActionResults result = libraryUtils.returnBook(DEFAULT_BOOK, RETURN_DATE);
    Assert.assertEquals(LibraryActionResults.BOOK_WAS_NOT_LOANED_OUT_WHEN_RETURNED, result);
  }
```

We'll need to add another enumeration to LibraryActionResults for this to compile:

```java
// someone tried returning a book that wasn't actually loaned out
BOOK_WAS_NOT_LOANED_OUT_WHEN_RETURNED, 
```

Now we can run the test... and it fails.

It is the nature of TDD to experiment with live code to fashion it the way we 
want.  Note that although our initial unit tests appear to do the same thing 
and expect different results, that is just the cyclical nature of it.  We're
playing with the code to see where we want it to end up!  We know that we have 
a business need to have at least these two results from the method, but we're 
not totally clear yet on how things should work - our view is blurry right now.

When we look at these two tests, a thought occurs to us; the state of the book 
will distinguish whether the result should be one thing or the other.  Right 
now, we are passing in DEFAULT_BOOK, but we want to clarify that here.  It 
should probably be something more like, BORROWED_BOOK and AVAILABLE_BOOK.  
Let's revise:

We will need to add class constants for these at the top of the class:

```java
// a book that has been borrowed
private final Book BORROWED_BOOK = BookTests.createTestBook();

// a book that is available for borrowing
private final Book AVAILABLE_BOOK = BookTests.createTestBook();
```

And here are our revised tests:

```java
  @Test
  public void testShouldReturnBook() {
    LibraryActionResults result = libraryUtils.returnBook(BORROWED_BOOK, RETURN_DATE);
    Assert.assertEquals(LibraryActionResults.BOOK_RETURNED, result);
  }

  @Test
  public void testShouldNotReturnBookNotLoaned() {
    LibraryActionResults result = libraryUtils.returnBook(AVAILABLE_BOOK, RETURN_DATE);
    Assert.assertEquals(LibraryActionResults.BOOK_WAS_NOT_LOANED_OUT_WHEN_RETURNED, result);
  }
```

Here we are using wishful thinking and more articulate code in a top-down approach to getting where we want to be.  How might we write the code at this level?  Here's a suggestion (note that when doing TDD for real, it goes in fits and starts, and there is plenty of thinking time.  For pedagogical reasons I am jumping through this far more quickly than could be reasonably done in actual practice)

```java
public LibraryActionResults returnBook(Book myBook, Date returnDate) {
    // get details on the book from the database
    // if the book is not loaned out,
        // return LibraryActionResults.BOOK_WAS_NOT_LOANED_OUT_WHEN_RETURNED;
    // else...
        // mark book as being returned
    return LibraryActionResults.BOOK_RETURNED;
}
```

This converts to code like this:

```java
public LibraryActionResults returnBook(Book myBook, Date returnDate) {
    Book book = persistence.searchBooksByTitle(myBook.title).get();
    if (!persistence.isBookLoanedOut(book.id).get()) {
        return LibraryActionResults.BOOK_WAS_NOT_LOANED_OUT_WHEN_RETURNED;
    }

    persistence.returnBook(book.id, returnDate);
    return LibraryActionResults.BOOK_RETURNED;
}
```

This implies two new methods in the persistence interface layer - isBookLoanedOut and returnBook.  You will need to create these in the interface, IPersistenceLayer:

```java
Optional<Boolean> isBookLoanedOut(long id);
void returnBook(long bookId, Date returnDate);
```

And you will need to add these to the implementation, PersistenceLayer, 
to satisfy the interface:

```java
@Override
public Optional<Boolean> isBookLoanedOut(long id) {
    return Optional.of(false);
}

@Override
public void returnBook(long bookId, Date returnDate) {

}
```

Now we'll modify our tests to look like this:

```java
    /**
     * When we successfully return a book, this method returns a result of BOOK_RETURNED
     */
@Test
public void testShouldReturnBook() {
    // arrange
    Mockito.when(mockPersistenceLayer.searchBooksByTitle(BORROWED_BOOK.title)).thenReturn(Optional.of(BORROWED_BOOK));
    // book is loaned out
    Mockito.when(mockPersistenceLayer.isBookLoanedOut(BORROWED_BOOK.id)).thenReturn(Optional.of(true));
    // running returnBook on the mockPersistenceLayer will do nothing

    // act
    LibraryActionResults result = libraryUtils.returnBook(BORROWED_BOOK, RETURN_DATE);

    // assert
    Assert.assertEquals(LibraryActionResults.BOOK_RETURNED, result);
}

/**
 * If we tried returning a book that wasn't actually loaned out, we should
 * get BOOK_WAS_NOT_LOANED_OUT_WHEN_RETURNED
 */
@Test
public void testShouldNotReturnBookNotLoaned() {
    // arrange
    Mockito.when(mockPersistenceLayer.searchBooksByTitle(AVAILABLE_BOOK.title)).thenReturn(Optional.of(AVAILABLE_BOOK));
    // book is not loaned out
    Mockito.when(mockPersistenceLayer.isBookLoanedOut(AVAILABLE_BOOK.id)).thenReturn(Optional.of(false));
    // running returnBook on the mockPersistenceLayer will do nothing

    // act
    LibraryActionResults result = libraryUtils.returnBook(BORROWED_BOOK, RETURN_DATE);

    // assert
    Assert.assertEquals(LibraryActionResults.BOOK_WAS_NOT_LOANED_OUT_WHEN_RETURNED, result);
}
```

Another interesting question - what if the book parameter is null or if nothing 
is found when we search for the book by title?  Just going a little further, 
we can add another test...

```java
/**
 * If we tried returning a book that wasn't registered, it should return BOOK_NOT_REGISTERED
 */
@Test
public void test_returnBook_noBookFound() {
    // arrange
    Mockito.when(
            mockPersistenceLayer.searchBooksByTitle(DEFAULT_BOOK.title)).thenReturn(Optional.empty());

    // act
    LibraryActionResults result = libraryUtils.returnBook(BORROWED_BOOK, RETURN_DATE);

    // assert
    Assert.assertEquals(LibraryActionResults.BOOK_NOT_REGISTERED, result);
}
```

Note that when we run this test, it fails, leading us in the correct direction 
to fix the code...  The code changes to this:

```java
public LibraryActionResults returnBook(Book myBook, Date returnDate) {
    Optional<Book> book = persistence.searchBooksByTitle(myBook.title);
    if (book.isEmpty()) {
        return LibraryActionResults.BOOK_NOT_REGISTERED;
    }
    if (!persistence.isBookLoanedOut(book.get().id).get()) {
        return LibraryActionResults.BOOK_WAS_NOT_LOANED_OUT_WHEN_RETURNED;
    }

    persistence.returnBook(book.get().id, returnDate);
    return LibraryActionResults.BOOK_RETURNED;
}
```

If we are being very careful developers, we should document our tests a bit 
to describe reasons behind what we're doing, as seen above.  We might also go
a little further in our tests...

```java
/**
 * What happens if the book being passed in is empty?
 * We could probably avoid the call to the database...
 */
@Test
public void test_returnBook_emptyBookObject() {    }

/**
 * What happens if the date object is empty or null?
 * Probably can immediately return
 */
@Test
public void test_returnBook_emptyDateObject() {    }

/**
 * What happens if the date we're returning the book happens before the date we borrowed it?
 * maybe a date glitch that would be caught.  This leads the way towards contract-based testing.
 */
@Test
public void test_returnBook_returnBeforeBorrow() {    }
```

What should go in those tests?  Should there be more? fewer? different?  Now 
this business layer is solid for now, we can start moving towards TDD on the 
database level, the persistence level.



Developing the database code using TDD
--------------------------------------

An experienced developer might write something like the following.  Add 
this at the end of the file in PersistenceLayerTests.java:

```java
@Test
public void testShouldReturnBookThatWasLoanedOut() {
    // set up a clean database for testing
    // lend out a book

    // return the book
    
    // assert that the book is returned
}
```

This converts to code like the following:

```java
@Test
public void testShouldReturnBookThatWasLoanedOut() {
    pl.cleanAndMigrateDatabase();
    final long bookId = pl.saveNewBook(DEFAULT_BOOK.title);
    final Book book = new Book(bookId, DEFAULT_BOOK.title);
    final long borrowerId = pl.saveNewBorrower(DEFAULT_BORROWER.name);
    final Borrower borrower = new Borrower(borrowerId, DEFAULT_BORROWER.name);
    pl.createLoan(book, borrower, BORROW_DATE);

    pl.returnBook(bookId, RETURN_DATE);

    boolean isAvailable = pl.isBookAvailable(DEFAULT_BOOK.id);
    assertTrue(isAvailable);
}
```

Duplicate the `BORROW_DATE` at the top of the file and name it `RETURN_DATE`.  Make 
it the day after (Make it Jan 2)

We are writing an integration test here - hitting the real database, so we're 
not mocking anymore, we're arranging state beforehand and testing that things 
are as expected after our action of returnBook.

Also, because our returnBook does just one thing - return a book - there 
isn't anything to indicate to us that it's considered returned.  For that 
purpose, we will use wishful thinking for another not-yet-created method, 
`isBookAvailable`.

This doesn't currently compile.  Let's use the IDE to generate these new methods.  Here is the change to the interface, IPersistenceLayer:

```java
boolean isBookAvailable(long id);
```

And here is the change to `PersistenceLayer`:

```java
@Override
public boolean isBookAvailable(long id) {
    return false;
}
```


  **** Slow down!  Bumpy road ahead! **** 


We're about to hit an inflection point in our process, so we want to reflect 
on what we've done to now, and what we're about to do.

The new methods for this persistence code that we've gotten from the IDE are 
empty, consisting solely of a method name, input parameters, and return type.  
In our previous TDD work, we've been able to gradually build out the 
code-under-test through the magic of expanded tests.  However, when we get 
into database work, some of that goes out the window because we must be able 
to comfortably manipulate the database.  This is where we're going off-road a bit.

This isn't ordinary unit testing - it's integration testing.  We're going to 
allow our tests to go straight into the real database, without mocks.  In order 
to do so, we need to play around a bit inside the database so we can correctly 
imagine the kind of SQL code we want to run.  Let's do that now.

First, start up the application server:

```bash
gradlew apprun
```

go to `http://localhost:8080/demo/console`

For the URL: 

    jdbc:h2:mem:training;MODE=PostgreSQL;DB_CLOSE_DELAY=-1

use an empty username and an empty password, and click Connect.

At the console, we want to understand our context and get a sense of what we 
want to change.  we can run some commands to look at the loan table. 

```sql
select * from library.loan;
-- ID    BOOK    BORROWER    BORROW_DATE  
-- (no rows, 8 ms)
```

Our design is that if a loan has a returned date, then the book is not 
loaned out anymore and is available for borrowing again.

We'll need a new RETURN_DATE column.  Let's go ahead and add that now:

```sql
ALTER TABLE library.loan ADD COLUMN RETURN_DATE date;
-- Update count: 0
-- (30 ms)
  
select * from library.loan;
-- ID    BOOK    BORROWER    BORROW_DATE    RETURN_DATE  
-- (no rows, 5 ms)  
```

We have the new column!  

There is a ramification to existing functionality with this new design.  Before, 
when we were looking for existing loans, we just searched the loan table.  Now 
though, a book is only marked as loaned-out if we can find it in the loans 
table _and_ it has no return date. Therefore, SQL to search a current loan would 
look like this:

```sql
-- For finding an existing loan (not returned)
SELECT * from library.loan l
JOIN library.book b ON b.id = l.book
WHERE l.RETURN_DATE IS NULL
AND b.title = 'a book';
```

Let's test that out by lending out a book.  This can be easily done in the 
UI (go to `http://localhost:8080/demo/library.html )

Add a book, `"a book"`, and add a borrower `"alice"`, and loan the book.
  
By running the query with that book, we see that it is finding the loan.

```sql
SELECT * from library.loan l
        JOIN library.book b ON b.id = l.book
        WHERE l.RETURN_DATE IS NULL
        AND b.title = 'a book';

ID      BOOK     BORROWER  BORROW_DATE   RETURN_DATE  ID  TITLE  
1       1        1	       2020-09-08    null         1   a book
```

If we add a returned date, we'll stop finding the loan.  This is the essence of 
the new functionality we're adding - the ability to add a returned date.  Here 
is the SQL:

```sql
-- For returning a book
UPDATE library.loan l 
SET return_date = '2019-02-16' 
WHERE l.book = 
     (
   SELECT b.id 
   FROM library.book b 
   JOIN library.loan l ON b.id = l.book 
   WHERE b.title = 'a book'
   );
```

We run our previous query (for finding existing loans) again and it finds 
nothing.  Success!  We can use this SQL to determine the count of existing 
non-returned loans for a book.  If it's 0, then the book is available.  If 
it's 1, it's checked out.  If it's more than 1, there's an error (it shouldn't 
be possible to find more than a count of one for a particular book loaned out).

```sql
-- Count of existing loans on a book
SELECT COUNT(*) from library.loan l
JOIN library.book b ON b.id = l.book
WHERE l.RETURN_DATE IS NULL
AND b.title = 'a book';
```

To summarize, we've played around with some stuff we're going to need

1) We need to alter our loan table to have return_date
2) we need a way to update our loan with the return date when a book gets returned.
3) We need a way to tell if a book is available (by counting the number of loans of that book that don't have return_date set).

Which translates to:

1) we must add a new Flyway schema migration script
2) We need to add two methods in the persistence layer for doing 2 and 3 above.

Let's start by adding the methods.  They would look something like this:

```java
@Override
public void returnBook(long id, Date returnDate) {
    CheckUtils.IntParameterMustBePositive(id);
    executeUpdateTemplate(
            "Updates the loan so that it has a return date, which clarifies that it is available for borrowing",
            "UPDATE library.loan l " +
                    "SET return_date = ? " +
                    "WHERE l.book = ?;", returnDate, id);
}


@Override
public Optional<Boolean> isBookAvailable(long id) {
    CheckUtils.IntParameterMustBePositive(id);
    Function<ResultSet, Optional<Boolean>> extractor = throwingFunctionWrapper((rs) -> {
        if (rs.next()) {
            final long countOfLoans = rs.getLong(1);
            if (countOfLoans == 0) {
                return Optional.of(true);
            } else if (countOfLoans == 1) {
                return Optional.of(false);
            } else {
                throw new SqlRuntimeException(
                        "More than one loan for a book was found - this should be impossible");
            }
        } else {
            return Optional.empty();
        }
    });

    return runQuery(new SqlData<>(
            "determine if a book is available for checkout by its id",
            "SELECT COUNT(*) from library.loan l " +
                    "WHERE l.book = ? AND l.RETURN_DATE IS NULL",
            extractor, id));
}
```


Note that we learned here for "isBookAvailable" it was necessary to provide an Optional of Boolean.  The reason for this is because our tool for communicating with the database requires us to always wrap our results in an Optional.  In this framework, an empty Optional will be returned if we get nothing from our SQL query.  That shouldn't happen in valid cases - it should only be 0 (no loans found) or 1 (one loan of the book).  We have to update the interface like this:

```java
Optional<Boolean> isBookAvailable(long id);
```

and the test like this:

```java
@Test
public void testShouldReturnBookThatWasLoanedOut() {
    pl.cleanAndMigrateDatabase();
    final long bookId = pl.saveNewBook(DEFAULT_BOOK.title);
    final Book book = new Book(bookId, DEFAULT_BOOK.title);
    final long borrowerId = pl.saveNewBorrower(DEFAULT_BORROWER.name);
    final Borrower borrower = new Borrower(borrowerId, DEFAULT_BORROWER.name);
    pl.createLoan(book, borrower, BORROW_DATE);

    pl.returnBook(bookId, RETURN_DATE);

    boolean isAvailable = pl.isBookAvailable(DEFAULT_BOOK.id).get();
    assertTrue(isAvailable);
}
```

The migration script would look like this:

src/main/resources/db/migration/V3__Add_Return_date_to_loan.sql

```sql
ALTER TABLE library.loan ADD COLUMN RETURN_DATE date;
```

Running the persistence test now works!

This would be a good point to commit.  

```sql
git add .
git commit -m "persistence TDD underway"
```

A quick word about TDD and other beneficial development strategies - don't 
be dogmatic.  Use them as much as you can to be disciplined about your 
work, because they help you attain high quality results.  Don't 
slavishly adhere to practices that are inordinately painful or slow.  In 
the case of developing this database code, we're using TDD, but the pacing and 
some techniques are a bit different because of the nature of the thing we're building.

The persistence code we developed seemed to arrive fully formed out of thin air, but that's not exactly so.  In fact, we are basing this code very much on similar code in that same file.  We are also using some development patterns that have been in use on this project a while.  Inevitably when you begin work on a project, these kinds of patterns are part of its ecosystem.

In any case, what I'm trying to convey is that we developed this code a bit differently than the previous one, in the following ways:
   
1) The previous code used mocks, but this code calls to other classes
2) The previous code is slowly built up, but this one seemed to arrive fully formed
3) We needed to play in the SQL console to better understand how the database would behave

Even so, we are still adhering, as much as possible, to the tenets of test-driven 
development, and will continue to do so, as you will see.  

This might also be a good time to mention _simplicity_, and doing the simplest, 
least possible thing every step.  The reasoning behind this is as follows:  
We want to take baby steps into design ideas.  By choosing the absolute 
simplest, easiest step, we can learn from it and then later decide the 
right path forward.  This stands in stark contrast to bigger planning 
processes.  By doing it this way, we are intentionally deciding to move 
very slowly so we can listen to the consequences of our choices when it's 
still easy to change.  If we move ahead too quickly, we cannot "hear" 
this.  It takes practice and discipline to slow yourself down this much, 
but the effects on quality are tremendous.

Moving right along... 

Let's try testing an edge case related to "isBookAvailable".  If we have a totally fresh database - no books in it - and check whether a particular book is available to loan, it *should* say "false" - that the book is not available.  Put this test in the PersistenceLayerTests (it will be necessary to import "assertFalse" statically:

```java
@Test
public void testShouldNotBeAvailableIfBookNotRegistered() {
    pl.cleanAndMigrateDatabase();

    boolean isAvailable = pl.isBookAvailable(DEFAULT_BOOK.id).get();

    assertFalse(isAvailable);
}
```

This test will fail, which leads us to the realization that our SQL code 
_doesn't care_ if the book doesn't even exist.  That's not exactly correct, 
and in programming we need as much _exactly correct_ as we can.  We need to 
change our SQL code to care about that also.  The best way to do this is 
to play around in the database console with some SQL.  In the console, 
paste this SQL, which is the current SQL script from isBookAvailable:

```sql
SELECT COUNT(*) from library.loan l 
WHERE l.book = 1234 AND l.RETURN_DATE IS NULL
```

When we run that, it doesn't fail.  It simply indicates a count of 0.  This 
isn't what we expected.  We asked for the count of books with an id of 
1234 (which doesn't exist) and a null return date - and it gave us 0, 
meaning it is available.  We need to alter our expectations a bit. 

A common adage in this field is that one of the two hard problems in 
computer science is naming things.

We need to rename our method.  It doesn't really tell us if a book is 
available - it only tells us that the book isn't actively loaned out. 

The persistence layer is kind of a simpleton - it doesn't have a lot of 
smarts, it just checks the data for what we ask.  Anything related to 
smarts will be done in the layer above.  

Let's rename isBookAvailable to isBookLoanedOut, with a disclaimer in 
its documentation that it needs further validation to ensure that the 
book is actually registered.  Also, we'll invert the result - now it
will return true if the count of loans is 1, and false if 0:

This is also good practice for using the IDE's refactoring tools.  It is 
possible to rename this here and have the IDE go throughout your codebase 
updating the name properly.  In Intellij, right-click on isBookAvailable,
go to refactoring, and rename.  Rename the base method.  Rename it "isBookLoanedOut".  

```java
/**
 * This is a simple method - it only checks if the id given
 * is found on a loan having no return date.  It may well
 * be that the book isn't even registered!  You need to check
 * that separately.
 * @param id the id of the book we want to check.
 * @return Optional of true if one outstanding loan was found, false
 *         if no outstanding loans, an error will be thrown
 *         if there are multiple outstanding loans.  Optional.empty
 *         if we fail to get a valid result from the database.
 */
@Override
public Optional<Boolean> isBookLoanedOut(long id) {
    CheckUtils.IntParameterMustBePositive(id);
    Function<ResultSet, Optional<Boolean>> extractor = throwingFunctionWrapper((rs) -> {
        if (rs.next()) {
            final long countOfLoans = rs.getLong(1);
            if (countOfLoans == 0) {
                return Optional.of(false);
            } else if (countOfLoans == 1) {
                return Optional.of(true);
            } else {
                throw new SqlRuntimeException(
                        "More than one loan for a book was found - this should be impossible");
            }
        } else {
            return Optional.empty();
        }
    });

    return runQuery(new SqlData<>(
            "determine if a book is available for checkout by its id",
            "SELECT COUNT(*) from library.loan l " +
                    "WHERE l.book = ? AND l.RETURN_DATE IS NULL",
            extractor, id));
}
```

This will cause us to change our tests in PersistenceLayerTests:

```java
@Test
public void testShouldReturnBookThatWasLoanedOut() {
    pl.cleanAndMigrateDatabase();
    final long bookId = pl.saveNewBook(DEFAULT_BOOK.title);
    final Book book = new Book(bookId, DEFAULT_BOOK.title);
    final long borrowerId = pl.saveNewBorrower(DEFAULT_BORROWER.name);
    final Borrower borrower = new Borrower(borrowerId, DEFAULT_BORROWER.name);
    pl.createLoan(book, borrower, BORROW_DATE);

    pl.returnBook(bookId, RETURN_DATE);

    boolean isBookLoanedOut = pl.isBookLoanedOut(DEFAULT_BOOK.id).get();
    assertFalse(isBookLoanedOut);
}

@Test
public void testShouldNotBeLoanedOutIfBookNotRegistered() {
    pl.cleanAndMigrateDatabase();

    boolean isBookLoanedOut = pl.isBookLoanedOut(DEFAULT_BOOK.id).get();

    assertFalse(isBookLoanedOut);
}
```

Let's add two more tests (do these make sense to you?):

```java
@Test
public void testShouldNotBeLoanedOutIfBookRegisteredButNotLoaned() {
    pl.cleanAndMigrateDatabase();
    final long bookId = pl.saveNewBook(DEFAULT_BOOK.title);

    boolean isBookLoanedOut = pl.isBookLoanedOut(bookId).get();

    assertFalse(isBookLoanedOut);
}

@Test
public void testShouldBeLoanedOutIfBookRegisteredAndLoaned() {
    pl.cleanAndMigrateDatabase();
    final long bookId = pl.saveNewBook(DEFAULT_BOOK.title);
    final Book book = new Book(bookId, DEFAULT_BOOK.title);
    final long borrowerId = pl.saveNewBorrower(DEFAULT_BORROWER.name);
    final Borrower borrower = new Borrower(borrowerId, DEFAULT_BORROWER.name);
    pl.createLoan(book, borrower, BORROW_DATE);

    boolean isBookLoanedOut = pl.isBookLoanedOut(DEFAULT_BOOK.id).get();

    assertTrue(isBookLoanedOut);
}
```

These should all pass.  Let's commit our work:

```bash
git add .
git commit -m "isBookLoanedOut more precisely handling our scenario"
```

Let's make a small improvement to maintainability - add some comments about 
the purpose of these tests above each method.  For example, you might put 
something above testShouldReturnBookThatWasLoanedOut like "This is a basic 
happy-path test to ensure that when we return a previously-loaned book, it 
is marked as available for loaning again".  A second kind of documentation 
can go in the assertion - you can add a message parameter that will print 
if the assertion fails, making it crystal clear to future maintainers (who 
might be you) what exactly went wrong.  It also acts as second form of 
documentation about the assertion when reading the code.  
 
With testShouldReturnBookThatWasLoanedOut, you might add a comment in the
following way:

```java
assertFalse("The book should have been considered loaned out by the system", isBookLoanedOut);
```

Spend 5 minutes going through the tests here, adding some documentation like we've described.
Time for another commit!  

```bash
git add .
git commit -m "improving clarity of some tests"
```



Back to the business layer
--------------------------

Now that's done, we step up a layer into the unit tests at LibraryUtilsTests.  If 
we run all our tests again, we see that testShouldNotReturnBookNotLoaned is not 
passing - because it's calling a method that does nothing except return a 
hard-coded value.  Let's change that now that we know what's happening at 
the lower level.

The implementation at LibraryUtils.returnBook should be more like this:

```java
/**
  * Return a loaned book
  */
public LibraryActionResults returnBook(Book myBook, Date returnDate) {
    Optional<Book> book = persistence.searchBooksByTitle(myBook.title);
    if (book.isEmpty()) {
        return LibraryActionResults.BOOK_NOT_REGISTERED;
    }
    if (!persistence.isBookLoanedOut(book.get())) {
        return LibraryActionResults.BOOK_WAS_NOT_LOANED_OUT_WHEN_RETURNED;
    }

    persistence.returnBook(book.get().id, returnDate);
    return LibraryActionResults.BOOK_RETURNED;
}
```

We'll need to mock out the persistence layer when writing our tests.  Our tests change to this:

```java
/**
 * When we successfully return a book, this method returns a result of BOOK_RETURNED
 */
@Test
public void testShouldReturnBook() {
    // arrange
    Mockito.when(mockPersistenceLayer.searchBooksByTitle(BORROWED_BOOK.title)).thenReturn(Optional.of(BORROWED_BOOK));
    // book is loaned out
    Mockito.when(mockPersistenceLayer.isBookLoanedOut(BORROWED_BOOK)).thenReturn(true);
    // running returnBook on the mockPersistenceLayer will do nothing

    // act
    LibraryActionResults result = libraryUtils.returnBook(BORROWED_BOOK, RETURN_DATE);

    // assert
    Assert.assertEquals(LibraryActionResults.BOOK_RETURNED, result);
}

/**
 * If we tried returning a book that wasn't actually loaned out, we should
 * get BOOK_WAS_NOT_LOANED_OUT_WHEN_RETURNED
 */
@Test
public void testShouldNotReturnBookNotLoaned() {
    // arrange
    Mockito.when(mockPersistenceLayer.searchBooksByTitle(AVAILABLE_BOOK.title)).thenReturn(Optional.of(AVAILABLE_BOOK));
    // book is not loaned out
    Mockito.when(mockPersistenceLayer.isBookLoanedOut(AVAILABLE_BOOK)).thenReturn(false);
    // running returnBook on the mockPersistenceLayer will do nothing

    // act
    LibraryActionResults result = libraryUtils.returnBook(BORROWED_BOOK, RETURN_DATE);

    // assert
    Assert.assertEquals(LibraryActionResults.BOOK_WAS_NOT_LOANED_OUT_WHEN_RETURNED, result);
}
```


These should run and pass.  Time for another commit! 

```bash
git commit -m "developing persistence details of returnBook, improving unit tests"
```

Let's add another test.  What happens if the book isn't registered?

```java
/**
 * If we tried returning a book that wasn't registered, it should return BOOK_NOT_REGISTERED
 */
@Test
public void test_returnBook_noBookFound() {
    // arrange
    Mockito.when(
            mockPersistenceLayer.searchBooksByTitle(DEFAULT_BOOK.title)).thenReturn(Optional.empty());

    // act
    LibraryActionResults result = libraryUtils.returnBook(BORROWED_BOOK, RETURN_DATE);

    // assert
    Assert.assertEquals(LibraryActionResults.BOOK_NOT_REGISTERED, result);
}
```

Great, it passes!

Time for another commit!  

```bash
git commit -m "Adding unit test for when book isn't registered"
```

Let's spend another five minutes documenting our tests, like before.  Then:

```bash
git commit -m "adding more documentation on tests"
```


Cycling with the glue code
--------------------------

Let's go up a level to the BDD tests in return_a_book.feature.  

The last step of the first scenario isn't done - "it is available to be borrowed".  That suggests having a method in libraryUtils called checkBookStatus that tells us the current status for a book.  I add the call in the glue code:

```java
@Then("it is available to be borrowed")
public void it_is_available_to_be_borrowed() {
    Assert.assertEquals(LibraryActionResults.AVAILABLE_FOR_BORROWING, libraryUtils.checkBookStatus(myBook));
}
```

Then, back to LibraryUtilsTests to do TDD:

```java
@Test
public void testShouldProvideAvailabilityOnBook() {
    Assert.assertEquals(LibraryActionResults.AVAILABLE_FOR_BORROWING, 
        libraryUtils.checkBookStatus(DEFAULT_BOOK));
}
```

And then we use the unit test to drive creation of the method in LibraryUtils.  
This code isn't compiling, so we can use the IDE to autogenerate some of this for us.  We need to add AVAILABLE_FOR_BORROWING as an enumeration, and we need to create a new method, checkBookStatus

LibraryActionResults.java:

```java
...
...
// a book is available to borrow
AVAILABLE_FOR_BORROWING,
...
...
```

libraryUtils:

```java
/**
 * Book can be in one of three states: not registered, loaned out, or not loaned out.
 */
public LibraryActionResults checkBookStatus(Book book) {
    if (persistence.searchBooksByTitle(book.title).isEmpty()) {
        return LibraryActionResults.BOOK_NOT_REGISTERED;
    }

    if (!persistence.isBookLoanedOut(book.id)) {
        return LibraryActionResults.AVAILABLE_FOR_BORROWING;
    } else {
        return LibraryActionResults.BOOK_CHECKED_OUT;
    }
}
```

A quick note - this looks very similar to the code for returnBook.  We 
might consider having some refactoring to reduce duplication.  But moving on...

Back up to the unit tests, where we modify and create new tests based on 
our new understanding.  Delete testShouldProvideAvailabilityOnBook and 
add the following:

```java
 /**
 * If a book is registered but not loaned out, return AVAILABLE_FOR_BORROWING
 */
@Test
public void testShouldIndicateAvailable() {
    Mockito.when(mockPersistenceLayer.searchBooksByTitle(DEFAULT_BOOK.title)).thenReturn(Optional.of(DEFAULT_BOOK));
    Mockito.when(mockPersistenceLayer.isBookLoanedOut(DEFAULT_BOOK.id)).thenReturn(Optional.of(false));

    final LibraryActionResults result = libraryUtils.checkBookStatus(DEFAULT_BOOK);

    Assert.assertEquals(LibraryActionResults.AVAILABLE_FOR_BORROWING, result);
}

/**
 * If a book is unregistered, return BOOK_NOT_REGISTERED
 */
@Test
public void testShouldIndicateUnregistered() {
    Mockito.when(
            mockPersistenceLayer.searchBooksByTitle(DEFAULT_BOOK.title)).thenReturn(Optional.empty());

    final LibraryActionResults result = libraryUtils.checkBookStatus(DEFAULT_BOOK);

    Assert.assertEquals(LibraryActionResults.BOOK_NOT_REGISTERED, result);
}

/**
 * If a book is registered and loaned out, return BOOK_CHECKED_OUT
 */
@Test
public void testShouldIndicateCheckedOut() {
    Mockito.when(mockPersistenceLayer.searchBooksByTitle(DEFAULT_BOOK.title)).thenReturn(Optional.of(DEFAULT_BOOK));
    Mockito.when(mockPersistenceLayer.isBookLoanedOut(DEFAULT_BOOK.id)).thenReturn(Optional.of(true));

    final LibraryActionResults result = libraryUtils.checkBookStatus(DEFAULT_BOOK);

    Assert.assertEquals(LibraryActionResults.BOOK_CHECKED_OUT, result);
}
```

Run it... and it passes.  Now we move up to the BDD test and run them 
again.. and the first scenario passes.

Time for another commit! 

```bash
git add .
git commit -m "new method: checkBookStatus, first scenario now passing"
```



The second scenario
--------------------

Now the first scenario is done.  Most of our work is done - let's take a 
look at what the second scenario entails.  Following is the filled-out 
code for the remainder of the glue code:

```java
package com.coveros.training.library;

import com.coveros.training.library.domainobjects.Book;
import com.coveros.training.library.domainobjects.Borrower;
import com.coveros.training.library.domainobjects.LibraryActionResults;
import com.coveros.training.persistence.PersistenceLayer;
import java.sql.Date;
import java.time.LocalDate;
import java.time.Month;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import static org.junit.Assert.assertEquals;


public class ReturnBookStepDefs {

    private static final Date BORROW_DATE = Date.valueOf(LocalDate.of(2018, Month.JANUARY, 1));
    private static final Date RETURN_DATE = Date.valueOf(LocalDate.of(2018, Month.JANUARY, 2));
    private static final String DEFAULT_BORROWER = "Alice";
    private Book myBook = Book.createEmpty();
    private Borrower myBorrower = Borrower.createEmpty();
    private LibraryUtils libraryUtils = LibraryUtils.createEmpty();
    private PersistenceLayer pl = new PersistenceLayer();
    private LibraryActionResults result = LibraryActionResults.NULL;

    /**
     * Set up the databases, clear them, initialize the Library Utility with them.
     */
    private void initializeEmptyDatabaseAndUtility() {
        pl.cleanAndMigrateDatabase();
        libraryUtils = new LibraryUtils();
    }

    @Given("a borrower had checked out a book, {string},")
    public void aBorrowerHadCheckedOutABook(String bookTitle) {
        initializeEmptyDatabaseAndUtility();
        libraryUtils.registerBook(bookTitle);
        myBook = libraryUtils.searchForBookByTitle(bookTitle);
        libraryUtils.registerBorrower(DEFAULT_BORROWER);
        myBorrower = libraryUtils.searchForBorrowerByName(DEFAULT_BORROWER);
        libraryUtils.lendBook(myBook, myBorrower, BORROW_DATE);
    }

    @When("I enter that book as returned")
    public void i_enter_that_book_as_returned() {
        result = libraryUtils.returnBook(myBook, RETURN_DATE);
    }

    @Then("it is available to be borrowed")
    public void it_is_available_to_be_borrowed() {
        assertEquals(LibraryActionResults.AVAILABLE_FOR_BORROWING, libraryUtils.checkBookStatus(myBook));
    }

    @Given("a book, {string} is available for borrowing,")
    public void a_book_is_available_for_borrowing(String title) {
        initializeEmptyDatabaseAndUtility();
        libraryUtils.registerBook(title);
        myBook = libraryUtils.searchForBookByTitle(title);
    }

    @Then("I am presented an error about it already being available")
    public void i_am_presented_an_error_about_it_already_being_available() {
        assertEquals(LibraryActionResults.BOOK_WAS_NOT_LOANED_OUT_WHEN_RETURNED, result);
    }

}
```

Time for another commit! 

```bash
git add .
git commit -m  "all BDD scenarios passing"
```

Running the whole feature file should pass, meaning that our new feature 
is substantially done.  

Let's assess where we are at.  We've developed the core functionality of a 
new feature.  Sure, there's no way to interact with it, yet.  But it exists, 
and it's tested thoroughly.  This follows the following principles from the 
agile manifesto: 

>Working software is the primary measure of progress.
>Continuous attention to technical excellence and good design enhances agility.
>Simplicity--the art of maximizing the amount of work not done--is essential.

and one of the principles:

>Our highest priority is to satisfy the customer through early and continuous delivery of valuable software.

by providing a high test coverage and quality, so that we can get our most recent work to the customer as fast as possible while confident that it does what the customer wanted and was built well.



Moving on to the API
--------------------

Form follows function - let's move into tests for the API and the UI. 
 
On a related note: the API and UI tests are written in Python, but this was 
arbitrary choice made for certain reasons that made sense for this team.  It's 
likelier that for your own team you should come to a consensus about which 
tools you wish to use.  

A benefit of using a tool like Python is that it gives you the ability to run 
one line of code at a time to incrementally build up your scripts.  It also 
enables you to debug later by running one line at a time and immediately see 
the result.  However, it does then require that the team must be familiar
with yet another programming language, so there's a good reason against doing 
it this way.

Nonetheless, for the purpose of this exercise we'll be doing it this way.

For the following sections on API and UI, I have to admit, there's a strong 
pressure to be less disciplined with the TDD approach and jump ahead.  For
example, there will usually be similar parts (as exists in Demo), which we 
can just copy and paste, thereby skipping past the (admittedly sometimes 
tedious) baby steps forward we've shown up until now.  I'm not saying that 
you are doing wrong when you give in to this pressure.  In fact I myself 
do it regularly - when I see a shortcut forward, I often cannot help but 
take it.  However, for two reasons I want to show this more incremental approach:

1. For pedagogical reasons, so you can see how TDD can look at API or UI levels
2. Despite the above, it really is a bit better to avoid the shortcut and 
   be disciplined, if you can manage it.

For example, I wanted to rearrange the directories and rename some things in 
Demo.  Unable to hold myself back, I just made all the changes at once, first, 
without running tests - I made a new directory, put many directories in it, 
renamed several of them, renamed several of the Gradle scripts, and then ran 
my tests.  

*Everything failed*

I tried for hours to get everything working, and I did make a lot of headway, 
but ran into a bunch of unexpected dead ends and built up a lot of technical 
debt in my hurry.  At the end I found myself so much painted into a corner 
that I just reset everything back to how it was and started again.  It turns 
out that the old adage, "haste makes waste", is certainly in effect in programming.  

I started again, but this time only making one small change at a time along 
the path I wished to take, each time running a large set of my tests, fixing 
what I encountered.  This time it was very easy to fix things, and no tech 
debt was built up.  There was a certain satisfaction of knowing I was making
definite forward progress.  When I eventually encountered some of the same 
issues from before, I was able to make the easy fixes and put off the hard 
fixes for another date.

in src/api_tests/test_api.py:

```python
# if they try to return a book that's checked out, it returns a success message.
def test_return_book_indicates_success():
    # first, lend out a book
    test_create_book_loan_already_lent()
    # then try returning it
    r = requests.post("%s/demo/return" % URL, data = {'book': 'alice in wonderland'})
    assert("BOOK_RETURNED" in r.text)
```

If we run the app and try running this new test:

```bash
gradlew apprun # (in one console) this runs the application

gradlew runApiTests  # (in another console) this runs the API tests against the application
```

We find out that there is no endpoint for the new functionality.  Let's 
drop down into the unit tests to drive the creation of this.

There needs to be a new "return book" POST method.  

Let's create a new LibraryReturnServletTests unit test class in 

    src/test/java/com/coveros/training/library/

In that class, here's a first test:

```java
package com.coveros.training.library;

import com.coveros.training.library.domainobjects.Book;
import org.junit.Test;
import org.mockito.Mockito;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class LibraryReturnServletTests {

    private HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
    private HttpServletResponse response = Mockito.mock(HttpServletResponse.class);
    private static final String DEFAULT_BOOK_TITLE = "a book";
    private static final Book DEFAULT_BOOK = new Book(1, DEFAULT_BOOK_TITLE);
    private LibraryReturnServlet libraryReturnServlet = new LibraryReturnServlet();

    @Test
    public void testHappyPathPost() {
        when(request.getParameter("book")).thenReturn(DEFAULT_BOOK_TITLE);

        libraryReturnServlet.doPost(request, response);

        verify(request).setAttribute("result", "SUCCESS");
    }
}
```

This is a great start.  We use the power of wishful thinking to drive what 
we want to exist.  This test may not work, but it gets us going down the 
right path.  

Realize we don't actually work from a place of total ignorance - we just 
try to start as simply as possible.  The developer understands the API and 
realizes that the request will take a parameter through the getParameter 
call, and the servlet has a doPost method that takes a request and a 
response object.

After writing this, naturally the IDE will complain about libraryReturnServlet 
not existing.  Here are the beginnings of that file, at 

    src/main/java/com/coveros/training/library/


Filename: LibraryReturnServlet.class:

```java
package com.coveros.training.library;

import com.coveros.training.helpers.ServletUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class LibraryReturnServlet extends HttpServlet {

    private static final Logger logger = LoggerFactory.getLogger(LibraryReturnServlet.class);
    static LibraryUtils libraryUtils = new LibraryUtils();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) {
        request.setAttribute("result", "SUCCESS");
        ServletUtils.forwardToResult(request, response, logger);
    }
}
```

Try to get this test passing.  Once it does, try to cycle through the TDD 
process. test <-> code.

Once you have done that a while, we can look at what results we might 
get.  Here is what I came up with for `LibraryReturnServletTests`:

```java
package com.coveros.training.library;

import com.coveros.training.library.domainobjects.LibraryActionResults;
import com.coveros.training.library.LibraryUtils;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.Date;
import java.time.LocalDate;
import java.time.Month;

import static org.mockito.Mockito.*;

public class LibraryReturnServletTests {

    private final static Date RETURN_DATE = Date.valueOf(LocalDate.of(2018, Month.JANUARY, 1));
    private static final String BOOK_TITLE = "The DevOps Handbook";
    private static final String ALICE = "alice";
    private final LibraryReturnServlet libraryReturnServlet = Mockito.spy(new LibraryReturnServlet());
    private final LibraryUtils libraryUtils = Mockito.mock(LibraryUtils.class);
    private final HttpServletRequest request = Mockito.mock(HttpServletRequest.class, RETURNS_DEEP_STUBS);
    private final HttpServletResponse response = Mockito.mock(HttpServletResponse.class);

    @Test
    public void testHappyPathPost() {
        when(request.getParameter("book")).thenReturn(BOOK_TITLE);
        doReturn(RETURN_DATE).when(libraryReturnServlet).getDateNow();
        LibraryReturnServlet.libraryUtils = libraryUtils;
        when(libraryUtils.returnBook(BOOK_TITLE, RETURN_DATE)).thenReturn(LibraryActionResults.SUCCESS);

        libraryReturnServlet.doPost(request, response);

        verify(request).setAttribute("result", "SUCCESS");
    }

    @Test
    public void testDateFunction() {
        final Date dateNow = libraryReturnServlet.getDateNow();
        Assert.assertNotEquals(dateNow, Date.valueOf(LocalDate.MIN));
        Assert.assertNotEquals(dateNow, Date.valueOf(LocalDate.MAX));
    }

}
```


LibraryReturnServlet ends up looking like this: 

```java
package com.coveros.training.library;

import com.coveros.training.helpers.ServletUtils;
import com.coveros.training.library.domainobjects.LibraryActionResults;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.Date;
import java.time.LocalDate;

@WebServlet(name = "LibraryReturnServlet", urlPatterns = {"/return"}, loadOnStartup = 1)
public class LibraryReturnServlet extends HttpServlet {

    private static final Logger logger = LoggerFactory.getLogger(LibraryReturnServlet.class);
    static LibraryUtils libraryUtils = new LibraryUtils();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) {
        final String book = request.getParameter("book");
        request.setAttribute("book", book);

        final Date now = getDateNow();
        request.setAttribute("date", now.toString());

        final LibraryActionResults libraryActionResults = libraryUtils.returnBook(book, now);

        request.setAttribute("result", libraryActionResults.toString());
        ServletUtils.forwardToResult(request, response, logger);
    }

    /**
     * Wrapping the call to get a date for now,
     * so it's easier to stub for testing.
     */
    Date getDateNow() {
        return Date.valueOf(LocalDate.now());
    }
}
```

We also need a helper method so we can pass a string to LibraryUtils.returnBook():
Add the following to LibraryUtils.java:

```java
/**
 * A helper-method of returnBook that takes the book's title
 */
public LibraryActionResults returnBook(String bookTitle, Date returnDate) {
    final Book book = searchForBookByTitle(bookTitle);
    return returnBook(book, returnDate);
}
```

Alright, we run the unit test in LibraryReturnServletTests, and... success!  

Now we go up a level.

Make sure the application is running:

```bash
gradlew apprun
```

And in another terminal, run the API tests:

```bash
gradlew runApiTests
```

The API test passes.

Time for another commit!  

```bash
git add .
git commit -m "Adding new endpoint for returnBook with API tests"
```


Developing the UI
-----------------

Almost everything is done - except the UI.

At this point there's not a whole lot going on in the UI - in many instances 
it's just a faceplate on top of the underlying functionality that simply 
provides access.  This is a good practice, since it means that form follows 
function.

However, it is also often the case that you want to drive UI creation in 
TDD style.  Since we have a simple UI, we may be able to afford to just 
do a happy-path case:

in 

    src/ui_tests/behave/features/return_book_ui.feature:

```gherkin
Feature: A librarian may use the UI to return books that were borrowed

    As a librarian,
    I want to mark books as returned,
    so that they can be checked out again.

    Scenario: Happy Path - Able to make a returned book available for borrowing
        Given a borrower had checked out a book,
        When I enter that book as returned
        Then I receive an indication it was returned successfully
```

This is just a copy of the feature from before, but only the happy-path section. We don't have direct access to the database in this test, we're simulating a regular user at the UI.  For that reason, we change the last step to just care about the response to the user, since we've already tested "availability of the book after return" in the previous BDD test.

A point worth bearing in mind here is that we want the absolute minimum number of UI-driven tests.  This is solely for a few practical reasons:
    - UI tests take longer to run
    - They require more maintenance, and more often (e.g. every time something moves around on a page)
    - They are often "flakey", that is, they may only work intermittently, rather than consistently and robustly

What we want is to test everything possible with low-level unit and integration tests, then the interfaces with API tests, then just the UI elements themselves with the UI tests.  This neatly coincides with the agile testing triangle, so we know our proper ratio of tests for high quality.


                                     .
                                    / \
                                   /   \
                                  / UI  \
                                 /-------\
                                /         \
                               /           \
                              /   SERVICE   \
                             /---------------\
                            /                 \
                           /                   \
                          /        UNIT         \
                         /                       \
                        +-------------------------+

                   The Agile testing triangle by Mike Cohn

When writing UI tests, try to consider solely the UI aspects, rather than 
underlying business functionality, most of which should have been tested at 
lower levels.  That said, since we are following BDD, we want to avoid hard 
specifics about how we implement UI, as much as practically possible.  That 
is, notice we are saying "enter a book as returned", rather than something
more concrete like "type the word 'the devops handbook' into the book name field"

Of course, when we get lower level and create the code necessary to automate 
this, then we will need to target concrete implementations.  But note: this 
is the province of the developer, and we want to allow them as much freedom 
to innovate as we are able.  Therefore, though they may go the conventional 
route and provide an expected UI, they may realize there is a better way 
that we hadn't anticipated.  By allowing this creative freedom to our team, 
we get huge dividends in return.

Start by making sure that the application is running.  If it needs to be 
restarted, the command is:

```bash
gradlew apprun
```

We're going to implement this in Python. You will want to open up the Python 
Selenium cheat sheet at docs/ui_testing/python_selenium_cheat_sheet.txt

From the root directory of the Demo application, start up the python terminal 
by running the following command:

```bash
  $ pipenv shell
  $ cd src/ui_tests/python
  $ python -i test.py
```

You will get a terminal that looks like this:

```python
>>>
```

Run the following commands. 
The following will open a browser and assign control to a variable "driver"

```python
>>> driver = start_testing()
```

Go to the Demo application:

```python
>>> driver.get("http://localhost:8080/demo/library.html")
```

Select the library return field:

```python
>>> driver.find_element_by_id("return_book_input_field")
```

This will return a big ugly error message, because the input field doesn't 
exist.  Let's modify our html so that it does.  Add the following to the 
src/main/webapp/library.html file right after the form called "lend", and 
then save the file:

```html
    <form method="post" action="return" class="regular-form">
        <h2>Return a book</h2>

        <label for="return_book_input_field">Book:</label>
        <p><input type="text" id="return_book_input_field" name="book" placeholder="book"/></p>

        <p><input type="submit" id="return_book_submit_button" value="return book" /></p>
    </form>
```

Now if you run the previous command in the Python terminal, you get something like this:

```python
>>> driver.find_element_by_id("return_book_input_field")
<selenium.webdriver.remote.webelement.WebElement (session="bf1b8e3d8058a44ef7ddddedfdb42986", element="81197fe8-a877-45f3-9a33-94e5187eff15")>
```

Let's add some text to it...

```python
>>> return_book_input_field = driver.find_element_by_id("return_book_input_field")
>>> return_book_input_field.send_keys("some_book")
```

You should see some_book appear in the input text for the return book form.
Let's click the submit button...

```python
>>> return_book_submit_button = driver.find_element_by_id("return_book_submit_button")
>>> return_book_submit_button.click()
```

The browser just sent the book to be returned to the application for processing.

This has been an example of how to piecemeal build up a UI through TDD 
commands.  Naturally, there will be times when you want to jump ahead, but 
like previously mentioned, it is better to take your time.  For instance, 
note how doing it this way we had to give real consideration to how testable 
to UI was. How the names of elements had to be clear.

Following is one way to include all the UI tests, through Behave (a Python-based
BDD testing framework):

    src/ui_tests/behave/features/steps/returnBookStepDefs.py:

```python
  from behave import given, when, then
  from hamcrest import *

  URL = 'http://localhost:8080/demo/library.html'


  @given('a borrower had checked out a book,')
  def step_impl(context, book_title):
      context.book = 'some book'
      context.borrower = 'alice'
      __register_borrower(context, context.borrower)
      __register_book(context, context.book)
      __loan_book(context, context.borrower, context.book)


  @when('I enter that book as returned')
  def step_impl(context):
      __return_book(context, context.book)


  @then('I receive an indication it was returned successfully')
  def step_impl(context):
      result = context.driver.find_element_by_id('result')
      assert_that(result.text, contains_string('BOOK_RETURNED'))


  def __register_borrower(context, borrower):
      driver = context.driver
      driver.get(URL)
      borrower_input = driver.find_element_by_id("register_borrower")
      borrower_input.clear()
      borrower_input.send_keys(borrower)
      submit_button = driver.find_element_by_id("register_borrower_submit")
      submit_button.click()

    
  def __register_book(context, title):
      driver = context.driver
      driver.get(URL)
      book_input = driver.find_element_by_id("register_book")
      book_input.clear()
      book_input.send_keys(title)
      submit_button = driver.find_element_by_id("register_book_submit")
      submit_button.click()
   
    
  def __loan_book(context, borrower, title):
      driver = context.driver
      driver.get(URL)
      book_input = driver.find_element_by_id("lend_book")
      book_input.clear()
      book_input.send_keys(title)
      borrower_input = driver.find_element_by_id("lend_borrower")
      borrower_input.clear()
      borrower_input.send_keys(borrower)
      submit_button = driver.find_element_by_id("lend_book_submit")
      submit_button.click()
    
  def __return_book(context, title):
      driver = context.driver
      driver.get(URL)
      book_input = driver.find_element_by_id("return_book_input_field")
      book_input.clear()
      book_input.send_keys(title)
      submit_button = driver.find_element_by_id("return_book_submit_button")
      submit_button.click()
```


Run the Behave BDD UI tests (in another terminal):

```bash
gradlew runBehaveTests
```

These tests pass, and now we have finished the feature!  

During UI testing, you can see errors that may be occurring by looking in the 
server logs for the application.  As far as the UI testing framework is 
concerned, all it knows about is the text and clicks it is sending, so 
anything you need to know related to the actual application under test is
elsewhere.

Time for another commit!  

```bash
git commit -m "UI complete, driven with Behave tests"
```

As a last step, run:

```bash
gradlew runAllTests
```

This should pass.  Check out the cucumber report at: 

    build/reports/bdd/library/index.html

You should now see that there is a new line, 

    "A librarian may return books that were borrowed"

Go ahead and play around with the new feature, at 

    http://localhost:8080/demo/library.html.  

You have coverage for all aspects, from UI to underlying behavior, API testing, 
Database testing, unit testing.

However, that doesn't mean we have attained perfection.  Not even close.  Recall 
that during this short exercise, we mostly focused on happy path scenarios.  

What happens when you click on return book without entering a title?  

This was a slightly artificial scenario, since we didn't really see the time 
spent thinking about alternative ways to solve the problem, and the various 
dead ends the developers may run into.  Throughout this process, we would have 
been running into challenges that needed solving.

This should be considered a good start, but there are always going to be other 
issues.  The more thorough you are here, 

  - the easier it will be to refactor in the future
  - the better the documentation of the system, 
  - the easier it will be to maintain.

For example, if we run a static analysis with SonarQube, what do we see?
What happens if we test this new feature in our performance lab?
Is the logging properly done?
Is this properly integrating with other developer's work?

If we are following the practice of leaving the work in a clean state each day so 
that others might pick it up, are we providing clues in the form of good 
documentation and annotation?

The best way to think about coding is not that we are writing instructions for the
computer to follow.  Rather, it is that we are explaining to a colleague precisely 
what we want the computer to do.  We emphasize human-to-human communication rather 
than human-to-computer.

Hope you found this helpful.

Copyright 2021, Coveros, Byron Katz. All rights reserved.


