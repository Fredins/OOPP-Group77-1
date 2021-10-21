package org.group77.mailMe.model.textFinding;

import org.group77.mailMe.model.Email;

import java.util.function.BiPredicate;

/**
 * BiPredicate subclass for checking if a given substring is in a given email's 'From' field.
 *
 * @author Hampus Jernkrook
 */
class InFromPredicate implements BiPredicate<Email, String> {
    /**
     * Tests whether the given substring is in the email's from-address.
     * Case-insensitive.
     *
     * @param email     - the email to scan through 'from' in.
     * @param substring - the substring to search for in the from-address.
     * @return true iff the given email's 'from'-address contains the given substring.
     * @author Hampus Jernkrook
     */
    @Override
    public boolean test(Email email, String substring) {
        return (email.from().toLowerCase().contains(substring.toLowerCase()));
    }
}
