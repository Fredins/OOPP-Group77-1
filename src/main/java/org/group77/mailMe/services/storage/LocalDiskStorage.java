package org.group77.mailMe.services.storage;

import org.group77.mailMe.model.Account;
import org.group77.mailMe.model.Folder;

import java.io.*;
import java.util.*;
import java.util.stream.*;

/**
 * Class for storing application files on the user's machine.
 */
public class LocalDiskStorage implements Storage {

    /**
     * The path to the applications root directory.
     */
    private final String appPath;
    /**
     * The separator symbol of the Operating System being used.
     */
    private final String separator;

    /**
     * Creates a LocalDiskStorage object and sets the app root directory and separator.
     *
     * @throws OSNotFoundException if the user's OS cannot be found.
     * @author David Zamanian
     * @author Hampus Jernkrook
     */
    public LocalDiskStorage() throws OSNotFoundException {
        // Depending on OS, get the appropriate app directory and separator.
        String[] appDirAndSep = OSHandler.getAppDirAndSeparator();
        appPath = appDirAndSep[0];
        separator = appDirAndSep[1];
        this.mkdir(appPath);
    }

    /**
     * Stores an account in a directory with the email address as name in the app directory.
     *
     * Firstly the directory with the email address is created, then the specified account
     * is serialized in that directory.
     *
     * @param account - The account to be stored.
     * @throws StorageException if account already exists or problem with IO
     * @author Alexey Ryabov
     * @author Hampus Jernkrook
     */
    @Override
    public void store(Account account) throws StorageException {
        String address = account.emailAddress();
        // store under appPath/emailAddress.
        String path = appPath + separator + address;
        // if account already exists, then abort and inform caller.
        // else if account is not already added, create directory and store file with account details
        if (testExists(path)) {
            throw new AccountAlreadyStoredException("Account already exists!");
        } else {
            mkdir(path);
            // path of the account file
            String accountFilePath = path + separator + "Account";
            try {
                touch(accountFilePath);
                serialize(account, accountFilePath);
            } catch (IOException e) {
                throw new StorageException(e);
            }
        }
    }

    /**
     * Stores the specified list of folders in the account directory of the specified account.
     *
     * @param account - the account to store the folders under
     * @param folders - List of folders to store
     * @throws StorageException the folders cannot be stored
     * @author Hampus Jernkrook
     * @author Martin Fredin.
     */
    @Override
    public void store(Account account, List<Folder> folders) throws StorageException {
        // path to the given account's directory
        String accountPath = appPath + separator + account.emailAddress();
        // For each folder, create a directory with the folder name and store the folder object
        folders.forEach(folder -> {
            String folderPath = accountPath + separator + folder.name();
            try {
                touch(folderPath);
                serialize(folder, folderPath);
            } catch (IOException e) {
                throw new StorageException(e);
            }
        });
    }

    /**
     * Store the specified folder in corresponding account directory
     * @param account the account which the folder should be stored under
     * @param folder the folder to be stored
     * @author Elin Hagman
     * @author Martin Fredin
     */
    @Override
    public void store(Account account, Folder folder) throws StorageException {
        String folderPath = appPath + separator + account.emailAddress() + separator + folder.name();
        try {
            touch(folderPath);
            serialize(folder, folderPath);
        } catch (IOException e) {
            throw new StorageException(e);
        }
    }

    /**
     * Store list of email addresses which the specified account has sent emails to
     *
     * @param account the account which recipients should be stored
     * @param suggestions the email addresses of the recipients
     * @author David Zamanian
     */
    @Override
    public void storeKnownRecipients(Account account, List<String> suggestions) throws StorageException {
        String address = account.emailAddress();
        String path = appPath + separator + address + separator + "Suggestions";

        try {
            touch(path);
            serialize(suggestions, path);
        } catch (IOException e) {
            throw new StorageException(e);
        }
    }

    /**
     * Retrieve the list of email addresses that the specified account has had as
     * recipients.
     *
     * @param account the account of which recipients is to be retrieved
     * @return Returns a list of all the auto-complete suggestions
     * @author David Zamanian
     */
    @Override
    public List<String> retrieveKnownRecipients(Account account) {
        // get all account directories under the app root directory
        String accountPath = appPath + separator + account.emailAddress();
        String path = accountPath + separator + "Suggestions";
        List<String> suggestions = new ArrayList<>();
        try {
            //Unpack the stored Suggestion object
            suggestions = (List<String>) deserialize(path);
        } catch (IOException | ClassNotFoundException ignore) {
        }
        return suggestions;
    }


