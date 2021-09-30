package org.group77.mailMe.services.storage;

import org.group77.mailMe.model.Account;
import org.group77.mailMe.model.Email;
import org.group77.mailMe.model.Folder;

import java.io.IOException;
import java.util.List;

public interface Storage {

  public boolean store(Account account) throws Exception;

  public boolean store(String emailAddress, List<Folder> folders) throws IOException;

  public void store(String emailAddress, Folder folder) throws IOException;

  public Account retrieveAccount(String emailAddress) throws IOException, ClassNotFoundException;

  public List<Folder> retrieveFolders(String emailAddress);

  public List<Email> retrieveEmails(String emailAddress, String folderName) throws IOException, ClassNotFoundException;

  public List<String> retrieveAllEmailAddresses();
}
