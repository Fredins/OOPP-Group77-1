package org.group77.mejl.model;

import javax.mail.Address;
import java.util.List;
import java.io.*;

public class LocalDiscStorage implements Storage {

    String appPath;
    String separator;
    
    // Storage interface methods
    /** @author Alexey Ryabov, Martin Fredin */
    public LocalDiscStorage(){
         appPath = OSHandler.getAppPath();
         separator = OSHandler.getSeparator();
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
            String adress = account.getEmailAddress();
            if (!testExists(appPath + adress + separator)) {
                mkdir(adress);
                touch(appPath + adress + separator);
                serialize(account, appPath + adress + separator);
                return false;
            } else {
                return true;
            }
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
