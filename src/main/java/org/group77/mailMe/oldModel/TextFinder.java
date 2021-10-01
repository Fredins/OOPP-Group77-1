package org.group77.mailMe.oldmodel;

import org.group77.mailMe.model.data.*;

import java.util.ArrayList;
import java.util.List;

/**
 * This class implements an algorithm for searching emails for a given substring.
 * This substring can be in either one of the email addresses, the subject or the content.
 *
 * @author Hampus Jernkrook
 */
public class TextFinder {
  /**
   * Searches a list of Emails for a given substring. The search is case-insensitive.
   *
   * @param emails    - the list of emails to search.
   * @param substring - the substring to search for.
   * @return A subset of the input list `emails`, with Emails containing the given substring.
   * @author Hampus Jernkrook
   */
  public List<Email> search(List<Email> emails, String substring) {
    // initialize list to store result
    List<Email> res = new ArrayList<>();
    // convert substring to lower case to ensure case-insensitiveness.
    substring = substring.toLowerCase();
    // for each email, if either from, one of the to:s, subject or content contains
    // the substring, then add that email to res.
    // Convert all strings to lowercase to ensure case-insensitiveness.
    for (Email email : emails) {
      // check everything except `to`
      if (email.from().toLowerCase().contains(substring) || email.subject().toLowerCase().contains(substring)
        || email.content().toLowerCase().contains(substring)) {
        res.add(email);
      } // if neither above contained substring, then check the list of to:s
      else {
        for (String to : email.to()) {
          if (to.toLowerCase().contains(substring)) {
            res.add(email);
            break; // break out of loop to avoid adding the same email more than once.
          }
        }
      }
    }
    return res;
  }
}
