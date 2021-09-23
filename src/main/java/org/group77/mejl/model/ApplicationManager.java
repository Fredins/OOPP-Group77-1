package org.group77.mejl.model;
import java.util.*;

public class ApplicationManager {

    AccountHandler accountHandler;
    EmailServiceProviderFactory espFactory;

    ApplicationManager() {}

    /**
     * @author Elin Hagman
     *
     * Creates an Account and tries to store it.
     * Only stores the account if it connects to the server and it is not already stored.
     *
     * @param emailAddress the Account's emailAddress
     * @param password the Account's password
     *
     * @return boolean, true if account was successfully stored,
     * false if account could not be stored.
    * */


    public boolean addAccount(String emailAddress, String password) throws Exception {

        Account account = accountHandler.createAccount(emailAddress,password);
        EmailServiceProviderStrategy espStrategy = espFactory.getEmailServiceProvider(account);

        if (espStrategy.testConnection(account)) {

            return accountHandler.storeAccount(account);

        } else {
            return false;
        }

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