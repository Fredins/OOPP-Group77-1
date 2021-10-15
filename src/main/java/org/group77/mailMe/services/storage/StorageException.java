package org.group77.mailMe.services.storage;

/**
 * abstract unchecked exception wrapper to rethrow checked exceptions like IOException and SQLException
 * @author Martin
 */
public class StorageException extends RuntimeException{
  public StorageException(Exception e){super(e);}
  public StorageException(String msg){super(msg);}
}
