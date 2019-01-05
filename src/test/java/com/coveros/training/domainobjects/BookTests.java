package com.coveros.training.domainobjects;

import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.Assert;
import org.junit.Test;

public class BookTests {
  @Test
  public void testShouldHaveEqualsAndHashcodeImplementedCorrectly() {
    EqualsVerifier.forClass(Book.class);
  }

  @Test
  public void testShouldOutputGoodString() {
    final Book book = new Book(1, "The DevOps Handbook");
    Assert.assertTrue("toString was: " + book.toString(), book.toString().contains("id=1,title=The DevOps Handbook"));
  }
}
