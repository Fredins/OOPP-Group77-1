package org.group77.mailMe.model;

import org.group77.mailMe.*;
import org.group77.mailMe.services.storage.*;
import org.junit.jupiter.api.*;

import java.io.*;
import java.time.*;
import java.util.*;

public class TestEmailFactory {

  /**
   * @author Martin Fredin
   */
  @Test
  public void TestDate(){
    LocalDateTime date = LocalDateTime.now(ZoneId.systemDefault());
    Email email = EmailFactory.createEmail("grupp77@gmail.com",
                                           new String[]{"someone@gmail.com"},
                                           "subject",
                                           "<h1>hello world</hi>",
                                           new ArrayList<>()

    );


    Assertions.assertTrue(
      email.date().isAfter(date.minusSeconds(1)) && email.date().isBefore(date.plusSeconds(1))
    );
  }

  /**
   * @author Martin Fredin
   */
  @Test
  public void TestAttachmentsFile(){
    File file = new File(String.valueOf(Main.class.getResource("images_and_icons/refresh.png")));
    Email email = EmailFactory.createEmail("grupp77@gmail.com",
                                           new String[]{"someone@gmail.com"},
                                           "subject",
                                           "<h1>hello world</hi>",
                                           new ArrayList<>(List.of(file))
    );
    Assertions.assertEquals(file, email.attachments().get(0).file());
  }

  /**
   * @author Martin Fredin
   */
  @Test
  public void TestAttachmentsName(){
    File file = new File(String.valueOf(Main.class.getResource("images_and_icons/refresh.png")));
    String fileName = "refresh.png";
    Email email = EmailFactory.createEmail("grupp77@gmail.com",
                                           new String[]{"someone@gmail.com"},
                                           "subject",
                                           "<h1>hello world</hi>",
                                           new ArrayList<>(List.of(file))
    );
    Assertions.assertEquals(fileName, email.attachments().get(0).name());
  }

}
