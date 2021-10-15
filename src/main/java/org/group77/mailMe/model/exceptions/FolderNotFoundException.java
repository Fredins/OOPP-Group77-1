package org.group77.mailMe.model.exceptions;

public class FolderNotFoundException extends Exception {
  public FolderNotFoundException(String folderName){ super("Failed to locate " + folderName);}
  FolderNotFoundException(Exception e){ super(e); }
}
