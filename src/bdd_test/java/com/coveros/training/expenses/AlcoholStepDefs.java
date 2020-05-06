package com.coveros.training.expenses;

import io.cucumber.datatable.DataTable;
import io.cucumber.java.PendingException;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.Assert;

import java.util.Map;

public class AlcoholStepDefs {

    private AlcoholResult alcoholResult;
    private DinnerPrices dinnerPrices;

    @Given("a dinner with the following prices in dollars:")
    public void aDinnerWithTheFollowingPricesInDollars(DataTable prices) {
        final Map<String, String> data = prices.asMaps().get(0);
        dinnerPrices = new DinnerPrices(
                Double.parseDouble(data.get("subtotal")),
                Double.parseDouble(data.get("food total")),
                Double.parseDouble(data.get("tip")),
                Double.parseDouble(data.get("tax")));

    }

    @When("I calculate the alcohol-related portion")
    public void iCalculateTheAlcoholRelatedPortion() {
        alcoholResult = AlcoholCalculator.calculate(dinnerPrices);
        throw new PendingException();
    }

    @Then("I get the following results:")
    public void iGetTheFollowingResults(DataTable expectedData) {
        final Map<String, String> data = expectedData.asMaps().get(0);
        AlcoholResult expectedResult = new AlcoholResult(
                Double.parseDouble(data.get("total food price")),
                Double.parseDouble(data.get("total alcohol price")),
                Double.parseDouble(data.get("food ratio")));
        Assert.assertEquals(alcoholResult, expectedResult);
    }
}
