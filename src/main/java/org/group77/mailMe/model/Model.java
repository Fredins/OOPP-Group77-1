package org.group77.mailMe.model;

import javafx.beans.property.*;
import javafx.collections.*;
import javafx.util.*;
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

  public void refresh() throws Exception {
    if (activeAccount.get() != null && !folders.isEmpty()) {
      Folder inbox = folders.stream()
        .filter(f -> f.name().equals("Inbox"))
        .findFirst()
        .orElseThrow(Exception::new);
      List<Email> emails = EmailServiceProviderFactory.getEmailServiceProvider(activeAccount.get()).refreshFromServer(activeAccount.get());
      List<Email> diffEmails = emails.stream()
        .filter(e -> !inbox.emails().contains(e))
        .collect(Collectors.toList());
      Folder newInbox = new Folder(inbox.name(),
                                   Stream.of(diffEmails, inbox.emails())
                                     .flatMap(Collection::stream)
                                     .collect(Collectors.toList())
      );

      activeFolder.set(newInbox);
      storage.store(activeAccount.get(), newInbox);
    } else {
      throw new Exception("no active account");
    }
  }
  public void addAccount(Account account) throws Exception {
    storage.store(account);
  }
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
  private List<Account> getAllAccounts() {
    return storage.retrieveAllAccounts();
  }

  private List<Folder> getAllFolders() throws Exception {
    if (activeAccount.get() != null) {
      return storage.retrieveFolders(activeAccount.get());
    } else {
      throw new Exception("no active account");
    }
  }

  public Model() throws OSNotFoundException, IOException {
    accounts.setAll(getAllAccounts());

    if(!accounts.isEmpty()){
      activeAccount.set(accounts.get(0));
    }

    if (activeAccount.get() != null) {
      List<Folder> localFolders = storage.retrieveFolders(activeAccount.get());
      if (localFolders.isEmpty()) {
        createFolders();
      }

      folders.setAll(storage.retrieveFolders(activeAccount.get()));
    }
  }

  public void createFolders() { // TODO this should be some sort of gui where the user can decide which folders he wants
    List<Folder> folderList = List.of(
      new Folder("Inbox", new ArrayList<>()),
      new Folder("Archive", new ArrayList<>()),
      new Folder("Sent", new ArrayList<>()),
      new Folder("Drafts", new ArrayList<>()),
      new Folder("Trash", new ArrayList<>())
    );
    folders.setAll(folderList);
    storage.store(activeAccount.get(), folderList);
  }
}



