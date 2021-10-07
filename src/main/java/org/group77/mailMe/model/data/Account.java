package org.group77.mailMe.model.data;

import java.io.*;

/**
 * Class representing an email account, with email address, password
 * and server provider.
 *
 * @author Elin Hagman
 * @author Martin Fredin (made it a record).
 */
public record Account(
        String emailAddress,
        char[] password,
        ServerProvider provider
) implements Serializable {
}
