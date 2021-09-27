package org.group77.mejl.model;

import java.util.ArrayList;
import java.util.List;
import java.io.*;

public class LocalDiscStorage implements Storage {

    String appPath;
    String separator;
    
    // Storage interface methods
    public LocalDiscStorage() throws OSNotFoundException {
        String[] appDirAndSep = OSHandler.getAppDirAndSeparator();
        appPath = appDirAndSep[0];
        separator = appDirAndSep[1];
    }

    // TODO This method return value doesn't make sense..
    //  ^^ Who wrote this? (hampus)
    /**
     * @author Alexey Ryabov. Revised by Hampus Jernkrook
     * @param account - keeps email address of account object.
     * @return - Needs more work.
     * @throws Exception
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
        } catch (Exception e) {throw new Exception("Failed in LocalDiskStorage -> store -method !");}

    }


    public boolean store(String emailAddress, List<Folder> folders) {return false;};
    public Account retrieveAccount(String emailAddress) {return null;};
    public List<Folder> retrieveFolders(String emailAddress) {return null;};

    /**
     * @author David Zamanian
     *
     * Creates a path for the specific OS down to the folderName. The path is then deserialized and cased to Folder
     * and returns the emails in that folder.
     *
     * @param emailAddress the emailAddress of the active account
     * @param folderName the name of the desired folder
     * @return returns a list of emails for the specific folder
     * @throws IOException If there are any problems when locating the file
     * @throws ClassNotFoundException Of the classes required is not on the classpath?
     */

    public List<Email> retrieveEmails(String emailAddress, String folderName) throws IOException, ClassNotFoundException {

        String path = appPath + separator + emailAddress + separator + folderName;
        Folder folder = (Folder) deserialize(path);
        return folder.getEmails();
    }

    /**
     * @author Hampus Jernkrook
     *
     * Get all email addresses added by the user.
     *
     * @return A list of email addresses.
     */
    public List<String> retrieveAllEmailAddresses() {
        // Find all directories at level 0 under appPath.
        // These correspond to each emailAddress added by the user.
        return getDirSuffix(getDirsAtLevel(new File(appPath), 0));
    }

    /**
     * @author Alexey Ryabov. Revised by Hampus Jernkrook
     * Method will create a path or return false is path already exists.
     * @param emailAddress - path of the email address.
     * @return false.
     * @throws Exception
     */
    private boolean testExists(String emailAddress) throws Exception {
        try {
            return (new File(emailAddress).exists());
        } catch (Exception e) {throw new Exception("Failed in LocalDiskStorage -> testExists -method !");}
    }
    
    private void touch(String path) throws IOException {
        File file = new File(path);
        file.createNewFile();
    }

    private void mkdir(String path) throws IOException {
        File file = new File(path);
        file.mkdir();
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
     * @author Hampus Jernkrook
     *
     * Finds all directories at a certain level from a parent path.
     * @param parent - the File encoding of the parent path to search under.
     * @param level - the level to search down to. level == 0 returns the first children of the parent.
     * @return A list of paths to the directories at the specified level.
     *
     * Method inspiration from
     * https://stackoverflow.com/questions/41344236/java-how-to-get-only-subdirectories-name-present-at-certain-level
     * Cred to user: krzydyn
     */
    private List<String> getDirsAtLevel(File parent, int level){
        List<String> dirs = new ArrayList<>();
        File[] files = parent.listFiles();
        if (files == null) return dirs; // empty dir
        for (File f : files){
            if (f.isDirectory()) {
                if (level == 0) dirs.add(f.getPath());
                else if (level > 0) dirs.addAll(getDirsAtLevel(f,level-1));
            }
        }
        return dirs;
    }

    /**
     * @author Hampus Jernkrook
     *
     * Get only the directory names from a list of directory paths,
     * i.e. get only the last suffix after the separator.
     * @param dirs - List of directory paths to cut off the prefixes from.
     * @return A list of directory names.
     */
    private List<String> getDirSuffix(List<String> dirs) {
        List<String> suffixes = new ArrayList<>();
        for (String path : dirs) {
            suffixes.add(path.substring(path.lastIndexOf(separator) + 1));
        }
        return suffixes;
    }


    
    
    
}
