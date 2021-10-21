package org.group77.mailMe.model;

import org.group77.mailMe.model.Email;

import java.io.*;
import java.util.*;

/**
 * Class representing named folders containing emails.
 *
 * @author Alexey Ryabov
 * @author Martin
 */
public record Folder(
        String name,
        List<Email> emails
) implements Serializable {

    public void addEmail(Email email) {
        this.emails.add(email);
    }

    public void deleteEmail(Email email) {
        this.emails.remove(email);
    }
}
