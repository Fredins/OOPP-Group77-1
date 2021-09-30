package org.group77.mailMe.model;

import java.util.List;

/**
 * @author Hampus Jernkrook
 */
public class Folder {
  private final String name;
  private final List<Email> emails;

  public Folder(String name, List<Email> emails) {
    this.name = name;
    this.emails = emails;
  }

  public String getName() {
    return name;
  }

  public List<Email> getEmails() {
    return emails;
  }

  @Override
  public String toString() {
    return name;
  }
}
