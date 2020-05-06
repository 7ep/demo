package com.coveros.training.cartesianproduct;

import io.cucumber.datatable.DataTable;
import io.cucumber.java.PendingException;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.Assert;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;

public class CartesianProductStepDefs {

    Set<Set<String>> setOfSets;
    String result;

    @Given("lists as follows:")
    public void listsAsFollows(DataTable randomLists) {
        final List<String> oldLists = randomLists.asList();
        setOfSets = new HashSet<>();

        for (int i = 0; i < oldLists.size(); i++) {
            StringTokenizer defaultTokenizer = new StringTokenizer(oldLists.get(i));
            final Set<String> tempSet = new HashSet<>();
            while (defaultTokenizer.hasMoreTokens())
            {
                tempSet.add(defaultTokenizer.nextToken());
            }
            setOfSets.add(tempSet);
        }

    }

    @When("we calculate the combinations")
    public void weCalculateTheCombinations() {
        result = CartesianProduct.calculate(setOfSets);
        throw new PendingException();
    }

    @Then("the resulting combinations should be as follows:")
    public void theResultingCombinationsShouldBeAsFollows(String expectedResults) {
        Assert.assertEquals(expectedResults, result);
    }
}
