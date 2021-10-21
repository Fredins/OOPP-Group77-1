package org.group77.mailMe.model;

import org.group77.mailMe.*;

import java.io.*;
import java.time.*;
import java.util.*;
import java.util.stream.*;

public class EmailFactory {
  public static Email createEmail(Control control, String[] to, String subject, String content, List<File> attachments){
    return new Email(control.getActiveAccount().get().emailAddress(),
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
