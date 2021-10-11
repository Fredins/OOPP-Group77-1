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

public class newModel {

    // all accounts
    private SubjectList<Account> accounts = new SubjectList<>(new ArrayList<>());
    // all accounts and their folders
    private Subject<Map<Account,List<Folder>>> folders = new Subject<>(null);
    // active account
    private Subject<Account> activeAccount = new Subject<>(null);
    // active accounts folders
    private SubjectList<Folder> activeFolders = new SubjectList<>(new ArrayList<>());

    // ev activeFolder, visableEmails osv, men känns som det är väldigt kopplat till View ist för Modell

    AccountFactory accountFactory;

    public newModel(Map<Account,List<Folder>> accountFolderMap) {

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
        });

    }

    public void updateInbox(List<Email> newEmails) throws Exception {

        // TODO: copied from Model, not sure if it's correct

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
        // check if account is in active account
        if (accounts.get().contains(account)) {
            activeAccount.set(account);
        }
    }

    public void addAccount(Account account) {
        // add account if it does not already exist in this accounts
        if (!(accounts.get().contains(account))) {
            accounts.add(account);
        }
    }


    private List<Folder> createFolders() {
        return List.of(
                new Folder("Inbox", new ArrayList<>()),
                new Folder("Archive", new ArrayList<>()),
                new Folder("Sent", new ArrayList<>()),
                new Folder("Drafts", new ArrayList<>()),
                new Folder("Trash", new ArrayList<>())
        );
    }

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

}
