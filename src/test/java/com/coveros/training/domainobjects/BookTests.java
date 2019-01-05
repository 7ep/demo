package com.coveros.training.domainobjects;

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
    Assert.assertTrue("toString was: " + book.toString(), book.toString().contains("title=The DevOps Handbook,id=1"));
  }

  static Book createTestBook() {
    return new Book(1, "The DevOps Handbook");
  }
}
