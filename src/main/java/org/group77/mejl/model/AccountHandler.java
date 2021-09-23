package org.group77.mejl.model;
import java.util.*;
public class AccountHandler {
/*
* @author
* */
    Account activeAccount;
    Storage storage;
    AccountFactory accountFactory = new AccountFactory();

    /* @author Alexey Ryabov
    * TODO Test this method */
    public boolean storeAccount(Account account) throws Exception {
        try {
            storage.store(account);
            return true;
        } catch (Exception e) {throw new Exception("Failed in AccountHandler -> storeAccount -method ! ");}
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
