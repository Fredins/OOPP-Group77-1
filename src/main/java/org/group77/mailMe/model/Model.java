package org.group77.mailMe.model;

import javafx.fxml.FXML;
import org.group77.mailMe.model.data.*;
import org.group77.mailMe.services.emailServiceProvider.*;
import org.group77.mailMe.services.storage.*;
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
    public SubjectList<Email> visibleEmails = new SubjectList<>(new ArrayList<>());
    public SubjectList<Folder> folders = new SubjectList<>(new ArrayList<>());
    public SubjectList<Account> accounts = new SubjectList<>(new ArrayList<>());
    public Subject<Account> activeAccount = new Subject<>(null);
    public Subject<Folder> activeFolder = new Subject<>(null);
    public Subject<Email> readingEmail = new Subject<>(null);
    /**
     * 1. load accounts from storage
     * 2. add event handler to state field active account
     *
     * @author Martin Fredin
     */
    public Model() throws Exception {
        // get all accounts stored from previous sessions
        accounts.replaceAll(storage.retrieveAccounts());


        // change event handlers
        // these handlers are for relations between the different states

        // update folders when active account is changed
        activeAccount.addObserver( newAccount -> {
            // if a new account is set as active, get the stored folders of that account.
            if (newAccount != null) {
                List<Folder> newFolders = storage.retrieveFolders(activeAccount.get());
                if (newFolders.isEmpty()) { //if user has no folders stored, create new ones and store.
                    newFolders = createFolders();
                    storage.store(activeAccount.get(), newFolders);
                }
                folders.replaceAll(newFolders);
            }
        });
        // if a new account is added then set it as active
        accounts.addObserver(newAccounts -> {
            Account newAccount = newAccounts.get(newAccounts.size() - 1);
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
            folders.replace(inbox, newInbox);
            // replace inbox in storage with newInbox
            storage.store(activeAccount.get(), newInbox);
        } else { // if there was no active account of there were no folders
            throw new Exception("no active account");
        }
    }

    /**
     * Tries to add a new account to accounts.
     *
     * If emailAddress does not belong to a supported domain, throws Exception with informative message.
     * If account cannot connect to server or store account, throws Exception.
     *
     * @throws Exception if domain is not supported or authentication to server fails
     * @author Elin Hagman, Martin
     */
    public void addAccount(String emailAddress, String password) throws Exception {
        // if the new account can connect to the server then store it and add to state
        Account account = AccountFactory.createAccount(emailAddress, password.toCharArray());

        if (account != null) {

            if (EmailServiceProviderFactory.getEmailServiceProvider(account).testConnection(account)) {
                storage.store(account); // throws authentication exception
                accounts.add(account);

            }
        } else {
            throw new Exception("Domain not supported");
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
    
    /**
     * Removes the currently open email and moves it to the trash.
     * @throws Exception
     * @author David Zamanian
     */

    public void DeleteEmail() throws Exception {

        List<Folder> newFolders = storage.retrieveFolders(activeAccount.get());
        //Move the currently open email to the trash
        newFolders.get(4).emails().add(readingEmail.get()); //TODO Fix better index if we want to add more folders in the future (from 4 to compare name to "Trash" somehow..)
        //Remove currently open email from the activeFolder
        newFolders.get(newFolders.indexOf(activeFolder.get())).emails().remove(readingEmail.get());
        //storage.store(activeAccount.get(), newFolders);
        folders.replaceAll(newFolders);
        refresh();
    }

    /**
     * Moves tha email to the desired folder and deletes it from the activeFolder. Choose where to move in the comboBox in the readingView.
     * @throws Exception
     * @author David Zamanian
     */

    public void MoveEmail(Folder folder) throws Exception {

        List<Folder> newFolders = storage.retrieveFolders(activeAccount.get());
        //Move the currently open email to the chosen folder in the comboBox
        newFolders.get(newFolders.indexOf(folder)).emails().add(readingEmail.get());
        //Delete the currently open email from the activeFolder
        newFolders.get(newFolders.indexOf(activeFolder.get())).emails().remove(readingEmail.get());
        //storage.store(activeAccount.get(), newFolders);
        folders.replaceAll(newFolders);
        refresh();

    }
}




