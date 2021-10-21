package org.group77.mailMe.model.textFinding;

import org.group77.mailMe.model.Email;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Class for finding specific emails from a set of emails.
 * Implements methods for searching, filtering and sorting.
 *
 * @author Hampus Jernkrook
 */
public class TextFinder {

    private final Filter<Email, String> stringFilter = new Filter<>();
    private final Filter<Email, LocalDateTime> dateFilter = new Filter<>();
    private final Sorter<Email> emailSorter = new Sorter<>();
    private final InFromPredicate inFromPredicate = new InFromPredicate();
    private final InToPredicate inToPredicate = new InToPredicate();
    private final InAnyTextFieldPredicate inAnyTextFieldPredicate = new InAnyTextFieldPredicate();
    private final OlderThanPredicate olderThanPredicate = new OlderThanPredicate();
    private final NewToOldComparator newToOldComparator = new NewToOldComparator();


    /**
     * Searches all text fields of an email for a given search word.
     *
     * @param emails     - the list of emails to search through.
     * @param searchWord - the search word to find.
     * @return - a list of emails that all contain the given search word in at least one of their text fields.
     * @author Hampus Jernkrook
     */
    public List<Email> search(List<Email> emails, String searchWord) {
        return stringFilter.filter(emails, inAnyTextFieldPredicate, searchWord);
    }

    /**
     * Filter out all emails that contain the given search word in their 'to' field.
     *
     * @param emails     - the list of emails to filter.
     * @param searchWord - the string to look for in the 'to' field.
     * @return a list of emails with the searchWord in one of the 'to' addresses.
     * @author Hampus Jernkrook
     */
    public List<Email> filterOnTo(List<Email> emails, String searchWord) {
        return stringFilter.filter(emails, inToPredicate, searchWord);
    }

    /**
     * Filter out all emails that contain the given search word in their 'from' field.
     *
     * @param emails     - the list of emails to filter.
     * @param searchWord - the string to look for in the 'from' field.
     * @return a list of emails with the searchWord in the from-address.
     * @author Hampus Jernkrook
     */
    public List<Email> filterOnFrom(List<Email> emails, String searchWord) {
        return stringFilter.filter(emails, inFromPredicate, searchWord);
    }

    /**
     * Filter out all emails that have a date less than the given max date.
     *
     * @param emails  - the list of emails to filter.
     * @param maxDate - the maximum date of any email in the result.
     * @return a list of emails with date less than the given max date.
     * @author Hampus Jernkrook
     */
    public List<Email> filterOnMaxDate(List<Email> emails, LocalDateTime maxDate) {
        return dateFilter.filter(emails, olderThanPredicate, maxDate);
    }

    /**
     * Filter out all emails that have a date greater than the given min date.
     *
     * @param emails  - the list of emails to filter.
     * @param minDate - the minimum date of any email in the result.
     * @return a list of emails with date greater than the given max date.
     * @author Hampus Jernkrook
     */
    public List<Email> filterOnMinDate(List<Email> emails, LocalDateTime minDate) {
        // get all emails with dates after the minDate, by running tests with negated olderThanPredicate.
        return dateFilter.filter(emails, olderThanPredicate.negate(), minDate);
    }

    /**
     * Sorts a list of emails by old to new dates.
     *
     * @param emails - list of emails to sort.
     * @return A deep copy of the input list, sorted from old to new emails.
     * @author Hampus Jernkrook
     */
    public List<Email> sortByOldToNew(List<Email> emails) {
        return emailSorter.sort(emails, newToOldComparator.reversed());
    }

    /**
     * Sorts a list of emails by new to old dates.
     *
     * @param emails - list of emails to sort.
     * @return A deep copy of the input list, sorted from new to old emails.
     * @author Hampus Jernkrook
     */
    public List<Email> sortByNewToOld(List<Email> emails) {
        return emailSorter.sort(emails, newToOldComparator);
    }
}
