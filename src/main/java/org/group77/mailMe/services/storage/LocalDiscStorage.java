package org.group77.mailMe.services.storage;

import org.group77.mailMe.model.data.*;

import java.io.*;
import java.util.*;
import java.util.stream.*;

public class LocalDiscStorage implements Storage {

  String appPath;
  String separator;

  // Storage interface methods
  public LocalDiscStorage() throws OSNotFoundException, IOException {
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
    // TODO if the folder directories already exists, then these should be overwritten...
    //  will they be overwritten now?
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

  @Override
  public void store(Account account, Folder folder) throws IOException {
    String folderPath = appPath + separator + account.emailAddress() + separator + folder.name();
    touch(folderPath);
    serialize(folder, folderPath);
  }


  @Override
  public List<Folder> retrieveFolders(Account account) {
    String accountPath = appPath + separator + account.emailAddress();
    File[] folderDirs = Arrays.stream(Objects.requireNonNull((new File(accountPath)).listFiles()))
      .filter(f -> !f.getName().equals("Account"))
      .toArray(File[]::new);
    return Arrays.stream(folderDirs)
      .map(f -> {
        Folder f1 = null;
        try {
          f1 = (Folder) deserialize(f.getPath());
        } catch (IOException | ClassNotFoundException e) {
          e.printStackTrace();
        }
        return f1;
      })
      .filter(Objects::nonNull)
      .sorted((f1, f2) -> {
        List<String> orderedNames = List.of(
          "Inbox",
          "Archive",
          "Sent",
          "Drafts",
          "Trash"
        );
        int i1 = orderedNames.indexOf(f1.name());
        int i2 = orderedNames.indexOf(f2.name());
        return Integer.compare(i1, i2);
      })
      .collect(Collectors.toList());
  }

  /**
   * @param folderName the name of the desired folder
   * @return returns a list of emails for the specific folder
   * @throws IOException            If there are any problems when locating the file
   * @throws ClassNotFoundException Of the classes required is not on the classpath?
   * @author David Zamanian
   * <p>
   * Creates a path for the specific OS down to the folderName. The path is then deserialized and cased to Folder
   * and returns the emails in that folder.
   */

  @Override
  public List<Email> retrieveEmails(Account account, String folderName) throws IOException, ClassNotFoundException {
    String path = appPath + separator + account.emailAddress() + separator + folderName + separator + "EmailListObject";
    return (List<Email>) deserialize(path);
  }

  @Override
  public List<Account> retrieveAllAccounts() {
    File[] accountDirs = (new File(appPath)).listFiles();
    List<Account> accounts = new ArrayList<>();
    if (accountDirs != null) {
      accounts = Arrays.stream(accountDirs)
        .map(f -> {
          Account a = null;
          try {
            a = (Account) deserialize(f.getPath() + separator + "Account");
          } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
          }
          return a;
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

  private void mkdir(String path) throws IOException {
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