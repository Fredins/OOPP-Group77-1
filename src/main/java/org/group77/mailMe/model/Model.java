package org.group77.mailMe.model;

import javafx.util.Pair;
import org.group77.mailMe.model.data.Account;
import org.group77.mailMe.model.data.Email;
import org.group77.mailMe.model.data.Folder;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Holds the application state.
 */

public class Model {

    // all accounts
    private SubjectList<Account> accounts = new SubjectList<>(new ArrayList<>());
    // all accounts and their folders
    //private Subject<Map<Account,List<Folder>>> folders = new Subject<>(null);
    // active account
    private Subject<Account> activeAccount = new Subject<>(null);
    // active accounts folders
    private SubjectList<Folder> activeFolders = new SubjectList<>(new ArrayList<>());
    private Subject<Folder> activeFolder = new Subject<>(null);
    private Subject<Email> readingEmail = new Subject<>(null);
    private SubjectList<Email> visibleEmails = new SubjectList<>(new ArrayList<>());



    AccountFactory accountFactory;

    public Model() {


    /* ALTERNATIVE: Model gets data from storage when it is created

    public Model(Map<Account,List<Folder>> accountFolderMap) {

        // ---- set attributes ----
        folders.set(accountFolderMap);
        accounts.replaceAll(accountFolderMap.keySet());

        // ----  set observers ----
        // if new account is added to this.accounts, set it as active
        this.accounts.addObserver(newAccounts -> {
            Account newAccount = newAccounts.get(newAccounts.size() - 1);
            activeAccount.set(newAccount);
        });

        // if active account is switched, set activeFolders to active accounts stored folders
        this.activeAccount.addObserver(newActiveAccount -> {
            if (newActiveAccount != null) {
                List<Folder> newActiveAccountFolders = folders.get().get(newActiveAccount);

                if (newActiveAccountFolders.isEmpty()) {
                    newActiveAccountFolders = createFolders();
                }

                activeFolders.replaceAll(newActiveAccountFolders);
            }
        });*/

    }

    /**
     * Add emails to this activeFolder's inbox folder.
     *
     * Emails already in inbox are not removed.
     *
     * @param newEmails emails to added to inbox folder in active account
     * @throws Exception if activeFolders does not have a inbox folder
     */

    public void updateInbox(List<Email> newEmails) throws Exception {

        Folder inbox = activeFolders.stream()
                .filter(folders -> folders.name().equals("Inbox"))
                .findFirst()
                .orElseThrow(Exception::new); // inbox not found
        Folder newInbox = new Folder(inbox.name(),
                Stream.of(newEmails, inbox.emails())
                        .flatMap(Collection::stream)
                        .collect(Collectors.toList())
        );
        // replace inbox with newInbox
        activeFolders.replace(inbox, newInbox);
    }

    public void setActiveAccount(Account account) {
        // check if account is in this accounts
        activeAccount.set(account);
        /*if (accounts.get().contains(account)) {

        }*/
    }

    public void addAccount(Account account) {
        // add account if it does not already exist in this accounts
        if (!(accounts.get().contains(account))) {
            accounts.add(account);
        }
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

    public SubjectList<Account> getAccounts() {
        return accounts;
    }


    public Subject<Account> getActiveAccount() {
        return activeAccount;
    }

    public SubjectList<Folder> getActiveFolders() {
        return activeFolders;
    }

    // --- view ---

    public Subject<Folder> getActiveFolder() {
        return activeFolder;
    }

    public Subject<Email> getReadingEmail() {
        return readingEmail;
    }

    public SubjectList<Email> getVisibleEmails() {
        return visibleEmails;
    }

    public void setActiveFolder(Folder activeFolder) {
        this.activeFolder.set(activeFolder);
    }

    public void setReadingEmail(Email readingEmail) {
        this.readingEmail.set(readingEmail);
    }

    public void setVisibleEmails(List<Email> visibleEmails) {
        this.visibleEmails.replaceAll(visibleEmails);
    }

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
