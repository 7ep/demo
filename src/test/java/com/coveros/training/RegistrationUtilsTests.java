package com.coveros.training;

import com.coveros.training.persistence.RegistrationUtils;
import org.junit.Assert;
import org.junit.Test;

public class RegistrationUtilsTests {

  @Test
  public void testEmptyObject() {
    final RegistrationUtils registrationUtils = RegistrationUtils.createEmpty();
    Assert.assertTrue(registrationUtils.isEmpty());
  }
}
