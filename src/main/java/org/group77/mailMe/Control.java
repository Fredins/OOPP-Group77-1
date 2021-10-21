package org.group77.mailMe;

import org.group77.mailMe.model.*;
import org.group77.mailMe.services.emailServiceProvider.EmailServiceProvider;
import org.group77.mailMe.services.emailServiceProvider.EmailServiceProviderFactory;
import org.group77.mailMe.services.storage.AccountAlreadyStoredException;
import org.group77.mailMe.services.emailServiceProvider.ServerException;
import org.group77.mailMe.services.storage.Storage;
import org.group77.mailMe.services.storage.StorageException;


import java.time.LocalDateTime;
import java.util.*;

import java.io.File;
import java.util.List;
import java.util.stream.*;


/**
 * Control is a facade to the backend.
 * It also separates the model from the services used in the application.
 * Handles exceptions thrown by model and services.
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
     * <p>
     * Creates and initiates an instance of Model with necessary data from storage.
     * Adds logic to model so that when active account changes its folders are retrieved
     * from storage.
     *
     * @param storage storage solution used for storing user data.
     * @author Elin Hagman
     * @author Martin Fredin
     */

    public Control(Storage storage) {
        this.storage = storage;

        this.model = new Model(storage.retrieveAccounts());

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

        //Updates the knownRecipients as soon as an account gets active
        getActiveAccount().addObserver(i -> {
            if (getActiveAccount().get() != null) {
                model.setKnownRecipients(storage.retrieveKnownRecipients(getActiveAccount().get()));

            }
        });

    }

    /**
     * Adds the recipients email address to the suggestion list in storage by retrieving the old list and add the
     * new email and store them together
     *
     * @param newKnownRecipients the new suggestions
     * @author David Zamanian
     * @author Martin Fredin
     */

    public void addNewKnownRecipient(List<String> newKnownRecipients) {
        try {
            List<String> oldKnownRecipients = storage.retrieveKnownRecipients(getActiveAccount().get()); //Retrieve the old suggestions
            List<String> combinedSuggestions = Stream.concat(oldKnownRecipients.stream(), newKnownRecipients.stream())
                    .distinct()
                    .collect(Collectors.toList()); //Concatenate the oldSuggestion with the newSuggestions
            storage.storeKnownRecipients(getActiveAccount().get(), combinedSuggestions);
            getKnownRecipients().replaceAll(combinedSuggestions); //Replace the oldSuggestions with the new
        } catch (StorageException e) {
            e.printStackTrace();
        }
    }

    /**
     * Removes brackets from a string. Used to remove "[" and "]" from recipients.
     *
     * @param s The string to remove brackets from
     * @author David Zamanian
     */

    public String removeBrackets(String s) {
        s = s.replaceAll("[\\[\\](){}]", "");
        return s;
    }


    /**
     * Retrieves and returns new emails from server.
     *
     * @return New emails retrieved from server
     * @throws ServerException TODO: Control should only throw Exceptions?
     * @author Martin Fredin
     */
    public List<Email> refresh() throws ServerException {
        List<Email> emails = new ArrayList<>();
        if (model.getActiveAccount() != null && model.getFolders() != null) {
            EmailServiceProvider esp = EmailServiceProviderFactory.createEmailServiceProvider(model.getActiveAccount().get());
            emails = esp.refreshFromServer(model.getActiveAccount().get());
        } //TODO: what happens if activeAccount or Folders is null?
        return emails;
    }

    /**
     * Adds the specified emails to the specified folder in this model.
     *
     * @param folderName the name of the folder that is to be updated
     * @param newEmails  the emails the specified folder should be updated with
     * @throws FolderNotFoundException // TODO: Should this be exception instead? The exception should be thrown by model
     * @author Martin Fredin
     */
    public void updateFolder(String folderName, List<Email> newEmails) throws FolderNotFoundException {
        Folder folder = model.getFolders().stream()
                .filter(folder1 -> folder1.name().equals(folderName))
                .findFirst()
                .orElseThrow(() -> new FolderNotFoundException(folderName));

        model.updateFolder(folder, newEmails);
        storage.store(model.getActiveAccount().get(), folder);
        storage.store(model.getActiveAccount().get(), model.getFolders().get()); // replaces old inbox with new emails

    }


    /**
     * Tries to add a new account to this model's accounts.
     * <p>
     * If emailAddress does not belong to a supported domain, throws Exception with informative message.
     * If account cannot connect to server or store account, throws Exception with informative message.
     *
     * @throws Exception if domain is not supported, authentication to server fails or if account already exists.
     * @author Elin Hagman
     * @author Martin Fredin
     * @author Hampus Jernkrook
     */
    public void addAccount(String emailAddress, String password) throws Exception {
        try {
            Account account = AccountFactory.createAccount(emailAddress, password.toCharArray());
            // test connection
            if (EmailServiceProviderFactory.createEmailServiceProvider(account).testConnection(account)) {
                storage.store(account); // throws account already exists exception
                model.addAccount(account); // will set created account to activeAccount
            }
        } catch (EmailDomainNotSupportedException | AccountAlreadyStoredException | ServerException e) {
            throw new Exception(e.getMessage());
        }
    }


    /**
     * finds the right email service provider and sends the email
     * @param email email to be sent
     * @throws Exception if there is no active account in this model
     * @author Alexey Ryabov, Martin Fredin
     */
    public void send(Email email) throws Exception {
        Account account = model.getActiveAccount().get();
        if (account != null) {
            EmailServiceProviderFactory
              .createEmailServiceProvider(account)
              .sendEmail(account, email);
        } else {
            throw new Exception("No active account");
        }
    }

    /**
     * Moves this model's activeEmail to the trash folder.
     *
     * @author David Zamanian
     * @author Martin Fredin
     */
    public void deleteEmail() {
        Optional<Folder> maybeTrash = getFolders().stream()
                .filter(folder -> folder.name().equals("Trash"))
                .findFirst();
        maybeTrash.ifPresent(this::moveEmail);

    }

    /**
     * Used when deleting emails from the trash. Will remove it from all inboxes and will not be able to recover it
     *
     * @author David Zamanian
     * @author Martin Fredin
     */

    public void permDeleteEmail() {
        model.permDeleteEmail();
        storage.store(getActiveAccount().get(), model.getActiveFolder().get());
    }


    /**
     * Moves the email to the desired newFolder and deletes it from the activeFolder.
     *
     * @param newFolder the folder this model's activeEmail should be moved to
     * @author David Zamanian
     * @author Martin Fredin
     */

    public void moveEmail(Folder newFolder) {
        model.moveActiveEmail(newFolder);
        storage.store(getActiveAccount().get(), model.getActiveFolder().get());
        storage.store(getActiveAccount().get(), newFolder);
    }

    /**
     * find sent folder, add email, save to storage.
     * @param email the newly sent email
     * @author Martin Fredin
     */
    public void addEmailToSent(Email email) {
        Folder sent = model.getFolders().stream()
          .filter(folder -> folder.name().equals("Sent"))
          .findFirst()
          .orElse(new Folder("Sent", new ArrayList<>()));
        sent.addEmail(email);
        storage.store(model.getActiveAccount().get(), sent);
    }

    /* public void deleteEmail(Email emailToBeDeleted) {
        // use method in model like: model.deleteEmail(emailToBeDeleted)
        // model returns new folder structure, store new copy in storage
    }
    */

    // state getters and setters

    public void setReadingEmail(Email readingEmail) {
        model.setActiveEmail(null);
        model.setActiveEmail(readingEmail);
    }

    public SubjectList<String> getKnownRecipients() {
        return model.getKnownRecipients();
    }

    public Subject<Account> getActiveAccount() {
        return model.getActiveAccount();
    }

    public SubjectList<Account> getAccounts() {
        return model.getAccounts();
    }

    public void setActiveAccount(Account account) throws ActiveAccountNotInAccounts {
        model.setActiveAccount(account);
    }

    public SubjectList<Folder> getFolders() {
        return model.getFolders();
    }

    public Subject<Folder> getActiveFolder() {
        return model.getActiveFolder();
    }

    public Subject<Email> getActiveEmail() {
        return model.getActiveEmail();
    }

    public SubjectList<Email> getActiveEmails() {
        return model.getActiveEmails();
    }

    public void setFolder(Folder activeFolder) {
        model.setActiveFolder(activeFolder);
    }

    /**
     * Filters this model's activeEmails to only contain Emails with recipients containing
     * the search word.
     *
     * @param searchWord the word which the recipients of an email should match
     * @author Hampus Jernkrook
     */
    public void filterOnTo(String searchWord) {
        model.filterOnTo(searchWord);
    }

    /**
     * Filters this model's activeEmails to only contain Emails with senders containing the search word.
     *
     * @param searchWord the word which the senders of an email should match
     * @author Hampus Jernkrook
     */
    public void filterOnFrom(String searchWord) {
        model.filterOnFrom(searchWord);
    }

    /**
     * Filters this model's activeEmails to only contain Emails that was sent before
     * the specified date.
     *
     * @param date the maximum date of an email
     * @author Hampus Jernkrook
     */
    public void filterOnMaxDate(LocalDateTime date) {
        model.filterOnMaxDate(date);
    }

    /**
     * Filters this model's activeEmails to only contain Emails that was sent after the
     * specified date.
     *
     * @param date the minimum date of an email
     * @author Hampus Jernkrook
     */
    public void filterOnMinDate(LocalDateTime date) {
        model.filterOnMinDate(date);
    }

    /**
     * Sorts the emails in this model's activeEmails from the newest to the oldest date.
     *
     * @author Hampus Jernkrook
     */
    public void sortByNewToOld() {
        model.sortByNewToOld();
    }

    /**
     * Sorts the emails in this model's activeEmails from the oldest to the newest date.
     *
     * @author Hampus Jernkrook
     */
    public void sortByOldToNew() {
        model.sortByOldToNew();
    }

    /**
     * Erases all filters from this model's activeEmails and restores it to its default state.
     *
     * @author Hampus Jernkrook
     */
    public void clearFilter() {
        model.clearFilter();
    }

    /**
     * Filters this model's activeEmails to only contain Emails that has any attribute that contains the
     * search word.
     *
     * @param searchWord the word which at least one the Emails attributes should contain.
     * @author Hampus Jernkrook
     */
    public void search(String searchWord) {
        model.search(searchWord);
    }

    /** Clears the search results
     *
     * @author Hampus Jernkrook
     */
    public void clearSearchResult() {
        model.clearSearchResult();
    }

}
