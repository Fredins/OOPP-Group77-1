package org.group77.mailMe.model.textFinding;

import org.group77.mailMe.model.data.Email;

import java.util.function.BiPredicate;

/**
 * BiPredicate subclass for checking if a given substring is in any of a given email's text fields.
 *
 * @author Hampus Jernkrook
 */
class InAnyTextFieldPredicate implements BiPredicate<Email, String> {
    /**
     * Searches an email for a given substring. The search is case-insensitive.
     * The substring can be in either one of the email's addresses, the subject or the content.
     *
     * @param email     - the email to scan through.
     * @param substring - the substring to search for in the email.
     * @return true iff the given any of the email's addresses, subject or content contains the given substring.
     * @author Hampus Jernkrook
     */
    @Override
    public boolean test(Email email, String substring) {
        // convert substring to lower case to ensure case-insensitivity.
        substring = substring.toLowerCase();
        // check everything except `to`.
        // Convert all strings to lowercase to ensure case-insensitivity.
        if (email.from().toLowerCase().contains(substring) || email.subject().toLowerCase().contains(substring)
                || email.content().toLowerCase().contains(substring)) {
            return true;
        } // if neither above contained substring, then check the list of to:s
        for (String to : email.to()) {
            if (to.toLowerCase().contains(substring)) {
                return true;
            }
        }
        // if substring was not found in any of the email's field then return false.
        return false;
    }
}
