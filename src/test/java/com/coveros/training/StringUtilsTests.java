package com.coveros.training;

import org.junit.Assert;
import org.junit.Test;

public class StringUtilsTests {

  @Test
  public void testShouldConvertNullToEmptyString() {
    final String s = StringUtils.makeNotNullable(null);
    Assert.assertEquals("", s);
  }

  @Test
  public void testShouuldNotAlterNonNullString() {
    final String s = StringUtils.makeNotNullable("abc");
    Assert.assertEquals("abc", s);
  }

}
