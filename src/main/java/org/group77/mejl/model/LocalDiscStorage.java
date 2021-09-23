package org.group77.mejl.model;

import java.util.List;

public class LocalDiscStorage implements Storage {

    OSHandler osHandler;
    String appPath;
    String separator;
    
    // Storage interface methods
    public boolean store(Account account) { return false; };
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
