package org.group77.mailMe.model;

import org.group77.mailMe.model.data.Account;
import org.group77.mailMe.model.data.Email;
import org.group77.mailMe.model.data.Folder;
import org.group77.mailMe.services.emailServiceProvider.EmailServiceProvider;
import org.group77.mailMe.services.emailServiceProvider.EmailServiceProviderFactory;
import org.group77.mailMe.services.storage.AccountAlreadyExistsException;
import org.group77.mailMe.services.storage.Storage;

import java.io.File;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Control is a facade between the frontend and backend.
 * It also separates the model from the services used in the application.
 *
 * @author Elin Hagman
 */

public class Control {

    /**
     * The storage solution.
     */
    private final Storage storage;

    /**
     * The model which holds the logic of the application and its state.
     */
    private final Model model;

    /**
     * Creates a Control with the specified storage solution.
     *
     * Creates and initiates a Model with necessary data from storage.
     * Adds logic to model so that when active account changes its folders are retrieved
     * from storage.
     *
     * @param storage storage solution used for storing user data.
     *
     * @author Elin Hagman, Martin Fredin
     */

    public Control(Storage storage) {
        this.storage = storage;

        this.model = new Model(storage.retrieveAccounts()); //TODO: add as parameter?
        //model.getAccounts().replaceAll(storage.retrieveAccounts());


        // update folders when active account is changed
        model.getActiveAccount().addObserver(newAccount -> {
            // if a new account is set as active, get the stored folders of that account.
            if (newAccount != null) {
                List<Folder> newFolders = storage.retrieveFolders(model.getActiveAccount().get());
                if (newFolders.isEmpty()) { //if user has no folders stored, create new ones and store.
                    newFolders = model.createFolders();
                    storage.store(model.getActiveAccount().get(), newFolders);
                }
                model.getFolders().replaceAll(newFolders);
            }
        });

        // ALTERNATIVE: send stored data to model directly
        /*
        Map<Account, List<Folder>> accountsData = new HashMap<>();
        for (Account account : storage.retrieveAccounts()) {
            List<Folder> folders = storage.retrieveFolders(account);
            accountsData.put(account,folders);

        }
        this.model = new Model(accountsData);

         */
    }

    /**
     * 1. retrieve emails from server
     * 2. set activeFolder = inbox
     * 3. store emails
     *
     * @throws Exception if inbox doesn't exist or if refreshFromServerFails
     * @author Martin Fredin
     */
    public void refresh() throws Exception {
        if (model.getActiveAccount() != null && model.getFolders() != null) {
            EmailServiceProvider esp = EmailServiceProviderFactory.getEmailServiceProvider(model.getActiveAccount().get());
            // receive new emails from server
            List<Email> newEmails = esp.refreshFromServer(model.getActiveAccount().get());
            // add new emails to inbox in model
            try {
                model.updateInbox(newEmails);
                storage.store(model.getActiveAccount().get(), model.getFolders().get()); // replaces old inbox with new emails
            } catch (InboxNotFoundException e){
                throw new Exception("No folder named Inbox");
            }
        } else {
            throw new Exception("No active account");
        }

    }

    /**
     * Tries to add a new account to accounts.
     * <p>
     * If emailAddress does not belong to a supported domain, throws Exception with informative message.
     * If account cannot connect to server or store account, throws Exception.
     *
     * @throws Exception if domain is not supported or authentication to server fails
     * @author Elin Hagman
     * @author Martin
     * @author Hampus Jernkrook
     */
    public void addAccount(String emailAddress, String password) throws Exception {
        try {
            Account account = model.createAccount(emailAddress, password.toCharArray());
            // test connection
            if (EmailServiceProviderFactory.getEmailServiceProvider(account).testConnection(account)) {
                storage.store(account); // throws account already exists exception
                model.addAccount(account); // will set created account to activeAccount
            }
        } catch (EmailDomainNotSupportedException | AccountAlreadyExistsException | CouldNotConnectToServerException e) {
            throw new Exception(e.getMessage());
        }
    }

