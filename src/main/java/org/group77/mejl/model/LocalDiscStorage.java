package org.group77.mejl.model;

import javax.mail.Address;
import java.util.List;

public class LocalDiscStorage implements Storage {

    OSHandler osHandler;
    String appPath;
    String separator;
    
    // Storage interface methods
    /*@author Alexey Ryabov */
    public boolean store(Account account) throws Exception {
        try {
            String adress = account.getEmailAddress();
            testExists(adress);
            return true;
        } catch (Exception e) {throw new Exception("Failed in LocalDiskStorage -> store -method !");}

    }


    public boolean store(String emailAddress, List<Folder> folders) {return false;};
    public Account retrieveAccount(String emailAddress) {return null;};
    public List<Folder> retrieveFolders(String emailAddress) {return null;};
    public List<Email> retrieveEmails(String emailAddress, String folderName) {return null;};
    public List<String> retrieveAllEmailAddresses() {return null;};
    
    private boolean testExists(String emailAddress) {
        return false;
    }
    
    private boolean mkdir(String path) {
        return false;
    }
    
    private boolean touch (String path) {
        return false;
    }
    
    private boolean serialize (Object object, String path) {
        return false; 
    }
    
    private Object deserialize(String path) {
        return null;
    }
    
    
    
}
