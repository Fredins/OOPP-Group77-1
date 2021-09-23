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


    public boolean store(Account account) throws Exception {
        try {
            String adress = account.getEmailAddress();
            if (testExists(appPath + adress)) {
                return true;
            } else {
                mkdir(adress);
                touch(appPath + adress);
                serialize(account, appPath + adress);

            }
        } catch (Exception e) {throw new Exception("Failed in LocalDiskStorage -> store -method !");}

        return true;
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
    
    private boolean testExists(String emailAddress) {
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
