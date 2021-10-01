package org.group77.mailMe.controller;

import javafx.beans.*;
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
  @FXML private ComboBox<?> accountsCombo;
  @FXML private TextField searchField;
  @FXML private ImageView searchImg;
  @FXML private ImageView accountImg;
  @FXML private FlowPane emailsFlow;

  public void init(Model m) {
    loadFolders(m.folders, m);
    loadEmails(m.visibleEmails, m);

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

    // input handler
    refreshBtn.setOnAction(i -> m.refresh());
    addAccountBtn.setOnAction(i -> openAddAccount(m));
    writeBtn.setOnAction(i -> openWriting(m));

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
