package org.group77.mailMe.services.storage;

import org.group77.mailMe.model.data.*;

import java.io.*;
import java.util.*;
import java.util.stream.*;

public class LocalDiscStorage implements Storage {

  private final String appPath;
  private final String separator;

  // Storage interface methods
  public LocalDiscStorage() throws OSNotFoundException {
    String[] appDirAndSep = OSHandler.getAppDirAndSeparator();
    appPath = appDirAndSep[0]; // TODO perhaps replace with Pair<String, String>
    separator = appDirAndSep[1];
    this.mkdir(appPath);
  }

  /**
   * @param account - keeps email address of account object.
   * @return - Needs more work.
   * @throws Exception
   * @author Alexey Ryabov. Revised by Hampus Jernkrook
   */
  @Override
  public void store(Account account) throws Exception {
    String address = account.emailAddress();
    String path = appPath + separator + address;
    try {
      // if account is not already added, create directory and store file with account details
      if (!testExists(path)) {
        mkdir(path);
        // path of the account file
        String accountFilePath = path + separator + "Account";
        touch(accountFilePath);
        serialize(account, accountFilePath);
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * TODO how to write tests for store and retrieve? The tests will be dependent on both...
   *
   * @param folders - List of folders to store away.
   * @return true if the folders could be successfully stored.
   * @throws IOException if the file operations fail.
   * @author Hampus Jernkrook
   */
  @Override
  public void store(Account account, List<Folder> folders) {
    // path to the given account's directory
    String accountPath = appPath + separator + account.emailAddress();
    // For each folder, create a directory with the folder name and store the folder object
    folders.forEach(f -> {
                      try {
                        String folderPath = accountPath + separator + f.name();
                        touch(folderPath);
                        serialize(f, folderPath);
                      } catch (IOException e) {
                        e.printStackTrace();
                      }
                    }
    );
  }

  /**
   * store folder in corresponding account dir
   */
  @Override
  public void store(Account account, Folder folder) throws IOException {
    String folderPath = appPath + separator + account.emailAddress() + separator + folder.name();
    touch(folderPath);
    serialize(folder, folderPath);
  }

  /**
   * 1. retrieve all folders
   * 2. sort folders
   */
  @Override
  public List<Folder> retrieveFolders(Account account) {
    String accountPath = appPath + separator + account.emailAddress();
    File[] files = Arrays.stream(Objects.requireNonNull((new File(accountPath)).listFiles()))
      .filter(file -> !file.getName().equals("Account"))
      .toArray(File[]::new);
    return Arrays.stream(files)
      .map(file -> {
        Folder folder = null;
        try {
          folder = (Folder) deserialize(file.getPath());
        } catch (IOException | ClassNotFoundException e) {
          e.printStackTrace();
        }
        return folder;
      })
      .filter(Objects::nonNull)
      .sorted((folder1, folder2) -> {
        List<String> orderedNames = List.of(
          "Inbox",
          "Archive",
          "Sent",
          "Drafts",
          "Trash"
        );
        int index1 = orderedNames.indexOf(folder1.name());
        int index2 = orderedNames.indexOf(folder2.name());
        return Integer.compare(index1, index2);
      })
      .collect(Collectors.toList());
  }

  /**
   * retrieve all saved accounts
   */
  @Override
  public List<Account> retrieveAccounts() {
    File[] accountDirs = (new File(appPath)).listFiles();
    List<Account> accounts = new ArrayList<>();
    if (accountDirs != null) {
      accounts = Arrays.stream(accountDirs)
        .map(f -> {
          Account account = null;
          try {
            account = (Account) deserialize(f.getPath() + separator + "Account");
          } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
          }
          return account;
        })
        .filter(Objects::nonNull)
        .collect(Collectors.toList());
    }
    return accounts;
  }


  /**
   * @param path - path of the file/directory to search for.
   * @return false.
   * @throws Exception
   * @author Alexey Ryabov. Revised by Hampus Jernkrook
   * Tell whether a file/directory exists with the given path.
   */
  private boolean testExists(String path) {
    return (new File(path).exists());
  }

  /**
   * @param path
   * @throws IOException
   * @author Hampus Jernkrook
   */
  private void touch(String path) throws IOException {
    File file = new File(path);
    // if the file already exists, overwrite it.
    if (file.exists()) {
      file.delete(); // first delete the existing
    }
    file.createNewFile(); //Create new file.
  }

  private void mkdir(String path) {
    File file = new File(path);
    file.mkdir(); //TODO does this create the directory if it exists? (hampus)
  }

  private void serialize(Object o, String path) throws IOException {
    FileOutputStream file = new FileOutputStream(path);
    ObjectOutputStream out = new ObjectOutputStream(file);
    out.writeObject(o);
    out.close();
    file.close();
  }

  private Object deserialize(String path) throws IOException, ClassNotFoundException {
    FileInputStream file = new FileInputStream(path);
    ObjectInputStream in = new ObjectInputStream(file);
    Object o = in.readObject();
    in.close();
    file.close();
    return o;
  }
}