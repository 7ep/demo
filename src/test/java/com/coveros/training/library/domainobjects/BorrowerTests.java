package com.coveros.training.library.domainobjects;

import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class BorrowerTests {

    @Test
    public void testShouldHaveEqualsAndHashcodeImplementedCorrectly() {
        EqualsVerifier.forClass(Borrower.class).verify();
    }

    @Test
    public void testShouldOutputGoodString() {
        final Borrower borrower = createTestBorrower();
        Assert.assertTrue(borrower.toString().contains("id=1,name=alice"));
    }

    public static Borrower createTestBorrower() {
        return new Borrower(1, "alice");
    }

    @Test
    public void testCanCreateEmpty() {
        final Borrower borrower = Borrower.createEmpty();
        Assert.assertTrue(borrower.isEmpty());
    }

    /**
     * toOutputString returns a JSON version of this object.
     */
    @Test
    public void testShouldReturnJsonString() {
        final Borrower borrower = createTestBorrower();
        final String expectedResult = "{\"Name\": \"alice\", \"Id\": \"1\"}";
        assertEquals(expectedResult, borrower.toOutputString());
    }

}
