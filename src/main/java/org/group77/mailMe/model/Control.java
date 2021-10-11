package org.group77.mailMe.model;

import org.group77.mailMe.model.data.Account;
import org.group77.mailMe.model.data.Email;
import org.group77.mailMe.model.data.Folder;
import org.group77.mailMe.services.emailServiceProvider.EmailServiceProvider;
import org.group77.mailMe.services.emailServiceProvider.EmailServiceProviderFactory;
import org.group77.mailMe.services.storage.Storage;

import javax.mail.MessagingException;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Control {

    // services
    private final Storage storage;

    // model
    private newModel model;

    public Control(Storage storage) {
        this.storage = storage;

        // retrieve data from storage and give to model
        Map<Account, List<Folder>> accountsData = new HashMap<>();
        for (Account account : storage.retrieveAccounts()) {
            List<Folder> folders = storage.retrieveFolders(account);
            accountsData.put(account,folders);

        }
        this.model = new newModel(accountsData);

    }

    public void refresh() throws Exception {
        // 1) check that there is an active account and folders is not empty in model
        // 2a) if true:
        //      a) get ESP for model's activeAccount
        //      b) send activeAccount to refreshFromServer, will receive List<Emails> INBOX
        //      c) add the new emails to model's inbox folder (TODO: refreshFolder in model)
        //      d) add the new emails to storage
        //
        // 2b) if not true:
        //      a) throw exception that there is no active account

        if (model.getActiveAccount() != null && model.getActiveFolders() != null) {

            EmailServiceProvider esp = EmailServiceProviderFactory.getEmailServiceProvider(model.getActiveAccount());
            // receive new emails from server
            List<Email> newEmails = esp.refreshFromServer(model.getActiveAccount());

            // add new emails to inbox in model
            try {
                model.updateInbox(newEmails);
                storage.store(model.getActiveAccount(),model.getActiveFolders()); // replaces old inbox with new emails
            } catch (Exception e){
                throw new Exception("No folder named inbox");
            }

        } else {
            throw new Exception("No active account");
        }


    }

    public void addAccount(String emailAddress, String password) throws Exception {

        // 1) try to create account with addAccount method in model or use AccountFactory directly? (TODO: addAccount in model)
        // 2a) if successful:
        //      a) get ESP for added account
        //      b) esp.testConnection(account)
        //          if connection successful:
        //              i) store account (throws an exception?)
        //              ii) add account in model's accounts (TODO: method that adds account to accounts attribute)
        //          if connection not successful:
        //              i) throw authentication exception
        // 2b) if not successful:
        //      a) throw domain not supported exception

        Account tempAccount = AccountFactory.createAccount(emailAddress,password.toCharArray());

        if (tempAccount != null) {
            storage.store(tempAccount); // throws authentication exception
            model.addAccount(tempAccount); // will set tempAccount to activeAccount

        } else {
            throw new Exception("Domain is not supported");
        }

    }

    public void send(List<String> recipients, String subject, String content, List<File> attachments) throws Exception {
        // 1) check that there is an active account in model
        // 2a) if true:
        //      a) get active account ESP
        //      b) esp.sendEmail(recipients,subject,content,attachment)
        // 2b) if not true:
        //      a) throw exception: no active account
        if (model.getActiveAccount() != null) {

            EmailServiceProvider esp = EmailServiceProviderFactory.getEmailServiceProvider(model.getActiveAccount());
            esp.sendEmail(model.getActiveAccount(),recipients,subject,content,attachments);

        } else {
            throw new Exception("No active account");
        }
    }

    public void deleteEmail(Email emailToBeDeleted) {
        // use method in model like: model.deleteEmail(emailToBeDeleted)
        // model returns new folder structure, store new copy in storage
    }


    public Account getActiveAccount() {
        return model.getActiveAccount();
    }

    public List<Account> getAccounts() {
        return model.getAccounts();
    }

    public void setActiveAccount(Account account) {
        model.setActiveAccount(account);
    }

    public List<Folder> getActiveFolders() {
        return model.getActiveFolders();
    }

}
