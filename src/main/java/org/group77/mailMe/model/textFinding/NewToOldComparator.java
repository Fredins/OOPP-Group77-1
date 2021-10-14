package org.group77.mailMe.model.textFinding;

import org.group77.mailMe.model.data.Email;

import java.util.Comparator;

/**
 * Comparator subclass for establishing a total ordering on Emails e1 and e2.
 * e1 < e2 iff e1 has a later date than e2.
 * e1 > e2 iff e1 has an older date than e2.
 * e1 == e2 iff e1 and e2 have the same date.
 *
 * @author Hampus Jernkrook
 */
public class NewToOldComparator implements Comparator<Email> {
    /**
     * Compares dates of two given emails.
     *
     * @param e1 Original email.
     * @param e2 Email to compare to.
     * @return -1 if e1 is newer than e2, 0 if they are equally old, 1 if e1 is older than e2.
     * @author Hampus Jernkrook
     */
    @Override
    public int compare(Email e1, Email e2) {
        if (e1.date().isAfter(e2.date())) {
            return -1;
        } else if (e1.date().isBefore(e2.date())) {
            return 1;
        } else return 0;
    }
}