package org.group77.mailMe.model;

import java.io.*;
import java.time.*;
import java.util.*;
import java.util.stream.*;

/**
 * Creates an email
 * @author Martin
 */
public class EmailFactory {
  /**
   * creates an email with current date and attachments
   * @param from email address of the sender
   * @param to email addresses of the receivers
   * @param subject subject of the email
   * @param content html content of the email
   * @param attachments list of File that contains the path to the real files
   */
  public static Email createEmail(String from, String[] to, String subject, String content, List<File> attachments){
    return new Email(from,
                      to,
                      subject,
                      content,
                      attachments.stream()
                       .map(file -> new Attachment(file.getName(), null, file))
                       .collect(Collectors.toList()),
                      LocalDateTime.now(ZoneId.systemDefault())
    );
  }
}
