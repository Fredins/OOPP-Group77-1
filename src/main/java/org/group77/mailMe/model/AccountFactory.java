package org.group77.mailMe.model;

import org.group77.mailMe.model.data.*;

/**
 * @author Martin Fredin.
 * <p>
 * Class for creating concrete account instances.
 */
public class AccountFactory {

  /**
   * Creates a concrete account instance.
   *
   * @param emailAddress - email address of the created account
   * @param password     - password of the created account
   * @return An account instance with the given attributes.
   * Sets ServerProvider depending on the suffix of the email address.
   * "@gmail.com" gets associated with ServerProvider.GMAIL.
   * "@hotmail.com", "@live.com", "@outlook.com" with ServerProvider.MICROSOFT.
   */
  public static Account createAccount(String emailAddress, char[] password) {
    if (emailAddress.contains("@gmail.com")) {
      return new Account(emailAddress, password, ServerProvider.GMAIL);
    }
    return null;
  }
}
