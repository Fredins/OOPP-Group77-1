package org.group77.mailMe.controller;

import javafx.collections.*;
import javafx.fxml.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.*;
import javafx.util.*;
import org.group77.mailMe.*;
import org.group77.mailMe.controller.utils.*;
import org.group77.mailMe.model.Control;
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
  @FXML private Button filterButton;
  @FXML private Button addAccountBtn;
  @FXML private FlowPane emailsFlow;
  @FXML private FlowPane filterFlowPane;


  /**
   * 1. load/display folders in flowPane
   * 2. populate account comboBox
   * 3. add event handlers to nodes and state fields
   * @param control the model
   * @author Martin, David
   */
  public void init(Control control) {
    loadFolders(control.getFolders().get(), control);
    if (control.getAccounts() != null) {
      populateAccountCombo(control.getAccounts().get(), control);
    }
    if(control.getActiveAccount().get() != null){
      accountsCombo.setValue(control.getActiveAccount().get());
    }


    //previous version: // filterButton.setOnAction(i -> WindowOpener.openFilter(control));
    // open filter view in background without displaying to user.
    loadFilterView(control);
    // upon pressing the filter button, show filter view if not shown, or hide it if currently shown.
    filterButton.setOnAction(i -> {
      // TODO: this is not the best from a user friendly standpoint.
      //  To utilize a 'cancel button' in filterview instead, need extra class between filterController
      //   and Mastercontroller? If so, use this row here:
      // filterFlowPane.setVisible(true);
      filterFlowPane.setVisible(!filterFlowPane.isVisible());
    });

    // attach event handlers
    refreshBtn.setOnAction(i -> refresh(control));
    writeBtn.setOnAction(i -> WindowOpener.openWriting(control));
    addAccountBtn.setOnAction(inputEvent -> WindowOpener.openAddAccount(control, node -> ((Stage) node.getScene().getWindow()).close()));
    filterButton.setOnAction(i -> WindowOpener.openFilter(control));
    control.getActiveAccount().addObserver(newAccount -> accountsCombo.setValue(newAccount));
    control.getFolders().addObserver(newFolders -> handleFoldersChange(newFolders, control));
    control.getActiveEmails().addObserver(newEmails -> loadEmails(newEmails, control));
    control.getAccounts().addObserver(newEmails -> accountsCombo.setItems(FXCollections.observableList(newEmails)));
    control.getActiveFolder().addObserver(newFolder -> control.getActiveEmails().replaceAll(newFolder.emails()));
    control.getActiveEmail().addObserver(newEmail -> handleActiveEmailChange(newEmail, control));
    control.getActiveFolder().addObserver(newFolder -> handleActiveFolderChange(control));
  }

  /**
   * @author Martin
   * @param control the control layer
   */
  private void refresh(Control control){
    try {
      control.refresh();
    } catch (Exception e) {
      e.printStackTrace(); // TODO feedback
    }
  }

  /**
   * when folders change:
   * 1. load folders if not empty
   * 2. set active folder to inbox
   * @author Martin
   * @param control the control layer
   * @param newFolders the new folders
   */
  private void handleFoldersChange(List<Folder> newFolders, Control control){
    if(!newFolders.isEmpty()){
      loadFolders(newFolders, control);
      control.getActiveFolder().set(newFolders.get(0));
    }
  }

  /**
   * when active email change:
   * 1. load email in reading pane or clear it
   * @author Martin
   * @param newEmail the new Email
   * @param control the control layer
   */
  private void handleActiveEmailChange(Email newEmail, Control control){
    if (newEmail != null) {
      loadReading(newEmail, control);
    } else {
      readingPane.getChildren().clear();
    }
  }

  /**
   * when active folder change:
   * 1. clear reading pane
   * @author Martin
   * @param control the control layer
   */
    private void handleActiveFolderChange(Control control){
      if (control.getActiveEmail() != null){
        readingPane.getChildren().clear();}

    }

    /**
     * Populates the comboBox with the emailAddresses of the accounts in model's accounts.
     * Adds an Account with emailAddress "Add New Account" that can be clicked on to open AddAccountView.
     *
     * @param accounts all accounts in model's accounts.
     * @param control  holds the state of the application
     * @author Martin, Elin Hagman, David Ågren Zamanian
     */

    private void populateAccountCombo(List<? extends Account> accounts, Control control) {
        accountsCombo.getItems().addAll(accounts);
        accountsCombo.setOnAction(inputEvent -> control.getActiveAccount().set(accountsCombo.getValue()));
        accountsCombo.setConverter(new StringConverter<>() {
            @Override
            public String toString(Account account) {
                return account != null ? account.emailAddress() : null;
            }

            @Override
            public Account fromString(String string) {
                Account account = null;
                try {
                    account = control.getAccounts().stream()
                            .filter(acc -> acc.emailAddress().equals(string))
                            .findAny()
                            .orElseThrow(Exception::new);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return account;
            }
        });
        accountsCombo.setButtonCell(new ListCell<>() {
            @Override
            protected void updateItem(Account item, boolean empty) {
                if (control.getActiveAccount().get() != null) {
                    setText(control.getActiveAccount().get().emailAddress());
                }
            }
        });
    }

    /**
     * loads/display email
     *
     * @param email   the email to be displayed
     * @param control the model
     * @author David, Martin
     */
    private void loadReading(Email email, Control control) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("Reading.fxml"));
            Pane pane = fxmlLoader.load();
            ((ReadingController) fxmlLoader.getController()).init(control, email);
            readingPane.getChildren().clear();
            readingPane.getChildren().add(pane);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 1. initialize every folder in state field folders
     * 2. display every folder in the folder flowPane
     *
     * @param folders all the folders to be loaded
     * @param control the model
     * @author Martin
     */
    private void loadFolders(List<Folder> folders, Control control) {
        foldersFlow.getChildren().clear();
        foldersFlow.getChildren().addAll(folders
                .stream()
                .map(folder -> {
                    Pane pane = null;
                    try {
                        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("FolderItem.fxml"));
                        pane = fxmlLoader.load();
                        ((FolderItemController) fxmlLoader.getController()).init(control, folder);
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
     *
     * @param emails  the emails to be displayed
     * @param control the model
     * @author David, Martin
     */
    private void loadEmails(List<Email> emails, Control control) {
        emailsFlow.getChildren().clear();
        emailsFlow.getChildren().addAll(emails
                .stream()
                .map(email -> {
                    Pane pane = null;
                    try {
                        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("EmailItem.fxml"));
                        pane = fxmlLoader.load();
                        ((EmailItemController) fxmlLoader.getController()).init(control, email);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    return pane;
                })
                .collect(Collectors.toList())
        );
    }

    /**
     * Load the FilterView in separate flowpane. Hide it upon initialisation (do not cover MainView).
     * @param control the control class to pass as argument to the FilterController.
     * @author Hampus Jernkrook
     * @author David Zamanian
     */
    private void loadFilterView(Control control) {
        System.out.println("INIT FILTER FIEW"); //todo remove
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("FilterView.fxml"));
            Pane pane = fxmlLoader.load();
            ((FilterController) fxmlLoader.getController()).init(control);
            filterFlowPane.getChildren().clear();
            filterFlowPane.getChildren().add(pane);
            filterFlowPane.setVisible(false);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
