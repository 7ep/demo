package com.coveros.training.helpers;

import com.coveros.training.helpers.DateUtils;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import java.time.LocalDate;
import java.time.Month;
import java.time.temporal.ChronoUnit;

public class DateUtilsTests {

    /**
     * This contrived example is here to provide an opportunity to
     * improve a flaky test.  Our tests should always return the same
     * result, we want total control over our laboratory.
     */
    @Ignore("used for teaching purposes")
    @Test
    public void testShouldReturnEvenTime() {
        Assert.assertTrue(DateUtils.isTimeEven());
    }

    /**
     * In Virginia, the law states you must be at least 16 years and 3 months
     * old to get a drivers license.
     *
     * In this test we'll presume a straightforward happy path.  A person
     * is born Jan 1, 1990.  When do they get their driver's license?
     *
     * 1990 + 16 years = 2006
     * Jan + 3 months = April
     * So, April 1, 2006.
     * How many days is that from the day they were born?
     */
    @Ignore("used for teaching purposes")
    @Test
    public void testShouldBe16Years3Months_HappyPath() {
        // arrange
        // assume a person is born Jan 1, 1990
        final LocalDate birthDate = LocalDate.of(1990, Month.JANUARY, 1);
        // expected that they can get it on April 1, 2006
        final LocalDate expectedLicenseDate = LocalDate.of(2006, Month.APRIL, 1);

        // act
        // add 16 years and 3 months
        final LocalDate licenseDate = calculateFirstPossibleLicenseDate(birthDate);

        // assert
        Assert.assertEquals(expectedLicenseDate, licenseDate);
        long noOfDaysBetween = ChronoUnit.DAYS.between(licenseDate, birthDate);

        // there were 5934 days from the day they were born to the day they can
        // get their license.  It seems fair that that number is the same for
        // everyone, right?  Why should being born on a different day of the
        // year make a difference in how long you have to wait to get your license?
        Assert.assertTrue(Math.abs(noOfDaysBetween) == 5934);

        // let's run an experiment.  We'll start with someone born in Jan 1, 1990,
        // and use the same method to calculate their first possible day to get
        // a driver's license.  We will then loop through every day for 100 years
        // to make sure the answer is always the same.

        for (LocalDate scanningBirthDate = birthDate;
             scanningBirthDate.isBefore(birthDate.plusYears(100));
             scanningBirthDate = scanningBirthDate.plusDays(1)) {
            LocalDate scanningLicenseDate = calculateFirstPossibleLicenseDate(scanningBirthDate);
            // assert
            long daysBetween = ChronoUnit.DAYS.between(scanningLicenseDate, scanningBirthDate);
            String errorMessage = "days between was " + Math.abs(daysBetween) + " for a birthdate of " + scanningBirthDate
                    + " and a license date of " + scanningLicenseDate;
            Assert.assertTrue(errorMessage, Math.abs(daysBetween) == 5934);
        }
    }

    /**
     * Determine the first date which this person is able to obtain a driver's
     * license, per Virginia's law that they be 16 years and 3 months.
     *<p>
     * calculates using this method: <p><code>birthDate.plusYears(16).plusMonths(3);</code></p>
     * </p>
     */
    private LocalDate calculateFirstPossibleLicenseDate(LocalDate birthDate) {
        return birthDate.plusYears(16).plusMonths(3);
    }

}
