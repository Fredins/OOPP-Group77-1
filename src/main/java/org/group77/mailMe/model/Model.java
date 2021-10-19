package org.group77.mailMe.model;

import org.group77.mailMe.model.data.Account;
import org.group77.mailMe.model.data.Email;
import org.group77.mailMe.model.data.Folder;
import org.group77.mailMe.model.exceptions.*;
import org.group77.mailMe.model.textFinding.TextFinder;
import org.group77.mailMe.services.storage.AccountAlreadyExistsException;


import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Holds the application state.
 */

public class Model {

    //private Subject<Map<Account,List<Folder>>> folders = new Subject<>(null);
    // state fields
    private final SubjectList<Account> accounts = new SubjectList<>(new ArrayList<>());
    private final SubjectList<Folder> folders = new SubjectList<>(new ArrayList<>());
    private final SubjectList<Email> activeEmails = new SubjectList<>(new ArrayList<>());
    private final Subject<Account> activeAccount = new Subject<>(null);
    private final Subject<Folder> activeFolder = new Subject<>(null);
    private final Subject<Email> activeEmail = new Subject<>(null);
    private final TextFinder textFinder = new TextFinder();

    private final SubjectList<String> autoSuggestions = new SubjectList<>(new ArrayList<>());

    public Model(List<Account> accounts){
        // if a new account is added then set it as active
        this.accounts.replaceAll(accounts);
        this.accounts.addObserver(newAccounts -> {
            Account newAccount = newAccounts.get(newAccounts.size() - 1);
            activeAccount.set(newAccount);
        }); // -- moved this cause it doesn't have anything to do with services. Martin


    }

    /**
     * 1. find the correct folder
     * 2. replace emails in folder
     * @author Martin
     * @param newEmails emails to added to inbox folder in active account
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

    public void setActiveAccount(Account account) throws ActiveAccountNotInAccounts {
        // check if account is in this accounts

        if (accounts.get().contains(account)) {
            activeAccount.set(account);
        } else {
            throw new ActiveAccountNotInAccounts();
        }
    }

    public void addAccount(Account account) throws AccountAlreadyExistsException {
        // add account if it does not already exist in this accounts
        if (!(accounts.get().contains(account))) {
            accounts.add(account);
        } else {
            throw new AccountAlreadyExistsException("Account already exists");
        }
    }

    public Account createAccount(String emailAddress, char[] password) throws EmailDomainNotSupportedException {
        return AccountFactory.createAccount(emailAddress, password);
    }


    public List<Folder> createFolders() {
        return List.of(
                new Folder("Inbox", new ArrayList<>()),
                new Folder("Archive", new ArrayList<>()),
                new Folder("Sent", new ArrayList<>()),
                new Folder("Drafts", new ArrayList<>()),
                new Folder("Trash", new ArrayList<>())
        );
    }

    public SubjectList<String> getAutoSuggestions() {return autoSuggestions;}
    public void setAutoSuggestions(List<String> list) {this.autoSuggestions.replaceAll(list);}

    public SubjectList<Account> getAccounts() { return accounts; }
    public Subject<Account> getActiveAccount() { return activeAccount; }
    public SubjectList<Folder> getFolders() { return folders; }
    public Subject<Folder> getActiveFolder() { return activeFolder; }
    public Subject<Email> getActiveEmail() { return activeEmail; }
    public SubjectList<Email> getActiveEmails() { return activeEmails; }
    public void setActiveFolder(Folder activeFolder) { this.activeFolder.set(activeFolder); }
    public void setActiveEmail(Email activeEmail) { this.activeEmail.set(activeEmail); }
    public void setActiveEmails(List<Email> activeEmails) { this.activeEmails.replaceAll(activeEmails); }

    public void filterOnTo(String searchWord) {
        List<Email> newActiveEmails = textFinder.filterOnTo(activeEmails.get(), searchWord);
        setActiveEmails(newActiveEmails);
    }

    public void filterOnFrom(String searchWord) {
        List<Email> newActiveEmails = textFinder.filterOnFrom(activeEmails.get(), searchWord);
        setActiveEmails(newActiveEmails);
    }

    public void filterOnMaxDate(LocalDateTime date) {
        List<Email> newActiveEmails = textFinder.filterOnMaxDate(activeEmails.get(), date);
        setActiveEmails(newActiveEmails);
    }

    public void filterOnMinDate(LocalDateTime date) {
        List<Email> newActiveEmails = textFinder.filterOnMinDate(activeEmails.get(), date);
        setActiveEmails(newActiveEmails);
    }

    public void sortByNewToOld() {
        List<Email> newActiveEmails = textFinder.sortByNewToOld(activeEmails.get());
        setActiveEmails(newActiveEmails);
    }

    public void sortByOldToNew() {
        List<Email> newActiveEmails = textFinder.sortByOldToNew(activeEmails.get());
        setActiveEmails(newActiveEmails);
    }

    public void clearFilter() {
        // set active emails to all emails in the current folder
        setActiveEmails(activeFolder.get().emails());
    }

    public void search(String searchWord) {
        List<Email> newActiveEmails = textFinder.search(activeEmails.get(), searchWord);
        setActiveEmails(newActiveEmails);
    }

    // does same as clearFilter....
    public void clearSearchResult() {
        setActiveEmails(activeFolder.get().emails());
    }
}
