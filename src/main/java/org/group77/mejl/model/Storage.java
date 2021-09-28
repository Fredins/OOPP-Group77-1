package org.group77.mejl.model;
import java.io.IOException;
import java.util.*;

public interface Storage {
    
    public boolean store(Account account) throws Exception;
    public boolean store(String emailAddress, List<Folder> folders) throws IOException;
    public Account retrieveAccount(String emailAddress) throws IOException, ClassNotFoundException;
    public List<Folder> retrieveFolders(String emailAddress);
    public List<Email> retrieveEmails(String emailAddress, String folderName) throws IOException, ClassNotFoundException;
    public List<String> retrieveAllEmailAddresses();
}
