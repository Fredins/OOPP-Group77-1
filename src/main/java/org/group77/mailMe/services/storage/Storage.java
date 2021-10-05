package org.group77.mailMe.services.storage;

import org.group77.mailMe.model.data.*;

import java.io.*;
import java.util.*;

public interface Storage {
  void store(Account account) throws Exception;
  void store(Account account, List<Folder> folders);
  void store(Account account, Folder folder) throws IOException;

  List<Account> retrieveAccounts();
  List<Folder> retrieveFolders(Account account);
  List<Email> retrieveEmails(Account account, String folderName) throws IOException, ClassNotFoundException;
}
