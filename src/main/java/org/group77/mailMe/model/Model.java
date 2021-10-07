package org.group77.mailMe.model;

import javafx.beans.property.*;
import javafx.beans.value.*;
import javafx.collections.*;
import org.group77.mailMe.model.data.*;
import org.group77.mailMe.services.emailServiceProvider.*;
import org.group77.mailMe.services.storage.*;

import java.io.*;
import java.util.*;
import java.util.stream.*;

/**
 * Class carrying the state of the application and
 * delegating work to other components of the application.
 */
public class Model {
    // Services //TODO change so that this is set via constructor from main
    private final Storage storage = new LocalDiscStorage();

    // Application state
    public ObservableList<Email> visibleEmails = FXCollections.observableList(new ArrayList<>());
    public ObservableList<Folder> folders = FXCollections.observableList(new ArrayList<>());
    public ObservableList<Account> accounts = FXCollections.observableList(new ArrayList<>());
    public SimpleObjectProperty<Account> activeAccount = new SimpleObjectProperty<>(null);
    public SimpleObjectProperty<Folder> activeFolder = new SimpleObjectProperty<>(null);
    // the email currently read by the user
    public SimpleObjectProperty<Email> readingEmail = new SimpleObjectProperty<>(null);

    /**
     * 1. load accounts from storage
     * 2. add event handler to state field active account
     *
     * @author Martin Fredin
     */
    public Model() throws Exception {
        // get all accounts stored from previous sessions
        accounts.setAll(storage.retrieveAccounts());


        // change event handlers
        // these handlers are for relations between the different states

        // update folders when active account is changed
        activeAccount.addListener((ChangeListener<? super Account>) (obs, oldAccount, newAccount) -> {
            // if a new account is set as active, get the stored folders of that account.
            if (newAccount != null) {
                List<Folder> newFolders = storage.retrieveFolders(activeAccount.get());
                if (newFolders.isEmpty()) { //if user has no folders stored, create new ones and store.
                    newFolders = createFolders();
                    storage.store(activeAccount.get(), newFolders);
                }
                folders.setAll(newFolders);
            }
        });
        // if a new account is added then set it as active
        accounts.addListener((ListChangeListener<? super Account>) changeEvent -> {
            Account newAccount = changeEvent.getList().get(changeEvent.getList().size() - 1);
            activeAccount.set(newAccount);
        });

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
        // if there is an active account and all folders are set then refresh from server.
        if (activeAccount.get() != null && !folders.isEmpty()) {
            // get inbox from state folders
            Folder inbox = folders.stream()
                    .filter(folder -> folder.name().equals("Inbox"))
                    .findFirst()
                    .orElseThrow(Exception::new);
            // get all new emails from server
            List<Email> serverEmails = EmailServiceProviderFactory.getEmailServiceProvider(
                    activeAccount.get()).refreshFromServer(activeAccount.get());
            // get emails currently in inbox in application.
            List<Email> inboxEmails = inbox.emails();
            // diffEmails = serverEmails \ inboxEmails.
            // This is needed in case some email previously fetched is left on the pop3 server.
            List<Email> diffEmails = serverEmails.stream()
                    .filter(email -> !inboxEmails.contains(email))
                    .collect(Collectors.toList());

            Folder newInbox = new Folder(inbox.name(),
                    Stream.of(diffEmails, inbox.emails())
                            .flatMap(Collection::stream)
                            .collect(Collectors.toList())
            );
            // replace inbox with newInbox
            folders.set(folders.indexOf(inbox), newInbox);
            // replace inbox in storage with newInbox
            storage.store(activeAccount.get(), newInbox);
        } else { // if there was no active account of there were no folders
            throw new Exception("no active account");
        }
    }

    /**
     * Adds a new account by adding it to the application state and storing it.
     *
     * @throws Exception if stoage or email serive provider have problems
     * @author Elin Hagman, Martin
     */
    public void addAccount(Account account) throws Exception {
        // if the new account can connect to the server then store it and add to state.
        if (EmailServiceProviderFactory.getEmailServiceProvider(account).testConnection(account)) {
            storage.store(account);
            accounts.add(account);
        }
    }

    /**
     * sends the email via the appropriate email service provider
     * TODO Alexey kan vi byta till send(Account) och sedan fixa med resten på backend?
     *
     * @author Alexey Ryabov
     */
    public void send(List<String> recipients, String subject, String content, List<String> attachments) throws Exception {
        // if there is an active account, then send a new email with the given arguments.
        if (activeAccount.get() != null) {
            EmailServiceProviderFactory.getEmailServiceProvider(activeAccount.get()).sendEmail(
                    activeAccount.get(),
                    recipients,
                    subject,
                    content,
                    attachments
            );
        } else {
            throw new Exception("no active account");
        }
    }

    /**
     * Create new folders.
     *
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
}



