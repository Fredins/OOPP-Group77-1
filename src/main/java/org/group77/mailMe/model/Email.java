package org.group77.mailMe.model;

import java.io.*;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

/**
 * Class representing an email, with fields for from- and to-email addresses,
 * subject and text content.
 *
 * @author Elin Hagman
 * @author Martin Fredin (made it a record, override the equals).
 * @author Hampus Jernkrook (added date support)
 */
public record Email(
        String from,    // the sender of the email
        String[] to,    // the recipients of the email
        String subject, // subject of the email
        String content, // text content
        List<Attachment> attachments,   // zero or more attachments attached to this email
        LocalDateTime date  // the date this email was created
) implements Serializable {
    /**
     * Constructor without date parameter. Sets date to the current time.
     * @param from - sender of the email
     * @param to - recipients of the email
     * @param subject - subject of the email
     * @param content - text content of the email
     * @param attachments - list of attachments
     */
    public Email(String from, String[] to, String subject, String content, List<Attachment> attachments) {
        this(from, to, subject, content, attachments, LocalDateTime.now(ZoneId.systemDefault()));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Email email = (Email) o;
        return (Objects.equals(from, email.from) && Arrays.equals(to, email.to) && Objects.equals(subject, email.subject)
                && Objects.equals(content, email.content) && Objects.equals(date, email.date())
                && Objects.equals(attachments, email.attachments));
    }
}
