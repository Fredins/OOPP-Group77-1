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

    public void addSuggestion(List<String> newKnownRecipients) {
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
     * Tries to add a new account to accounts.
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
            Account account = model.createAccount(emailAddress, password.toCharArray());
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
     * Creates and sends an Email with the data from the parameters
     * and sets this model's activeAccount as sender of the Email.
     *
     * @param recipients The recipients of the email to be sent
     * @param subject    The subject of the email to be sent
     * @param content    The content of the email to be sent
     * @param files      The files of the email to be sent
     * @throws Exception if there is no active account in this model
     * @author Alexey Ryabov
     */
    public void send(List<String> recipients, String subject, String content, List<File> files) throws Exception {
        Account account = model.getActiveAccount().get();
        if (account != null) {
            //Creating new email to be copied to Sent-folder
            List<Attachment> attachments = files.stream()
                    .map(file -> new Attachment(file.getName(), null, file))
                    .collect(Collectors.toList());

            Email email = new Email(
                    account.emailAddress(),
                    recipients.toArray(String[]::new),
                    subject,
                    content,
                    attachments,
                    LocalDateTime.now());

            EmailServiceProvider esp = EmailServiceProviderFactory.createEmailServiceProvider(model.getActiveAccount().get());
            esp.sendEmail(account, email);

        } else {
            throw new Exception("No active account");
        }
    }

    /**
     * TODO: fix javadoc comment
     * Moves a copy of successfully an email into the a folder of choice.
     * Sent-folder(index 2 in the folders list).
     *
     * @param - is a email that is currently being sent.
     * @throws Exception
     * @author Alexey Ryabov
     */
    public void moveSentEmail(List<String> recipients, String subject, String content, List<File> files, int folderIndex) throws Exception {
        //Convert String list of recipients to String Array of recipients.
        String[] recipientsArray = recipients.toArray(String[]::new);
        //Creating new email to be copied to Sent-folder
        List<Attachment> attachments = files.stream()
                .map(file -> new Attachment(file.getName(), null, file))
                .collect(Collectors.toList());
        Email newEmail = new Email(model.getActiveAccount().get().emailAddress(), recipientsArray, subject, content, attachments, null);
        List<Folder> newFolders = storage.retrieveFolders(model.getActiveAccount().get());
        //Move the currently open email to the Sent-folder
        newFolders.get(folderIndex).emails().add(newEmail); // index 2 -> SentFolder
        storage.store(model.getActiveAccount().get(), newFolders);
        model.getFolders().replaceAll(newFolders);
    }

    /**
     * Removes this model's activeEmail and moves it to the trash folder.
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
        Email email = getActiveEmail().get();
        Folder deleteFromFolder = getActiveFolder().get();
        getActiveEmails().remove(email);
        deleteFromFolder.emails().remove(email);
        getActiveFolder().set(new Folder(deleteFromFolder.name(), deleteFromFolder.emails()));
        storage.store(getActiveAccount().get(), deleteFromFolder);
    }


    /**
     * Moves the email to the desired MoveTofolder and deletes it from the activeFolder. Choose where to move in the comboBox in the readingView.
     *
     * @param MoveTofolder The MoveTofolder that was selected in the "Move" comboBox in readingView
     * @author David Zamanian
     * @author Martin Fredin
     */

    public void moveEmail(Folder MoveTofolder) {
        Email email = getActiveEmail().get();
        getActiveEmails().remove(email);
        Folder moveFromFolder = getActiveFolder().get();
        moveFromFolder.emails().remove(email);
        getActiveFolder().set(new Folder(moveFromFolder.name(), moveFromFolder.emails()));
        MoveTofolder.emails().add(email);
        storage.store(getActiveAccount().get(), moveFromFolder);
        storage.store(getActiveAccount().get(), MoveTofolder);
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


    public void clearSearchResult() {
        model.clearSearchResult();
    }

}
