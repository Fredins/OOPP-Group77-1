package org.group77.mailMe.model.data;

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
        String from,
        String[] to,
        String subject,
        String content,
        String attachments,
        LocalDateTime date
) implements Serializable {
    public Email(String from, String[] to, String subject, String content) {
        this(from, to, subject, content, null, LocalDateTime.now(ZoneId.systemDefault())); // set current time as date
    }

    public Email(String from, String[] to, String subject, String content, LocalDateTime date) {
        this(from, to, subject, content, null, date);
    }

    public Email(String from, String[] to, String subject, String content, String attachments) {
        this(from, to, subject, content, attachments, LocalDateTime.now(ZoneId.systemDefault()));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Email email = (Email) o;
        return Objects.equals(from, email.from) && Arrays.equals(to, email.to) && Objects.equals(subject, email.subject) && Objects.equals(content, email.content) && Objects.equals(attachments, email.attachments);
    }
}
