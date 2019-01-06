package com.coveros.training.domainobjects;

import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.Assert;
import org.junit.Test;

public class BorrowerTests {

    @Test
    public void testShouldHaveEqualsAndHashcodeImplementedCorrectly() {
        EqualsVerifier.forClass(Borrower.class).verify();
    }

    @Test
    public void testShouldOutputGoodString() {
        final Borrower borrower = createTestBorrower();
        Assert.assertTrue( borrower.toString().contains("id=1,name=alice"));
    }

    public static Borrower createTestBorrower() {
        return new Borrower(1, "alice");
    }

    @Test
    public void testCanCreateEmpty() {
        final Borrower borrower = Borrower.createEmpty();
        Assert.assertTrue(borrower.isEmpty());
    }

}
