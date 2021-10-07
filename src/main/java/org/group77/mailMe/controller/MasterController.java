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
   * @param model the model
   * @author Martin, David
   */
  public void init(Model model) {
    loadFolders(model.folders, model);

    if (model.accounts != null) {
      populateAccountCombo(model.accounts, model);
    }

    // input handlers
    refreshBtn.setOnAction(i -> {
      try {
        model.refresh();
      } catch (Exception e) {
        e.printStackTrace(); // TODO feedback
      }
    });
    writeBtn.setOnAction(i -> WindowOpener.openWriting(model));
    accountsCombo.setOnAction(i -> {
      Account selected = accountsCombo.getSelectionModel().getSelectedItem();
      if (selected != null) {
        if(selected.emailAddress().equals("Add New Account")) {
          WindowOpener.openAddAccount(model, node -> ((Stage) node.getScene().getWindow()).close());
        }else{
          model.activeAccount.set(selected);
        }
      }
    });

    // change handlers
    model.activeAccount.addListener((ChangeListener<? super Account>) (obs, oldAccount, newAccount) -> {
      accountsCombo.setValue(newAccount);
    });
    model.folders.addListener((ListChangeListener<? super Folder>) changeEvent -> {
      ObservableList<? extends Folder> newFolders =  changeEvent.getList();
      if(!newFolders.isEmpty()){
        loadFolders(newFolders, model);
        model.activeFolder.set(newFolders.get(0));
      }
    });
    model.visibleEmails.addListener((ListChangeListener<? super Email>) changeEvent -> loadEmails(changeEvent.getList(), model));
    model.accounts.addListener((ListChangeListener<? super Account>) changeEvent -> populateAccountCombo(changeEvent.getList(), model));
    model.activeFolder.addListener((ChangeListener<? super Folder>) (obs, oldFolder, newFolder) -> model.visibleEmails.setAll(newFolder.emails()));
    model.readingEmail.addListener((obs, oldEmail, newEmail) -> {
      if (newEmail != null) {
        loadReading(newEmail, model);
      } else {
        readingPane.getChildren().clear();
      }
    });
  }

    /** Populates the comboBox with the emailAddresses of the accounts in model's accounts.
     * Adds an Account with emailAddress "Add New Account" that can be clicked on to open AddAccountView.
     *
     * @param accounts all accounts in model's accounts.
     * @param model holds the state of the application
     * @author Martin, Elin Hagman, David Ågren Zamanian
   */

  private void populateAccountCombo(List<? extends Account> accounts, Model model) {
    accountsCombo.getItems().clear();
    Account addAcc = new Account("Add New Account", new char[]{}, ServerProvider.GMAIL);
    // override toString() and fromString() to make account correspond to its email address
    accountsCombo.setConverter(new StringConverter<>() {
      @Override public String toString(Account account) {
        return account != null ? account.emailAddress() : null;
      }
      @Override public Account fromString(String string) {
        Account account = null;
        try {
          account = model.accounts.stream()
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
   * @param email the email to be displayed
   * @param model the model
   * @author David, Martin
   */
 private void loadReading(Email email, Model model) {
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
   * @param folders all the folders to be loaded
   * @param model the model
   * @author Martin
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
   * @param emails the emails to be displayed
   * @param model the model
   * @author David, Martin
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
