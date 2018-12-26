package com.coveros.training;

import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.Assert;
import org.junit.Test;

public class BorrowerDataTests {

    @Test
    public void testShouldHaveEqualsAndHashcodeImplementedCorrectly() {
        EqualsVerifier.forClass(BorrowerData.class);
    }

    @Test
    public void testShouldOutputGoodString() {
        final BorrowerData borrowerData = new BorrowerData(1, "alice");
        Assert.assertTrue( borrowerData.toString().contains("id=1,name=alice"));
    }
}
