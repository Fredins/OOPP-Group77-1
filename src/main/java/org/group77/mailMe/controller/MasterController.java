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

  public void init(Model model) {
    loadFolders(model.folders, model);
    if (model.accounts != null) {
      populateAcountCombo(model.accounts, model);
    }


    // change handler
    model.folders.addListener((ListChangeListener<? super Folder>) c -> loadFolders(c.getList(), model));
    model.visibleEmails.addListener((ListChangeListener<? super Email>) c -> loadEmails(c.getList(), model));

    model.readingEmail.addListener((ChangeListener<? super Pair<Boolean, Email>>) (o, op, p) -> {
      if (p.getKey()) {
        loadReading(p.getValue(), model);
      } else {
        readingPane.getChildren().clear();
      }
    });

    // input handlers
    refreshBtn.setOnAction(i -> refresh(model));
    addAccountBtn.setOnAction(i -> openAddAccount(model));
    writeBtn.setOnAction(i -> openWriting(model));
    accountsCombo.setOnAction(i -> setActiveAccount(model));


    // change handlers
    model.accounts.addListener((ListChangeListener<? super Account>) c -> {
      populateAcountCombo(c.getList(), model);
      if(c.getList().size() == 1){
        model.activeAccount.set(new Pair<>(true, c.getList().get(0)));
      }
    });
  }

  private void setActiveAccount(Model m) {
    Account selected = accountsCombo.getSelectionModel().getSelectedItem();
    if (selected != null) {
      m.activeAccount.set(new Pair<>(true, selected));
    }
  }

  @FXML AnchorPane startPagePane;
  @FXML private AnchorPane startPageContentPane;
  //TODO reimplement elin's startpage

  private void refresh(Model m) {
    try {
      m.refresh();
    } catch (Exception e) {
      e.printStackTrace(); // TODO feedback
    }
  }

  private void populateAcountCombo(List<? extends Account> accounts, Model m) {
    accountsCombo.getItems().clear();
    Account addAcc = new Account("Add New Account", new char[]{}, ServerProvider.GMAIL_PROVIDER);
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
    accountsCombo.getItems().add(addAcc);
  }

  void loadReading(Email email, Model model) {
    try {
      FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("Reading.fxml"));
      Pane pane = fxmlLoader.load();
      ((ReadingController) fxmlLoader.getController()).init(model, email);
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
      Stage stage = new Stage();
      Pane pane = fxmlLoader.load();
      ((WritingController) fxmlLoader.getController()).init(m);
      stage.setTitle("New MeMail");
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
