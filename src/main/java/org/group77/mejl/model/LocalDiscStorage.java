package org.group77.mejl.model;

import javax.mail.Address;
import java.util.List;
import java.io.*;

public class LocalDiscStorage implements Storage {

    String appPath;
    String separator;
    
    // Storage interface methods

    /*@author Alexey Ryabov */

    public LocalDiscStorage() throws OSNotFoundException {
        appPath = OSHandler.getAppDirAndSeparator()[0];
        separator = OSHandler.getAppDirAndSeparator()[1];

    }

    // TODO This method return value doesn't make sense..
    /**
     * @author Alexey Ryabov
     * @param account - keeps email address of account object.
     * @return - Needs more work.
     * @throws Exception
     */
    public boolean store(Account account) throws Exception {
        try {
            String address = account.getEmailAddress();
            if (!testExists(appPath + address + separator)) {
                mkdir(address);
                touch(appPath + address + separator);
                serialize(account, appPath + address + separator);
                return true;
            } else {
                return true;
            }
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
    };
    public List<String> retrieveAllEmailAddresses() {return null;};

    /**
     * @author Alexey Ryabov
     * Method will create a path or return false is path already exists.
     * @param emailAddress - path of the email address.
     * @return false.
     * @throws Exception
     */
    private boolean testExists(String emailAddress) throws Exception {
        try {
            File file = new File(emailAddress);
        } catch (Exception e) {throw new Exception("Failed in LocalDiskStorage -> testExists -method !");}
        return false;
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
    
    
    
}
