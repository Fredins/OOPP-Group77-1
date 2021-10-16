package org.group77.mailMe.services.storage;

import org.group77.mailMe.model.data.*;

import java.io.*;
import java.util.*;

/**
 * Interface for multiple storage solutions
 * @author Martin, Elin, David
 */
public interface Storage {
  void store(Account account) throws Exception;
  void store(Account account, List<Folder> folders);
  void store(Account account, Folder folder) throws IOException;
  void store(Account account, String s) throws IOException, Exception;

  List<Account> retrieveAccounts();
  List<Folder> retrieveFolders(Account account);
  List<String> retrieveSuggestions(Account account);
}
