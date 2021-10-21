package org.group77.mailMe.model;

import org.group77.mailMe.model.textFinding.TextFinder;
import org.group77.mailMe.services.storage.AccountAlreadyStoredException;


import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * A high level layer of the state of an email application.
 * <p>
 * Can hold several accounts and a possibly current activeAccount.
 * Can hold folders that belong to the activeAccount.
 * Holds activeFolder, activeEmails and activeEmail which content can be filtered, sorted and searched on.
 * Can hold previous recipients of the activeAccount.
 *
 * @author Elin Hagman
 * @author Martin Fredin
 * @author Hampus Jernkrook
 */

public class Model {

    // state fields
    /**
     * An observable list of accounts of the email application.
     */
    private final SubjectList<Account> accounts = new SubjectList<>(new ArrayList<>());
    /**
     * An observable subject with the currently activeAccount .
     */
    private final Subject<Account> activeAccount = new Subject<>(null);
    /**
     * An observable list of the folders that belong to activeAccount.
     */
    private final SubjectList<Folder> folders = new SubjectList<>(new ArrayList<>());
    /**
     * An observable subject with a folder in folders that is active.
     */
    private final Subject<Folder> activeFolder = new Subject<>(null);
    /**
     * A list of observable emails of this activeFolder which sort, filter and search functionality
     * will be applied to.
     */
    private final SubjectList<Email> activeEmails = new SubjectList<>(new ArrayList<>());
    /**
     * An observable subject of the emails in activeEmails which sort, filter and search
     * functionality will be applied to.
     */
    private final Subject<Email> activeEmail = new Subject<>(null);
    /**
     * An observable list that can hold the previous recipients of the activeAccount.
     */
    private final SubjectList<String> knownRecipients = new SubjectList<>(new ArrayList<>());

    /**
     * TextFinder that provides search, filter and sort functionality to Model.
     */
    private final TextFinder textFinder = new TextFinder();

    /**
     * Creates a Model with the given accounts.
     * Adds observer to this accounts that sets any account added to this activeAccount.
     * There is initially no activeAccount, has to be set by client.
     *
     * @param accounts the accounts of the email application
     * @author Martin Fredin
     */

    public Model(List<Account> accounts) {
        this.accounts.replaceAll(accounts);
        // if a new account is added then set it as active
        this.accounts.addObserver(newAccounts -> {
            Account newAccount = newAccounts.get(newAccounts.size() - 1);
            activeAccount.set(newAccount);
        });


    }


    /**
     * Adds emails to the specified folder.
     *
     * @param folder    the folder to be updated
     * @param newEmails the emails to be added to the specified folder
     * @author Martin Fredin
     */

    public void updateFolder(Folder folder, List<Email> newEmails) {
        Folder newFolder = new Folder(folder.name(),
                Stream.of(newEmails, folder.emails())
                        .flatMap(Collection::stream)
                        .collect(Collectors.toList())
        );
        // replace inbox with newInbox
        folders.replace(folder, newFolder);
    }

    /**
     * Sets one of the accounts in this accounts as activeAccount.
     *
     * @param account to be set as activeAccount
     * @throws ActiveAccountNotInAccounts if account is not in this accounts
     * @author Elin Hagman
     */

    public void setActiveAccount(Account account) throws ActiveAccountNotInAccounts {

        if (accounts.get().contains(account)) {
            activeAccount.set(account);
        } else {
            throw new ActiveAccountNotInAccounts();
        }
    }

    /**
     * Adds an account to this accounts if it does not already exist.
     *
     * @param account to be added to accounts
     * @throws AccountAlreadyStoredException if account already exists in accounts
     * @author Elin Hagman
     */

    public void addAccount(Account account) throws AccountAlreadyStoredException {

        if (!(accounts.get().contains(account))) {
            accounts.add(account);
        } else {
            throw new AccountAlreadyStoredException("Account already exists");
        }
    }

    /**
     * Creates an account with the specified emailAddress and password
     *
     * @param emailAddress the email address of the account to be created
     * @param password     the password of the account to be created
     * @return the account with the specified email address and password
     * @throws EmailDomainNotSupportedException if AccountFactory gives exception that domain is not supported
     * @author Elin Hagman
     */

    public Account createAccount(String emailAddress, char[] password) throws EmailDomainNotSupportedException {
        return AccountFactory.createAccount(emailAddress, password);
    }

    /**
     * Creates and returns a default set of folders.
     *
     * @return List of folders
     * @author Martin Fredin
     */
    public List<Folder> createFolders() {
        return List.of(
                new Folder("Inbox", new ArrayList<>()),
                new Folder("Archive", new ArrayList<>()),
                new Folder("Sent", new ArrayList<>()),
                new Folder("Drafts", new ArrayList<>()),
                new Folder("Trash", new ArrayList<>())
        );
    }

