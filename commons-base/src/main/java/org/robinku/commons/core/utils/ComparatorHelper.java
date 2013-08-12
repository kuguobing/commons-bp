package org.robinku.commons.core.utils;

import java.util.Calendar;
import java.util.Date;

/**
 * 
 * @author: Ku Guobing
 * @Email: kuguobing@gmail.com
 * @Date: 2008-7-20
 */
public final class ComparatorHelper {

    private ComparatorHelper() {
    }

    public static int compareCalendar(Calendar o1, Calendar o2) {
        // deals with nulls as well as simple compare
        if (o1 == null && o2 != null)
            return -1;
        if (o2 == null && o1 != null)
            return 1;
        if (o1 == null && o2 == null)
            return 0;

        return o1.compareTo(o2);
    }

    public static int compareDate(Date o1, Date o2) {
        // deals with nulls as well as simple compare
        if (o1 == null && o2 != null)
            return -1;
        if (o2 == null && o1 != null)
            return 1;
        if (o1 == null && o2 == null)
            return 0;

        return o1.compareTo(o2);
    }

    public static int compareStrings(String o1, String o2) {
        return compareStrings(o1, o2, false);
    }

    public static int compareStrings(String o1, String o2, boolean ignoreCase) {
        // deals with nulls as well as simple compare
        if (o1 == null && o2 != null)
            return -1;
        if (o2 == null && o1 != null)
            return 1;
        if (o1 == null && o2 == null)
            return 0;

        if (ignoreCase) {
            return o1.compareToIgnoreCase(o2);
        } else {
            return o1.compareTo(o2);
        }
    }

}
