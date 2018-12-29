package com.coveros.training;

import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.Assert;
import org.junit.Test;

public class BorrowerTests {

    @Test
    public void testShouldHaveEqualsAndHashcodeImplementedCorrectly() {
        EqualsVerifier.forClass(Borrower.class);
    }

    @Test
    public void testShouldOutputGoodString() {
        final Borrower borrower = new Borrower(1, "alice");
        Assert.assertTrue( borrower.toString().contains("id=1,name=alice"));
    }
}
