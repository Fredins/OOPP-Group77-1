package org.group77.mailMe.model.exceptions;

public class ServerException extends Exception {
  public ServerException() {
    super("Could not connect to server.");
  }
  public ServerException(Exception e) {
    super(e);
  }
}
