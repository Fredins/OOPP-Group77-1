package org.group77.mailMe.model.textFinding;

import org.group77.mailMe.model.data.Email;

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
    private final MaxDatePredicate maxDatePredicate = new MaxDatePredicate();


    public List<Email> search(List<Email> emails, String searchWord) {
        return null;
    }

    List<Email> filterOnTo(List<Email> emails, String searchWord) {
        return null;
    }

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
