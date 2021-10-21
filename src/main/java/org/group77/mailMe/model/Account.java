package org.group77.mailMe.model;

import java.io.*;

/**
 * Class representing an email account, with email address, password
 * and server provider.
 *
 * @author Elin Hagman
 * @author Martin Fredin (made it a record).
 */
public record Account(
        String emailAddress, // the email address of the account
        char[] password,    // the password used for this account on the remote server
        ServerProvider provider // the server provider that this account is used for
) implements Serializable {
}
