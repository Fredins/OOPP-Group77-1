package org.group77.mailMe.services.storage;

/**
 * abstract unchecked exception wrapper to rethrow checked exceptions like IOException and SQLException
 * @author Martin
 */
public class StorageException extends RuntimeException{
  /**
   * Wrapper for other exceptions.
   * @param e - the exception to wrap. This exception will use the message of e.
   */
  public StorageException(Exception e){super(e);}
  public StorageException(String msg){super(msg);}
}