    /**
     * sends the email via the appropriate email service provider
     * TODO Alexey kan vi byta till send(Account) och sedan fixa med resten på backend?
     *
     * @author Alexey Ryabov
     */
    public void send(List<String> recipients, String subject, String content, List<File> attachments) throws Exception {
        if (model.getActiveAccount() != null) {
            EmailServiceProvider esp = EmailServiceProviderFactory.getEmailServiceProvider(model.getActiveAccount().get());
            esp.sendEmail(model.getActiveAccount().get(),recipients,subject,content,attachments);

        } else {
            throw new Exception("No active account");
        }
    }

    /**
     * Removes the currently open email and moves it to the trash.
     *
     * @throws Exception
     * @author David Zamanian
     */

    public void deleteEmail() throws Exception {

        List<Folder> newFolders = storage.retrieveFolders(model.getActiveAccount().get());
        //Move the currently open email to the trash
        newFolders.get(4).emails().add(model.getActiveEmail().get()); //TODO Fix better index if we want to add more folders in the future (from 4 to compare name to "Trash" somehow..)
        //Remove currently open email from the activeFolder
        newFolders.get(newFolders.indexOf(model.getActiveFolder().get())).emails().remove(model.getActiveEmail().get());
        storage.store(model.getActiveAccount().get(), newFolders);
        model.getFolders().replaceAll(newFolders);
        refresh();
    }

    /**
     * Used when deleting emails from the trash. Will remove it from all inboxes and will not be able to recover it
     *
     * @throws Exception
     * @author David Zamanian
     */

    public void permDeleteEmail() throws Exception {
        List<Folder> newFolders = storage.retrieveFolders(model.getActiveAccount().get());
        newFolders.get(newFolders.indexOf(model.getActiveFolder().get())).emails().remove(model.getActiveEmail().get());
        storage.store(model.getActiveAccount().get(), newFolders);
        model.getFolders().replaceAll(newFolders);
        refresh();
    }


    /**
     * Moves the email to the desired folder and deletes it from the activeFolder. Choose where to move in the comboBox in the readingView.
     *
     * @param folder The folder that was selected in the "Move" comboBox in readingView
     * @throws Exception
     * @author David Zamanian
     */

    public void moveEmail(Folder folder) throws Exception {
        List<Folder> newFolders = storage.retrieveFolders(model.getActiveAccount().get());
        //Move the currently open email to the chosen folder in the comboBox
        newFolders.get(newFolders.indexOf(folder)).emails().add(model.getActiveEmail().get());
        //Delete the currently open email from the activeFolder
        newFolders.get(newFolders.indexOf(model.getActiveFolder().get())).emails().remove(model.getActiveEmail().get());
        storage.store(model.getActiveAccount().get(), newFolders);
        model.getFolders().replaceAll(newFolders);
        refresh();
    }

    /* public void deleteEmail(Email emailToBeDeleted) {
        // use method in model like: model.deleteEmail(emailToBeDeleted)
        // model returns new folder structure, store new copy in storage
    }
    */

    // state getters and setters
    public Subject<Account> getActiveAccount() { return model.getActiveAccount(); }
    public SubjectList<Account> getAccounts() { return model.getAccounts(); }
    public void setActiveAccount(Account account) { model.setActiveAccount(account); }
    public SubjectList<Folder> getActiveFolders() { return model.getFolders(); }
    public Subject<Folder> getActiveFolder() { return model.getActiveFolder(); }
    public Subject<Email> getReadingEmail() { return model.getActiveEmail(); }
    public SubjectList<Email> getVisibleEmails() { return model.getActiveEmails(); }
    public void setActiveFolder(Folder activeFolder) { model.setActiveFolder(activeFolder); }
    public void setReadingEmail(Email readingEmail) { model.setActiveEmail(readingEmail); }
    public void setVisibleEmails(List<Email> visibleEmails) { model.setActiveEmails(visibleEmails); }


    public void filterOnTo(String searchWord) {
        model.filterOnTo(searchWord);
    }

    public void filterOnFrom(String searchWord) {
        model.filterOnFrom(searchWord);
    }

    public void filterOnMaxDate(LocalDateTime date) {
        model.filterOnMaxDate(date);
    }

    public void clearFilter() {
        model.clearFilter();
    }

}
