package org.group77.mailMe.model.textFinding;

import org.group77.mailMe.model.data.Email;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Class for finding specific emails from a set of emails.
 * Implements methods for searching, filtering and sorting.
 *
 * @author Hampus Jernkrook
 */
public class TextFinder {
    /*
    - dateSorter : Sorter<Email, Date>
- stringFilter : Filter<Email, String>
- dateFilter : Filer<Email, Date>
     */
    private final Filter<Email, String> stringFilter = new Filter<>();
    private final Filter<Email, Date> dateFilter = new Filter<>();
    private final Sorter<Email, Date> dateSorter = new Sorter<>();
    private final InFromPredicate inFromPredicate = new InFromPredicate();
    private final InToPredicate inToPredicate = new InToPredicate();
    private final InAnyTextFieldPredicate inAnyTextFieldPredicate = new InAnyTextFieldPredicate();
    private final MaxDatePredicate maxDatePredicate = new MaxDatePredicate();


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

    List<Email> filterOnTo(List<Email> emails, String searchWord) {
        return null;
    }

    /**
     * Filter out all emails that contain the given search word in their 'to' field.
     *
     * @param emails     - the list of emails to filter.
     * @param searchWord - the string to look for in the 'to' field.
     * @return a list of emails with the searchWord in one of the 'to' addresses.
     * @author Hampus Jernkrook
     */
    List<Email> filterOnFrom(List<Email> emails, String searchWord) {
        return stringFilter.filter(emails, inFromPredicate, searchWord);
    }

    List<Email> filterOnMaxDate(List<Email> emails, Date maxDate) {
        return null;
    }

    public List<Email> sortByOldToNew(List<Email> emails) {
        return null;
    }

    public List<Email> sortByNewToOld(List<Email> emails) {
        return null;
    }
}
