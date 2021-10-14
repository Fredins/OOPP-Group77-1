package org.group77.mailMe.model;

import org.group77.mailMe.model.data.Account;
import org.group77.mailMe.model.data.Email;
import org.group77.mailMe.model.data.Folder;

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
    private final SubjectList<Email> emails = new SubjectList<>(new ArrayList<>());
    private final Subject<Account> activeAccount = new Subject<>(null);
    private final Subject<Folder> activeFolder = new Subject<>(null);
    private final Subject<Email> activeEmail = new Subject<>(null);

    public Model(List<Account> accounts){
        // if a new account is added then set it as active
        this.accounts.replaceAll(accounts);
        this.accounts.addObserver(newAccounts -> {
            Account newAccount = newAccounts.get(newAccounts.size() - 1);
            activeAccount.set(newAccount);
        }); // -- moved this cause it doesn't have anything to do with services. Martin


    }

    /**
     * Add emails to this activeFolder's inbox folder.
     *
     * Emails already in inbox are not removed.
     *
     * @param newEmails emails to added to inbox folder in active account
     * @throws InboxNotFoundException if activeFolders does not have a inbox folder
     */

    public void updateInbox(List<Email> newEmails) throws InboxNotFoundException {
        Folder inbox = folders.stream()
                .filter(folders -> folders.name().equals("Inbox"))
                .findFirst()
                .orElseThrow(InboxNotFoundException::new); // inbox not found
        Folder newInbox = new Folder(inbox.name(),
                Stream.of(newEmails, inbox.emails())
                        .flatMap(Collection::stream)
                        .collect(Collectors.toList())
        );
        // replace inbox with newInbox
        folders.replace(inbox, newInbox);
    }

    public void setActiveAccount(Account account) throws ActiveAccountNotInAccounts {
        // check if account is in this accounts

        if (accounts.get().contains(account)) {
            activeAccount.set(account);
        } else {
            throw new ActiveAccountNotInAccounts();
        }
    }

    public void addAccount(Account account) {
        // add account if it does not already exist in this accounts
        if (!(accounts.get().contains(account))) {
            accounts.add(account);
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

    public SubjectList<Account> getAccounts() { return accounts; }
    public Subject<Account> getActiveAccount() { return activeAccount; }
    public SubjectList<Folder> getFolders() { return folders; }
    public Subject<Folder> getActiveFolder() { return activeFolder; }
    public Subject<Email> getActiveEmail() { return activeEmail; }
    public SubjectList<Email> getEmails() { return emails; }
    public void setActiveFolder(Folder activeFolder) { this.activeFolder.set(activeFolder); }
    public void setActiveEmail(Email activeEmail) { this.activeEmail.set(activeEmail); }
    public void setEmails(List<Email> emails) { this.emails.replaceAll(emails); }

    /*
    public Account getActiveAccount() {
        return activeAccount.get();
        // will not return Subject, only account in it, so no one can change it.
        // register as an observer in some other way
    }

    public List<Folder> getActiveFolders() {
        return activeFolders.get(); // will not return SubjectList...
    }

    public List<Account> getAccounts() {
        return accounts.get();
    }

    public Map<Account,List<Folder>> getFolders() {
        return folders.get();
    }

     */
}
