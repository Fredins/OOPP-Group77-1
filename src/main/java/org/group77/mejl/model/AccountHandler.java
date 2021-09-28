package org.group77.mejl.model;
import com.sun.mail.smtp.SMTPOutputStream;

import java.io.IOException;
import java.util.*;
public class AccountHandler {
/*
* @author
* */
    Account activeAccount;
    Storage storage;
    AccountFactory accountFactory;

    public AccountHandler() throws OSNotFoundException, IOException {
        this.storage = new LocalDiscStorage();
        this.accountFactory = new AccountFactory();
    }

    /** @author Alexey Ryabov
    * TODO Test this method */
    public boolean storeAccount(Account account) throws Exception {
        try {
            storage.store(account);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
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

    /**
     * @author David Zamanian
     *
     * calls retrieveEmails in storage with the email address of the active account and the foldername
     *
     * @param folderName the name of the desired folder
     * @return
     * @throws OSNotFoundException If the operating system is not found
     * @throws IOException If there are any problems when locating the file
     * @throws ClassNotFoundException Of the classes required is not on the classpath?
     */

    public List<Email> getEmails (String folderName) throws OSNotFoundException, IOException, ClassNotFoundException {
        storage.retrieveEmails(getActiveAccount().getEmailAddress(), folderName);
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
