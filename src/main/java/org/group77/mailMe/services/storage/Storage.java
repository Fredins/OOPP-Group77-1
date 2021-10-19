package org.group77.mailMe.services.storage;

import org.group77.mailMe.model.data.*;
import org.group77.mailMe.model.exceptions.*;

import java.io.*;
import java.util.*;

/**
 * Interface for multiple storage solutions
 * @author Martin, Elin, David
 */
public interface Storage {

  void store(Account account) throws StorageException;
  void store(Account account, List<Folder> folders) throws StorageException;
  void store(Account account, Folder folder) throws StorageException;
  void store(Account account, String s) throws IOException, Exception;

  List<Account> retrieveAccounts() throws StorageException;
  List<Folder> retrieveFolders(Account account) throws StorageException;
  List<String> retrieveSuggestions(Account account);}

