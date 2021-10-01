package org.group77.mailMe.model.data;

public record Folder(
  String name,
  Email[] emails
) {
}
