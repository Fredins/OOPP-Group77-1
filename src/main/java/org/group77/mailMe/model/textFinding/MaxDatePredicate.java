package org.group77.mailMe.model.textFinding;

import org.group77.mailMe.model.data.Email;

import java.util.Date;
import java.util.function.BiPredicate;

/**
 * BiPredicate subclass for checking if a given email has a date less than the given max date.
 *
 * @author Hampus Jernkrook
 */
public class MaxDatePredicate implements BiPredicate<Email, Date> {
    @Override
    public boolean test(Email email, Date maxDate) {
        return false;
    }
}