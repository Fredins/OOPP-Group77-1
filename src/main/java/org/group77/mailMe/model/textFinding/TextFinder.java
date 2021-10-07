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

    public List<Email> search(List<Email> emails, String searchWord) {
        return null;
    }

    List<Email> filterOnTo(List<Email> emails, String searchWord) {
        return null;
    }

    List<Email> filterOnFrom(List<Email> emails, String searchWord) {
        return null;
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
