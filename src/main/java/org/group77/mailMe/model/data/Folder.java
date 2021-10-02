package org.group77.mailMe.model.data;

import java.io.*;
import java.util.*;

public record Folder(
  String name,
  List<Email> emails
) implements Serializable {
}