    /** Gets called from Control's getAutoSuggestion
     *
     * @return returns the list of the known recipients
     * @author David Zamanian
     */
    public SubjectList<String> getKnownRecipients() {
        return knownRecipients;
    }

    /** Replaces knownRecipients with the new list
     *
     * @param list The new list of recipients that will replace the old onw
     * @author David Zamanian
     */
    public void setKnownRecipients(List<String> list) {
        this.knownRecipients.replaceAll(list);
    }

    public SubjectList<Account> getAccounts() {
        return accounts;
    }

    public Subject<Account> getActiveAccount() {
        return activeAccount;
    }

    public SubjectList<Folder> getFolders() {
        return folders;
    }

    public Subject<Folder> getActiveFolder() {
        return activeFolder;
    }

    public Subject<Email> getActiveEmail() {
        return activeEmail;
    }

    public SubjectList<Email> getActiveEmails() {
        return activeEmails;
    }

    public void setActiveFolder(Folder activeFolder) {
        this.activeFolder.set(activeFolder);
    }

    public void setActiveEmail(Email activeEmail) {
        this.activeEmail.set(activeEmail);
    }

    public void setActiveEmails(List<Email> activeEmails) {
        this.activeEmails.replaceAll(activeEmails);
    }

    /**
     * Filters this activeEmails to only contain Emails with recipients containing the search word.
     *
     * @param searchWord the word which the recipients of an email should match
     * @author Hampus Jernkrook
     */
    public void filterOnTo(String searchWord) {
        List<Email> newActiveEmails = textFinder.filterOnTo(activeEmails.get(), searchWord);
        setActiveEmails(newActiveEmails);
    }

    /**
     * Filters this activeEmails to only contain Emails with senders containing the search word.
     *
     * @param searchWord the word which the senders of an email should match
     * @author Hampus Jernkrook
     */
    public void filterOnFrom(String searchWord) {
        List<Email> newActiveEmails = textFinder.filterOnFrom(activeEmails.get(), searchWord);
        setActiveEmails(newActiveEmails);
    }

    /**
     * Filters this activeEmails to only contain Emails that was sent before the specified date.
     *
     * @param date the maximum date of an email
     * @author Hampus Jernkrook
     */
    public void filterOnMaxDate(LocalDateTime date) {
        List<Email> newActiveEmails = textFinder.filterOnMaxDate(activeEmails.get(), date);
        setActiveEmails(newActiveEmails);
    }

    /**
     * Filters this activeEmails to only contain Emails that was sent after the specified date.
     *
     * @param date the minimum date of an email
     * @author Hampus Jernkrook
     */

    public void filterOnMinDate(LocalDateTime date) {
        List<Email> newActiveEmails = textFinder.filterOnMinDate(activeEmails.get(), date);
        setActiveEmails(newActiveEmails);
    }

    /**
     * Sorts the emails in this activeEmails from the newest to the oldest date.
     *
     * @author Hampus Jernkrook
     */
    public void sortByNewToOld() {
        List<Email> newActiveEmails = textFinder.sortByNewToOld(activeEmails.get());
        setActiveEmails(newActiveEmails);
    }

    /**
     * Sorts the emails in this activeEmails from the oldest to the newest date.
     *
     * @author Hampus Jernkrook
     */
    public void sortByOldToNew() {
        List<Email> newActiveEmails = textFinder.sortByOldToNew(activeEmails.get());
        setActiveEmails(newActiveEmails);
    }

    /**
     * Erases all filters from this activeEmails and restores it to its default state.
     *
     * @author Hampus Jernkrook
     */
    public void clearFilter() {
        // set active emails to all emails in the current folder
        setActiveEmails(activeFolder.get().emails());
    }

    /**
     * Filters this activeEmails to only contain Emails that has any attribute that contains the
     * search word.
     *
     * @param searchWord the word which at least one the Emails attributes should contain.
     * @author Hampus Jernkrook
     */

    public void search(String searchWord) {
        List<Email> newActiveEmails = textFinder.search(activeEmails.get(), searchWord);
        setActiveEmails(newActiveEmails);
    }


    /** Clears the search result by setting the active emails to all emails in the active folder.
     *
     * @author Hampus Jernkrook
     */
    public void clearSearchResult() {
        setActiveEmails(activeFolder.get().emails());
    }

    /**
     * Moves this activeEmail to trash folder
     *
     * @author David Zamanian
     * @author Elin Hagman
     */
    public void deleteEmail() {
        // remove activeEmail from its folder
        // add it to trash folder
    }

    /**
     * Permanently deletes this activeEmail from this activeFolder and this activeEmails
     *
     * @author Elin Hagman
     * @author David Zamanian
     */
    public void permDeleteEmail() {
        activeEmails.remove(activeEmail.get());
        activeFolder.get().deleteEmail(activeEmail.get());
        // next row is necessary to notify listeners
        getActiveFolder().set(new Folder(activeFolder.get().name(), activeFolder.get().emails()));
    }
}
