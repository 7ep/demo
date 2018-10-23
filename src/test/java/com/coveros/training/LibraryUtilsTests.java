package com.coveros.training;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;

public class LibraryUtilsTests {

    @Test
    public void testShouldRegisterABorrower() {
        //arrange
        String borrower = "alice";
        final DatabaseUtils databaseUtils = Mockito.mock(DatabaseUtils.class);
        doReturn(null).when(databaseUtils).searchDatabaseForKey(borrower);
        doNothing().when(databaseUtils).saveTextToFile(borrower);

        LibraryUtils libraryUtils = new LibraryUtils(databaseUtils);

        //act
        String result = libraryUtils.registerBorrower("alice");

        //assert
        Assert.assertEquals("successfully registered", result);


    }
}
