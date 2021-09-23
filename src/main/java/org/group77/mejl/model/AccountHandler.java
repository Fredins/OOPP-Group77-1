package org.group77.mejl.model;
import java.util.*;
public class AccountHandler {

    Account activeAccount;
    Storage storage;
    AccountFactory accountFactory = new AccountFactory();

    public boolean storeAccount(Account account) {
        return false;
    }

    public Account getAccount(String emailAddress) {
        return null;
    }

    public List<String> getEmailAddresses() {
        return null;
    }

    public boolean storeFolders(List<Folder> folders) {
        return false;
    }

    public List<Email> getEmails (String folderName) {
        return null;
    }

    public Account getActiveAccount() {
        return activeAccount;
    }

    public boolean setActiveAccount(String emailAddress) {
        return false;
    }

    public Account createAccount(String emailAddress, String password) {
        return accountFactory.createAccount(emailAddress, password);
    }


}
