package org.group77.mailMe.model;

/**
 * Exception to signal that a given folder could not be located.
 * @author Martin Fredin
 */
public class FolderNotFoundException extends Exception {
  /**
   * Creates the exception with a suitable message based on the folderName.
   * @param folderName - name of the folder which can not be located.
   */
  public FolderNotFoundException(String folderName){ super("Failed to locate " + folderName);}
}
