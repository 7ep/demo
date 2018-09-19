package com.coveros.training;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.*;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import cucumber.api.PendingException;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;


import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

import cucumber.api.java.en.When;

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
