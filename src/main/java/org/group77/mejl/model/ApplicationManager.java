package org.group77.mejl.model;
import java.util.*;

public class ApplicationManager {

    AccountHandler accountHandler;
    EmailServiceProviderFactory espFactory;

    ApplicationManager() {}

    public boolean addAccount(String emailAddress, String password) {
        return false;
    }

    public boolean setActiveAccount(String emailAddress) {
        return false;
    }

    public List<String> getEmailAddresses() {
        return null;
    }

    public List<Email> getEmails(String folderName) {
        return null;
    }

    public boolean sendEmail(List<String> recipients, String subject, String content) {
        return false;
    }

    public List<Folder> refreshFromServer() {
        return null;
    }


}
