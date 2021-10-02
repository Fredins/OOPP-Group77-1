package org.group77.mailMe.services.storage;

import org.group77.mailMe.model.data.*;

import java.io.IOException;
import java.util.List;

public interface Storage {
  void store(Account account) throws Exception;
  void store(Account account, List<Folder> folders);
  void store(Account account, Folder folder) throws IOException;

  List<Account> retrieveAllAccounts();
  List<Folder> retrieveFolders(Account account);
  List<Email> retrieveEmails(Account account, String folderName) throws IOException, ClassNotFoundException;
}
