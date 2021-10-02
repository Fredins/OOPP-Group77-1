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
  public SimpleObjectProperty<Pair<Boolean, Account>> activeAccount = new SimpleObjectProperty<>(new Pair<>(false, null));
  public SimpleObjectProperty<Pair<Boolean, Folder>> activeFolder = new SimpleObjectProperty<>(new Pair<>(false, null));
  public SimpleObjectProperty<Pair<Boolean, Email>> readingEmail = new SimpleObjectProperty<>(new Pair<>(false, null));

  public void refresh() throws Exception {
    if (activeAccount.get().getKey() && !folders.isEmpty()) {
      Folder inbox = folders.stream()
        .filter(f -> f.name().equals("Inbox"))
        .findFirst()
        .orElseThrow(Exception::new);
      List<Email> emails = EmailServiceProviderFactory.getEmailServiceProvider(activeAccount.get().getValue()).refreshFromServer(activeAccount.get().getValue());
      List<Email> diffEmails = emails.stream()
        .filter(e -> !inbox.emails().contains(e))
        .collect(Collectors.toList());
      inbox.emails().addAll(0, diffEmails);
      storage.store(activeAccount.get().getValue(), inbox);
      activeFolder.set(new Pair<>(true, inbox));
    } else {
      throw new Exception("no active account");
    }
  }
  public void addAccount(Account account) throws Exception {
    storage.store(account);
  }
  public void send(List<String> recipients, String subject, String content, List<String> attachments) throws Exception {
    if (activeAccount.get().getKey()) {
      EmailServiceProviderFactory.getEmailServiceProvider(activeAccount.get().getValue()).sendEmail(
        activeAccount.get().getValue(),
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
    if (activeAccount.get().getKey()) {
      return storage.retrieveFolders(activeAccount.get().getValue());
    } else {
      throw new Exception("no active account");
    }
  }

  public Model() throws OSNotFoundException, IOException {
    accounts.setAll(getAllAccounts());

    if (!accounts.isEmpty()) {
      activeAccount.set(new Pair<>(true, accounts.get(0)));
    }
    if (activeAccount.get().getKey()) {
      List<Folder> localFolders = storage.retrieveFolders(activeAccount.get().getValue());
      if (localFolders.isEmpty()) {
        createFolders();
      }

      folders.setAll(storage.retrieveFolders(activeAccount.get().getValue()));
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
    storage.store(activeAccount.get().getValue(), folderList);
  }
}



