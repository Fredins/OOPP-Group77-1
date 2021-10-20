package org.group77.mailMe.model.data;

import java.io.*;

public record Attachment(
  String name,
  byte[] content,
  File file
) implements Serializable{}
