package com.coveros.training;

import com.coveros.selenified.Locator;
import com.coveros.selenified.Selenified;
import com.coveros.selenified.application.App;
import com.coveros.selenified.element.Element;
import com.coveros.selenified.services.Call;
import com.coveros.selenified.services.Request;
import com.coveros.selenified.services.Response;
import org.testng.Assert;
import org.testng.ITestContext;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.HashMap;

public class SelenifiedSample extends Selenified {

    @BeforeClass(alwaysRun = true)
    public void beforeClass(ITestContext test) {
        // set the base URL for the tests here
        setAppURL(this, test, "https://www.coveros.com/");
    }

    @DataProvider(name = "coveros search terms", parallel = true)
    public Object[][] DataSetOptions() {
        return new Object[][]{new Object[]{"python"},
                new Object[]{"perl"}, new Object[]{"bash"},};
    }

    @Test(groups = {"sample"}, description = "A sample selenium test to check a title")
    public void sampleTest() {
        // use this object to manipulate the app
        App app = this.apps.get();
        // verify the correct page title
        app.azzert().titleEquals("Coveros | Bringing together agile and security to deliver superior software");
        // verify no issues
        finish();
    }

    @Test(dataProvider = "coveros search terms", groups = {"sample"},
            description = "A sample selenium test using a data provider to perform a search")
    public void sampleTestWDataProvider(String searchTerm) {
        // use this object to manipulate the app
        App app = this.apps.get();
        // find the search box element and create the object
        Element searchBox = app.newElement(Locator.NAME, "s");
        //perform the search and submit
        searchBox.type(searchTerm);
        searchBox.submit();
        //wait for the page to return the results
        app.newElement(Locator.ID, "recent-posts-4").waitForState().present();
        // verify the correct page title
        app.azzert().titleEquals("You searched for " + searchTerm + " - Coveros");
        // verify no issues
        finish();
    }

    @Test(groups = {"sample", "services"}, description = "A sample web services test to verify the response code")
    public void sampleServicesSearchTest() {
        HashMap<String, Object> params = new HashMap<>();
        params.put("s", "Max+Saperstone");
        // use this object to verify the app looks as expected
        Call call = this.calls.get();
        // retrieve the zip code and verify the return code
        final Response response = call.get("", new Request().setUrlParams(params));
        Assert.assertEquals(403, response.getCode());
        // verify no issues
        finish();
    }
}

