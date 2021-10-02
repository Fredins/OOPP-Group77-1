package org.group77.mailMe.controller;

import javafx.beans.value.*;
import javafx.collections.*;
import javafx.fxml.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.image.*;
import javafx.scene.layout.*;
import javafx.stage.*;
import javafx.util.*;
import org.group77.mailMe.*;
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

  public void init(Model m) {
    loadFolders(m.folders, m);
    if (m.accounts != null) {
      populateAcountCombo(m.accounts, m);
    }

    // change handler
    m.folders.addListener((ListChangeListener<? super Folder>) c -> loadFolders(c.getList(), m));
    m.visibleEmails.addListener((ListChangeListener<? super Email>) c -> loadEmails(c.getList(), m));

    m.readingEmail.addListener((ChangeListener<? super Pair<Boolean, Email>>) (o, op, p) -> {
      if (p.getKey()) {
        loadReading(p.getValue(), m);
      } else {
        readingPane.getChildren().clear();
      }
    });

    // input handlers
    refreshBtn.setOnAction(i -> refresh(m));
    addAccountBtn.setOnAction(i -> openAddAccount(m));
    writeBtn.setOnAction(i -> openWriting(m));
    accountsCombo.setOnAction(i -> setActiveAccount(m));

    // change handlers
    m.accounts.addListener((ListChangeListener<? super Account>) c -> populateAcountCombo(c.getList(), m));
  }

  private void setActiveAccount(Model m) {
    Account selected = accountsCombo.getSelectionModel().getSelectedItem();
    if (selected != null) {
      m.activeAccount.set(new Pair<>(true, selected));
    }
  }

  @FXML AnchorPane startPagePane;
  @FXML private AnchorPane startPageContentPane;
  /* TODO reimplement elin's startpage
  private void openStartPage() throws IOException {

    // initialize StartPageView and its Controller
    FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("StartPageView.fxml"));
    startPageContentPane.getChildren().add(fxmlLoader.load());
    StartPageController startPageController = fxmlLoader.getController();
    startPageController.init(appManager);

    // add OnMouseClickedListeners to buttons
    List<Label> emailAddressesLabels = startPageController.getAccountsListView().getItems();
    for (Label emailAddressLabel : emailAddressesLabels) {
      emailAddressLabel.setOnMouseClicked(actionEvent -> {
        appManager.setActiveAccount(emailAddressLabel.getText());
        System.out.println("Active account: " + appManager.getActiveAccount().getEmailAddress());
        startPagePane.toBack();
      });
    }

    startPageController.getAddAccountButton().setOnAction(EventHandler -> {
      try {
        openEmailSettings();
      } catch (IOException exception) {
        exception.printStackTrace();
      }
      startPagePane.toBack();
    });

    if (appManager.getEmailAddresses().size() == 0) {
      // open accountview
      startPagePane.toBack();
    } else if (appManager.getEmailAddresses().size() == 1) {
      startPagePane.toBack();
      appManager.setActiveAccount(appManager.getEmailAddresses().get(0));
      System.out.println("Active account: " + appManager.getActiveAccount().getEmailAddress());
    } else {
      startPagePane.toFront();
    }

  }
   */


  private void refresh(Model m) {
    try {
      m.refresh();
    } catch (Exception e) {
      e.printStackTrace(); // TODO feedback
    }
  }

  private void populateAcountCombo(List<? extends Account> accounts, Model m) {
    accountsCombo.getItems().clear();
    accountsCombo.setConverter(new StringConverter<>() {
      @Override public String toString(Account account) {
        return account != null ? account.emailAddress() : null;
      }
      @Override public Account fromString(String string) {
        Account account = null;
        try {
          account = m.accounts.stream()
            .filter(a -> a.emailAddress().equals(string))
            .findAny()
            .orElseThrow(Exception::new);
        } catch (Exception e) {
          e.printStackTrace();
        }
        return account;
      }
    });
    accountsCombo.getItems().addAll(accounts);
  }

  void loadReading(Email e, Model m) {
    try {
      FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("Reading.fxml"));
      Pane pane = fxmlLoader.load();
      ((ReadingController) fxmlLoader.getController()).init(m, e);
      readingPane.getChildren().clear();
      readingPane.getChildren().add(pane);
    } catch (IOException e1) {
      e1.printStackTrace();
    }
  }

  void openAddAccount(Model m) {
    System.out.println("open account");
    try {
      FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("AddAccount.fxml"));
      Pane pane = fxmlLoader.load();
      ((AddAccountController) fxmlLoader.getController()).init(m);
      Stage stage = new Stage();
      stage.setTitle("Add Account");
      stage.setScene(new Scene(pane));
      stage.show();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
  void openWriting(Model m) {
    try {
      FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("Writing.fxml"));
      Pane pane = fxmlLoader.load();
      ((WritingController) fxmlLoader.getController()).init(m);
      Stage stage = new Stage();
      stage.setTitle("Add Account");
      stage.setScene(new Scene(pane));
      stage.show();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
  private void loadFolders(List<? extends Folder> folders, Model m) {
    foldersFlow.getChildren().clear();
    foldersFlow.getChildren().addAll(folders
                                       .stream()
                                       .map(f -> {
                                         Pane pane = null;
                                         try {
                                           FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("FolderItem.fxml"));
                                           pane = fxmlLoader.load();
                                           ((FolderItemController) fxmlLoader.getController()).init(m, f);
                                         } catch (IOException e) {
                                           e.printStackTrace();
                                         }
                                         return pane;
                                       })
                                       .collect(Collectors.toList()));
  }

  private void loadEmails(List<? extends Email> emails, Model m) {
    emailsFlow.getChildren().clear();
    emailsFlow.getChildren().addAll(emails
                                      .stream()
                                      .map(e -> {
                                        Pane pane = null;
                                        try {
                                          FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("EmailItem.fxml"));
                                          pane = fxmlLoader.load();
                                          ((EmailItemController) fxmlLoader.getController()).init(m, e);
                                        } catch (IOException e1) {
                                          e1.printStackTrace();
                                        }
                                        return pane;
                                      })
                                      .collect(Collectors.toList())
    );
  }
}
