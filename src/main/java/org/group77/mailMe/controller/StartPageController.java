package org.group77.mailMe.controller;

import javafx.fxml.*;
import javafx.geometry.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import org.group77.mailMe.model.*;

public class StartPageController {
  @FXML private Label welcomeLabel;
  @FXML private BorderPane startPageBorderPane;
  @FXML private Button addAccountButton;
  @FXML
  private final ListView<Label> accountsListView = new ListView<Label>();

  public void init(Model m) {

    startPageBorderPane.setCenter(accountsListView);
    accountsListView.setPrefSize(startPageBorderPane.getPrefWidth() - 20, startPageBorderPane.getPrefHeight() - 20);
    // Add buttons for every stored accounts with actionEvent listeners
    initStoredAccounts(m);
    accountsListView.setOnMouseClicked(i -> {
    });
  }

  private void initStoredAccounts(Model m) {
    m.accounts.forEach(account -> {
      Label accountLabel = new Label(account.emailAddress());
      accountsListView.getItems().add(accountLabel);
      accountLabel.setPrefWidth(accountsListView.getPrefWidth());
      accountLabel.setAlignment(Pos.CENTER);
    });
  }

  public ListView<Label> getAccountsListView() {
    return this.accountsListView;
  }

  public Button getAddAccountButton() {
    return this.addAccountButton;
  }
}