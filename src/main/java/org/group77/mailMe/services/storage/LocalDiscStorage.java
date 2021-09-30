package org.group77.mailMe.services.storage;

import org.group77.mailMe.model.Account;
import org.group77.mailMe.model.Email;
import org.group77.mailMe.model.Folder;

import java.util.ArrayList;
import java.util.List;
import java.io.*;

public class LocalDiscStorage implements Storage {

    String appPath;
    String separator;

    // Storage interface methods
    public LocalDiscStorage() throws OSNotFoundException, IOException {
        String[] appDirAndSep = OSHandler.getAppDirAndSeparator();
        appPath = appDirAndSep[0];
        separator = appDirAndSep[1];
        this.mkdir(appPath);
    }

    // TODO This method return value doesn't make sense..
    //  ^^ Who wrote this? (hampus)

    /**
     * @param account - keeps email address of account object.
     * @return - Needs more work.
     * @throws Exception
     * @author Alexey Ryabov. Revised by Hampus Jernkrook
     */
    public boolean store(Account account) throws Exception {
        String address = account.getEmailAddress();
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
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * TODO how to write tests for store and retrieve? The tests will be dependent on both...
     *
     * @param emailAddress - the email address of the account to store the data under.
     * @param folders      - List of folders to store away.
     * @return true if the folders could be successfully stored.
     * @throws IOException if the file operations fail.
     * @author Hampus Jernkrook
     */
    public boolean store(String emailAddress, List<Folder> folders) throws IOException {
        // path to the given account's directory
        String path = appPath + separator + emailAddress + separator;
        // TODO if the folder directories already exists, then these should be overwritten...
        //  will they be overwritten now?
        // For each folder, create a directory with the folder name and store the folder object
        for (Folder folder : folders) {
            String folderPath = path + folder.getName();
            String objectPath = folderPath + separator + "EmailListObject";
            // create directory with folder name
            mkdir(folderPath);
            // create a directory for the folder object
            touch(objectPath);
            // go over all emails and store in an arraylist. This is needed to get something serializable.
            ArrayList<Email> emails = new ArrayList<>(folder.getEmails());
            // store the serialized list of emails
            serialize(emails, objectPath);
        }
        return true;
    }

    /**
     * If an account with the given email address exists as a saved object on the user's
     * machine, find and return the account object.
     *
     * @param emailAddress - the email address of the account that should be retrieved.
     * @return The Account with the given email address.
     * @throws IOException
     * @throws ClassNotFoundException
     * @author Hampus Jenrkrook
     */
    public Account retrieveAccount(String emailAddress) throws IOException, ClassNotFoundException {
        // retrieve the account at path "appPath/emailAddress" and unpack to Account object
        Account account = (Account) deserialize(appPath + separator + emailAddress + separator + "Account"); //TODO set the account object name somewhere else
        return account;
    }

    public List<Folder> retrieveFolders(String emailAddress) {
        return null;
    }

    /**
     * @param emailAddress the emailAddress of the active account
     * @param folderName   the name of the desired folder
     * @return returns a list of emails for the specific folder
     * @throws IOException            If there are any problems when locating the file
     * @throws ClassNotFoundException Of the classes required is not on the classpath?
     * @author David Zamanian
     * <p>
     * Creates a path for the specific OS down to the folderName. The path is then deserialized and cased to Folder
     * and returns the emails in that folder.
     */

    public List<Email> retrieveEmails(String emailAddress, String folderName) throws IOException, ClassNotFoundException {

        String path = appPath + separator + emailAddress + separator + folderName;
        Folder folder = (Folder) deserialize(path);
        return folder.getEmails();
    }

    /**
     * @return A list of email addresses.
     * @author Hampus Jernkrook
     * <p>
     * Get all email addresses added by the user.
     */
    public List<String> retrieveAllEmailAddresses() {
        // Find all directories at level 0 under appPath.
        // These correspond to each emailAddress added by the user.
        return getDirSuffix(getDirsAtLevel(new File(appPath), 0));
    }

    /**
     * @param path - path of the file/directory to search for.
     * @return false.
     * @throws Exception
     * @author Alexey Ryabov. Revised by Hampus Jernkrook
     * Tell whether a file/directory exists with the given path.
     */
    private boolean testExists(String path) throws Exception {
        try {
            return (new File(path).exists());
        } catch (Exception e) {
            throw new Exception("Failed in LocalDiskStorage -> testExists -method !");
        }
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

    /**
     * @param parent - the File encoding of the parent path to search under.
     * @param level  - the level to search down to. level == 0 returns the first children of the parent.
     * @return A list of paths to the directories at the specified level.
     * <p>
     * Method inspiration from
     * https://stackoverflow.com/questions/41344236/java-how-to-get-only-subdirectories-name-present-at-certain-level
     * Cred to user: krzydyn
     * @author Hampus Jernkrook
     * <p>
     * Finds all directories at a certain level from a parent path.
     */
    private List<String> getDirsAtLevel(File parent, int level) {
        List<String> dirs = new ArrayList<>();
        File[] files = parent.listFiles();
        if (files == null) return dirs; // empty dir
        for (File f : files) {
            if (f.isDirectory()) {
                if (level == 0) dirs.add(f.getPath());
                else if (level > 0) dirs.addAll(getDirsAtLevel(f, level - 1));
            }
        }
        return dirs;
    }

    /**
     * @param dirs - List of directory paths to cut off the prefixes from.
     * @return A list of directory names.
     * @author Hampus Jernkrook
     * <p>
     * Get only the directory names from a list of directory paths,
     * i.e. get only the last suffix after the separator.
     */
    private List<String> getDirSuffix(List<String> dirs) {
        List<String> suffixes = new ArrayList<>();
        for (String path : dirs) {
            suffixes.add(path.substring(path.lastIndexOf(separator) + 1));
        }
        return suffixes;
    }


}