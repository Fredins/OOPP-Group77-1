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
     *
     * @param email     - the email to scan through 'to:s' in.
     * @param substring - the substring to search for in the to-addresses.
     * @return true iff any of the given email's to-addresses contains the given substring.
     * @author Hampus Jernkrook
     */
    @Override
    public boolean test(Email email, String substring) {
        List<String> toList = Arrays.asList(email.to());
        return (toList.stream().anyMatch(to -> to.contains(substring.toLowerCase())));
    }
}
