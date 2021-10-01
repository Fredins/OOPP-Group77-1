package org.group77.mailMe.model.data;

public record Account(
  String emailAddress,
  char[] password,
  ServerProvider provider
) {
}
