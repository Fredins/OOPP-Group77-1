package org.group77.mailMe.model.data;

public record Email(
  String from,
  String[] to,
  String subject,
  String content) {
}
