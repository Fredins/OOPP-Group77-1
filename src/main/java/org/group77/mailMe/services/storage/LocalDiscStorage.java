package org.group77.mailMe.services.storage;

import org.group77.mailMe.model.data.*;

import java.io.*;
import java.util.*;
import java.util.stream.*;

/**
 * Class for storing application files on the user's machine.
 */
public class LocalDiscStorage implements Storage {

    private final String appPath; //application's root directory
    private final String separator; //separator symbol of the user's OS.

    /**
     * Initializes the storage object and sets the app root directory and separator.
     *
     * @throws OSNotFoundException if the user's OS cannot be found.
     * @author David Zamanian
     * @author Hampus Jernkrook
     */
    public LocalDiscStorage() throws OSNotFoundException {
        // Depending on OS, get the appropriate app directory and separator.
        String[] appDirAndSep = OSHandler.getAppDirAndSeparator();
        appPath = appDirAndSep[0]; // TODO perhaps replace with Pair<String, String>
        separator = appDirAndSep[1];
        this.mkdir(appPath); //create the
    }

    /**
     * @param account - The account to be stored.
     * @throws AccountAlreadyExistsException, IOException
     * @author Alexey Ryabov
     * @author Hampus Jernkrook
     */
    @Override
    public void store(Account account) throws AccountAlreadyExistsException, IOException {
        String address = account.emailAddress();
        // store under appPath/emailAddress.
        String path = appPath + separator + address;
        // if account already exists, then abort and inform caller.
        // else if account is not already added, create directory and store file with account details
        if (testExists(path)) {
            throw new AccountAlreadyExistsException("Account already exists!");
        } else {
            mkdir(path);
            // path of the account file
            String accountFilePath = path + separator + "Account";
            touch(accountFilePath);
            serialize(account, accountFilePath);
        }
    }

    /**
     * @param folders - List of folders to store away.
     * @param account - the account to store the folders under.
     * @author Hampus Jernkrook
     * @author Martin Fredin.
     * TODO: get rid of try-catch and propagate exception to control.
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
     * store folder in corresponding account directory
     *
     * @author Elin Hagman, Martin
     */
    @Override
    public void store(Account account, Folder folder) throws IOException {
        String folderPath = appPath + separator + account.emailAddress() + separator + folder.name();
        touch(folderPath);
        serialize(folder, folderPath);
    }

    /**
     * store list of email suggestions proposed when writing
     *
     * @author David Zamanian
     */
    @Override
    public void store(Account account, String suggestion) throws Exception {
        String address = account.emailAddress();
        String dirPath = appPath + separator + address + separator + "Suggestions";

        if ( 1 != 1){ //TODO Create a testExists for this
            throw new Exception();
        } else {
            mkdir(dirPath);
            String suggestionPath = dirPath + separator + "Suggestion";
            touch(suggestionPath);
            serialize(suggestion, suggestionPath);
        }
    }

    /**
     * Retrieve the list of suggestions for a specific account
     *
     * @author David Zamanian.
     * @return
     */
    @Override
    public List<String> retrieveSuggestions(Account account) {
        // get all account directories under the app root directory
        String accountPath = appPath + separator + account.emailAddress();
        File[] accountsDir = Arrays.stream(Objects.requireNonNull((new File(accountPath)).listFiles())).filter(file -> file.getName().equals("Suggestions"))
                .toArray(File[]::new);
        List<String> suggestions;
        // if there were any account directories, then unpack the account objects and
        // add to resulting list.
            suggestions = Arrays.stream(accountsDir)
                    .map(f -> {
                        String suggestion = null;
                        try {
                            // unpack the account object under AppDir/*emailAddress*/Account
                            suggestion = (String) deserialize(f.getPath() + separator + "Suggestion");
                        } catch (IOException | ClassNotFoundException e) {
                            e.printStackTrace(); // TODO propagate
                        }
                        return suggestion;
                    })
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());

        return suggestions;
    }


    /**
     * 1. retrieve all folders
     * 2. sort folders
     *
     * @param account - the account to retrieve folders for.
     * @author David Zamanian
     * @author Martin Fredin
     * @author Hampus Jernkrook
     */
    @Override
    public List<Folder> retrieveFolders(Account account) {
        // path to account directory
        String accountPath = appPath + separator + account.emailAddress();
        // get all files under the account directory (all folders).
        File[] files = Arrays.stream(Objects.requireNonNull((new File(accountPath)).listFiles()))
                .filter(file -> !file.getName().equals("Account") && !file.getName().equals("Suggestions"))
                .toArray(File[]::new);
        // returned the folders sorted by inbox,archive,sent,drafts,trash
        return Arrays.stream(files)
                .map(file -> {
                    Folder folder = null;
                    try {
                        //unpack the stored folder object
                        folder = (Folder) deserialize(file.getPath());
                    } catch (IOException | ClassNotFoundException e) {
                        e.printStackTrace(); //TODO propagate exception to control instead?
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
     * retrieve all saved accounts
     *
     * @return a list of all stored accounts.
     * @author Martin (updated according to new design).
     * @author David Zamanian.
     */
    @Override
    public List<Account> retrieveAccounts() {
        // get all account directories under the app root directory
        File[] accountDirs = (new File(appPath)).listFiles();
        List<Account> accounts = new ArrayList<>();
        // if there were any account directories, then unpack the account objects and
        // add to resulting list.
        if (accountDirs != null) {
            accounts = Arrays.stream(accountDirs)
                    .map(f -> {
                        Account account = null;
                        try {
                            // unpack the account object under AppDir/*emailAddress*/Account
                            account = (Account) deserialize(f.getPath() + separator + "Account");
                        } catch (IOException | ClassNotFoundException e) {
                            e.printStackTrace(); // TODO propagate
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
     * @param path the path to the file including filename
     * @throws IOException if the file is not found
     * @author Hampus Jernkrook
     * @author Martin
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
     * @author Martin
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
     * @author Martin
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
     * @throws IOException if the file don't exist
     * @throws ClassNotFoundException if the file can't be deserialized in to Object
     * @author Martin
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