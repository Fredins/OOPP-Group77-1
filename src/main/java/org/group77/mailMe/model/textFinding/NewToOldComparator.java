package org.group77.mailMe.model.textFinding;

import java.util.Comparator;
import java.util.Date;

/**
 * Comparator subclass for establishing a total ordering on Dates d1 and d2.
 * d1 > d2 iff d1 is a later date than d2.
 * d1 < d2 iff d1 is an older date than d2.
 * d1 == d2 iff d1 and d2 are the same date.
 *
 * @author Hampus Jernkrook
 */
public class NewToOldComparator implements Comparator<Date> {
    @Override
    public int compare(Date d1, Date d2) {
        return 0;
    }
}