    /**
     * Retrieve the list of stored folders of the specified account.
     *
     * @param account the account to retrieve folders for
     * @return list of folders belonging to the specified account
     * @author David Zamanian
     * @author Martin Fredin
     * @author Hampus Jernkrook
     */
    @Override
    public List<Folder> retrieveFolders(Account account) throws StorageException {
        // path to account directory
        String accountPath = appPath + separator + account.emailAddress();
        // get all files under the account directory (all folders) except "Account" and "Suggestions".
        File[] files = Arrays.stream(Objects.requireNonNull((new File(accountPath)).listFiles()))
                .filter(file -> !file.getName().equals("Account") && !file.getName().equals("Suggestions"))
                .toArray(File[]::new);
        // returned the folders sorted by inbox,archive,sent,drafts,trash
        return Arrays.stream(files)
                .map(file -> {
                    Folder folder;
                    try {
                        //unpack the stored folder object
                        folder = (Folder) deserialize(file.getPath());
                    } catch (IOException | ClassNotFoundException e) {
                        throw new StorageException(e);
                    }
                    return folder;
                })
                .filter(Objects::nonNull) //get all folders that are not null.
                .sorted((folder1, folder2) -> { //sort in the correct order.
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
                .collect(Collectors.toList()); //collect the results and return.
    }

    /**
     * Retrieve a list of all the stored accounts in app directory
     *
     * @return a list of all stored accounts
     * @author Martin (updated according to new design).
     * @author David Zamanian.
     */
    @Override
    public List<Account> retrieveAccounts() throws StorageException {
        // get all account directories under the app root directory
        File[] accountDirs = (new File(appPath)).listFiles();
        List<Account> accounts = new ArrayList<>();
        // if there were any account directories, then unpack the account objects and
        // add to resulting list.
        if (accountDirs != null) {
            accounts = Arrays.stream(accountDirs)
                    .map(f -> {
                        Account account;
                        try {
                            // unpack the account object under AppDir/*emailAddress*/Account
                            account = (Account) deserialize(f.getPath() + separator + "Account");
                        } catch (IOException | ClassNotFoundException e) {
                            throw new StorageException(e);
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
     * @return true iff the given path exists on the user's machine.
     * @author Alexey Ryabov
     * @author Hampus Jernkrook
     * Tell whether a file/directory exists with the given path.
     */
    private boolean testExists(String path) {
        return (new File(path).exists());
    }

    /**
     * Create new file with the specified path
     * 
     * @param path the path to the file including filename
     * @throws IOException if the file is not found
     * @author Hampus Jernkrook
     * @author Martin Fredin
     */
    private void touch(String path) throws IOException {
        File file = new File(path);
        // if the file already exists, overwrite it.
        if (file.exists()) {
            file.delete(); // first delete the existing
        }
        file.createNewFile(); //Create new file.
    }

    /**
     * @param path the path to directory including directory name
     * @author Martin Fredin
     */
    private void mkdir(String path) {
        File file = new File(path);
        file.mkdir();
    }

    /**
     * serialize java object to specified location on computer
     *
     * @param o    the object to be serialized
     * @param path the location on the computer including filename
     * @throws IOException if the object can't be serialized
     * @author Martin Fredin
     */
    private void serialize(Object o, String path) throws IOException {
        FileOutputStream file = new FileOutputStream(path);
        ObjectOutputStream out = new ObjectOutputStream(file);
        out.writeObject(o);
        out.close();
        file.close();
    }

    /**
     * deserialze file on computer to java object
     *
     * @param path the path of the file including filename
     * @return the object, uncasted
     * @throws IOException            if the file don't exist
     * @throws ClassNotFoundException if the file can't be deserialized in to Object
     * @author Martin Fredin
     */
    private Object deserialize(String path) throws IOException, ClassNotFoundException {
        FileInputStream file = new FileInputStream(path);
        ObjectInputStream in = new ObjectInputStream(file);
        Object o = in.readObject();
        in.close();
        file.close();
        return o;
    }
}