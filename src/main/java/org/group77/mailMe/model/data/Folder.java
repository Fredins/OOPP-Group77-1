package org.group77.mailMe.model.data;

import java.io.*;
import java.util.*;

public record Folder(
  String name,
  List<Email> emails
) implements Serializable {
  @Override public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Folder folder = (Folder) o;
    return Objects.equals(name, folder.name) && Objects.equals(emails, folder.emails);
  }
  @Override public int hashCode() {
    return Objects.hash(name, emails);
  }
}
