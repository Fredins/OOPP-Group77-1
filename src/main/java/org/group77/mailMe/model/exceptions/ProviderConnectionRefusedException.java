package org.group77.mailMe.model.exceptions;

public class ProviderConnectionRefusedException extends Exception {
  public ProviderConnectionRefusedException(){ super("Failed to establish a connection with email service provider");}
}
