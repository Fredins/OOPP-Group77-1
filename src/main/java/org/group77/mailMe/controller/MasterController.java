package org.group77.mailMe.controller;

import javafx.beans.value.*;
import javafx.collections.*;
import javafx.fxml.*;
import javafx.scene.control.*;
import javafx.scene.image.*;
import javafx.scene.layout.*;
import javafx.stage.*;
import javafx.util.*;
import org.group77.mailMe.*;
import org.group77.mailMe.controller.utils.*;
import org.group77.mailMe.model.*;
import org.group77.mailMe.model.data.*;

import java.io.*;
import java.util.*;
import java.util.stream.*;

public class MasterController {
  @FXML private Button refreshBtn;
  @FXML private Button addAccountBtn;
  @FXML private Button writeBtn;
  @FXML private FlowPane foldersFlow;
  @FXML private Pane readingPane;
  @FXML private ComboBox<Account> accountsCombo;
  @FXML private TextField searchField;
  @FXML private ImageView searchImg;
  @FXML private ImageView accountImg;
  @FXML private FlowPane emailsFlow;

  /**
   * 1. load/display folders in flowPane
   * 2. populate account comboBox
   * 3. add event handlers to nodes and state fields
   */
  public void init(Model model) {
    loadFolders(model.folders, model);

    if (model.accounts != null) {
      populateAcountCombo(model.accounts, model);
    }

    // input handlers
    refreshBtn.setOnAction(i -> {
      try {
        model.refresh();
      } catch (Exception e) {
        e.printStackTrace(); // TODO feedback
      }
    });
    addAccountBtn.setOnAction(i -> WindowOpener.openAddAccount(model, node -> ((Stage) node.getScene().getWindow()).close()));
    writeBtn.setOnAction(i -> WindowOpener.openWriting(model));
    accountsCombo.setOnAction(i -> {
      Account selected = accountsCombo.getSelectionModel().getSelectedItem();
      if (selected != null) {
        model.activeAccount.set(selected);
      }
    });

    // change handlers
    model.folders.addListener((ListChangeListener<? super Folder>) c -> loadFolders(c.getList(), model));
    model.visibleEmails.addListener((ListChangeListener<? super Email>) c -> loadEmails(c.getList(), model));
    model.accounts.addListener((ListChangeListener<? super Account>) c -> populateAcountCombo(c.getList(), model));
    model.activeFolder.addListener((ChangeListener<? super Folder>) (obs, oldFolder, newFolder) -> model.visibleEmails.setAll(newFolder.emails()));
    model.readingEmail.addListener((o, op, p) -> {
      if (p != null) {
        loadReading(p, model);
      } else {
        readingPane.getChildren().clear();
      }
    });
  }

  /**
   * populates the comboBox with accounts
   */
  private void populateAcountCombo(List<? extends Account> accounts, Model m) {
    accountsCombo.getItems().clear();
    Account addAcc = new Account("Add New Account", new char[]{}, ServerProvider.GMAIL_PROVIDER);
    // override toString() and fromString() to make account correspond to its email address
    accountsCombo.setConverter(new StringConverter<>() {
      @Override public String toString(Account account) {
        return account != null ? account.emailAddress() : null;
      }
      @Override public Account fromString(String string) {
        Account account = null;
        try {
          account = m.accounts.stream()
            .filter(acc -> acc.emailAddress().equals(string))
            .findAny()
            .orElseThrow(Exception::new);
        } catch (Exception e) {
          e.printStackTrace();
        }
        return account;
      }
    });
    accountsCombo.getItems().addAll(accounts);
    accountsCombo.getItems().add(addAcc);
  }

  /**
   * loads/display email
   */
  void loadReading(Email email, Model model) {
    try {
      FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("Reading.fxml"));
      Pane pane = fxmlLoader.load();
      ((ReadingController) fxmlLoader.getController()).init(model, email);
      readingPane.getChildren().clear();
      readingPane.getChildren().add(pane);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * 1. initialize every folder in state field folders
   * 2. display every folder in the folder flowPane
   */
  private void loadFolders(List<? extends Folder> folders, Model model) {
    foldersFlow.getChildren().clear();
    foldersFlow.getChildren().addAll(folders
                                       .stream()
                                       .map(folder -> {
                                         Pane pane = null;
                                         try {
                                           FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("FolderItem.fxml"));
                                           pane = fxmlLoader.load();
                                           ((FolderItemController) fxmlLoader.getController()).init(model, folder);
                                         } catch (IOException e) {
                                           e.printStackTrace();
                                         }
                                         return pane;
                                       })
                                       .collect(Collectors.toList()));
  }
  /**
   * 1. initialize every email in state field visibleEmails
   * 2. display every email in the emails flowPane
   */
  private void loadEmails(List<? extends Email> emails, Model model) {
    emailsFlow.getChildren().clear();
    emailsFlow.getChildren().addAll(emails
                                      .stream()
                                      .map(email -> {
                                        Pane pane = null;
                                        try {
                                          FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("EmailItem.fxml"));
                                          pane = fxmlLoader.load();
                                          ((EmailItemController) fxmlLoader.getController()).init(model, email);
                                        } catch (IOException e) {
                                          e.printStackTrace();
                                        }
                                        return pane;
                                      })
                                      .collect(Collectors.toList())
    );
  }
}
