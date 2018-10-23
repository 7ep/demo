package com.coveros.training;

import com.coveros.training.LibraryUtils;
import cucumber.api.PendingException;
import cucumber.api.java.en.Given;



public class LibraryStepDefs {

    private String myUser;
    private LibraryUtils libraryUtils;

    @Given("^a borrower, \"([^\"]*)\", is registered$")
    public void aBorrowerIsRegistered(String username) {
        myUser = username;
        libraryUtils.registerBorrower(username);
    }
}
