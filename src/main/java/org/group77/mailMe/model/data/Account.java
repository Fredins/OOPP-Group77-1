package org.group77.mailMe.model.data;

import java.io.*;

public record Account(
  String emailAddress,
  char[] password,
  ServerProvider provider
) implements Serializable {
}
