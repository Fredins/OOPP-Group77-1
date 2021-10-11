package org.group77.mailMe.model.textFinding;

import org.group77.mailMe.model.data.Email;

import java.util.Arrays;
import java.util.List;
import java.util.function.BiPredicate;

/**
 * BiPredicate subclass for checking if a given substring is in a given email's 'To' field.
 *
 * @author Hampus Jernkrook
 */
public class InToPredicate implements BiPredicate<Email, String> {
    /**
     * Tests whether the given substring is in any of the email's to-addresses.
     * Case-insensitive.
     *
     * @param email     - the email to scan through 'to:s' in.
     * @param substring - the substring to search for in the to-addresses.
     * @return true iff any of the given email's to-addresses contains the given substring.
     * @author Hampus Jernkrook
     */
    @Override
    public boolean test(Email email, String substring) {
        // convert array of to-addresses to stream and filter out those that contain the substring.
        return (Arrays.stream(email.to()).anyMatch(to -> to.toLowerCase().contains(substring.toLowerCase())));
    }
}
