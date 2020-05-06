package com.coveros.training.library.domainobjects;

import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.Assert;
import org.junit.Test;

public class BookTests {

    @Test
    public void testShouldHaveEqualsAndHashcodeImplementedCorrectly() {
        EqualsVerifier.forClass(Book.class).verify();
    }

    @Test
    public void testShouldOutputGoodString() {
        final Book book = createTestBook();
        final String b = book.toString();
        Assert.assertTrue("toString was: " + book.toString(),
                b.contains("title=The DevOps Handbook") &&
                b.contains("id=1"));
    }

    public static Book createTestBook() {
        return new Book(1, "The DevOps Handbook");
    }

    @Test
    public void testCanCreateEmpty() {
        final Book book = Book.createEmpty();
        Assert.assertTrue(book.isEmpty());
    }

}
