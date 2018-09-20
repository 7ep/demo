package com.coveros.training;

import cucumber.api.java.en.When;
import org.junit.Assert;

import java.util.List;

public class MathServletTest {

    @When("I add number_a to number_b, I get a sum:")
    public void i_add_number_a_to_number_b_I_get_a_sum(List<List<Integer>> dataTable) {
        for (List row : dataTable) {
          Integer addend_a = (Integer)row.get(0);
          Integer addend_b = (Integer)row.get(1);
          Integer expected_sum = (Integer)row.get(2);
          Integer actual_sum = addend_a + addend_b;
          Assert.assertEquals(expected_sum, actual_sum);
        }
    }

}
