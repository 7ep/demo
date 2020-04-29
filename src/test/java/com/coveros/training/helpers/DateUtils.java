package com.coveros.training.helpers;

import java.util.Date;

public class DateUtils {

    /**
     * @return true if the current time from {@link Date#getTime} is even.
     */
    public static boolean isTimeEven() {
        final Date date = new Date();
        return date.getTime() % 2 == 0;
    }

}
