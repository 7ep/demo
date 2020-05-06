package com.coveros.training.library.domainobjects;

import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.Assert;
import org.junit.Test;

import java.sql.Date;
import java.time.LocalDate;
import java.time.Month;

public class LoanTests {

    @Test
    public void testShouldHaveEqualsAndHashcodeImplementedCorrectly() {
        EqualsVerifier.forClass(Loan.class).verify();
    }

    @Test
    public void testShouldOutputGoodString() {
        final Loan loan = createTestLoan();
        Assert.assertTrue("toString was: " + loan.toString(), loan.toString().contains("title=The DevOps Handbook"));
    }

    public static Loan createTestLoan() {
        Date borrowDate = Date.valueOf(LocalDate.of(2018, Month.JANUARY, 1));
        return new Loan(BookTests.createTestBook(), BorrowerTests.createTestBorrower(), 1, borrowDate);
    }

    @Test
    public void testCanCreateEmpty() {
        final Loan loan = Loan.createEmpty();
        Assert.assertTrue(loan.isEmpty());
    }
}
