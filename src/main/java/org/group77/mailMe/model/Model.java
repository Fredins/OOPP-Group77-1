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

public class Model {
  // Services
  private final Storage storage = new LocalDiscStorage();

  // Application state
  public ObservableList<Email> visibleEmails = FXCollections.observableList(new ArrayList<>());
  public ObservableList<Folder> folders = FXCollections.observableList(new ArrayList<>());
  public ObservableList<Account> accounts = FXCollections.observableList(new ArrayList<>());
  public SimpleObjectProperty<Account> activeAccount = new SimpleObjectProperty<>(null);
  public SimpleObjectProperty<Folder> activeFolder = new SimpleObjectProperty<>(null);
  public SimpleObjectProperty<Email> readingEmail = new SimpleObjectProperty<>(null);

  /**
   * 1. load accounts from storage
   * 2. add event handler to state field active account
   */
  public Model() throws OSNotFoundException, IOException {
    accounts.setAll(storage.retrieveAccounts());


    // change event handlers
    // these handlers are for relations between the different states

    // update folders when active account is changed
    activeAccount.addListener((ChangeListener<? super Account>) (obs, oldAccount, newAccount) -> {
      if(newAccount != null){
        folders.setAll(storage.retrieveFolders(activeAccount.get()));
        if(folders.isEmpty()){
          List<Folder> newFolders = createFolders();
          folders.setAll(newFolders);
          storage.store(activeAccount.get(), newFolders);
        }
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
   */
  public void refresh() throws Exception {
    if (activeAccount.get() != null && !folders.isEmpty()) {
      // get inbox from state folders
      Folder inbox = folders.stream()
        .filter(folder -> folder.name().equals("Inbox"))
        .findFirst()
        .orElseThrow(Exception::new);
      // diffEmails = serverEmails \ inboxEmails
      List<Email> serverEmails = EmailServiceProviderFactory.getEmailServiceProvider(activeAccount.get()).refreshFromServer(activeAccount.get());
      List<Email> inboxEmails = inbox.emails();
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
    } else {
      throw new Exception("no active account");
    }
  }

  /**
   * 1. add account to state field accounts
   * 2. store the account
   * 3. make the account active
   * 4. create initial folders for the account
   */
  public void addAccount(Account account) throws Exception {
    if (EmailServiceProviderFactory.getEmailServiceProvider(account).testConnection(account)){
      storage.store(account);
      accounts.add(account);
    }
  }

  /**
   *  sends the email via the appropriate email service provider
   * TODO Alexey kan vi byta till send(Account) och sedan fixa med resten på backend?
   */
  public void send(List<String> recipients, String subject, String content, List<String> attachments) throws Exception {
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
   * 1. create folders and set the corresponding state field
   * 2. store the folders
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



