package org.group77.mailMe.controller;

import javafx.fxml.*;
import javafx.geometry.*;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import org.group77.mailMe.model.*;

public class StartPageController {
  @FXML private Label welcomeLabel;
  @FXML private BorderPane startPageBorderPane;
  @FXML private Button addAccountButton;
  @FXML private FlowPane accountsFlowPane;

  public void init(Model model) {


    // Add buttons for every stored accounts with actionEvent listeners
    initStoredAccounts(m);
    accountsListView.setOnMouseClicked(i -> {
    });
  }

  private void initStoredAccounts(Model model) {
    model.accounts.forEach(account -> {
      Label accountLabel = new Label(account.emailAddress());
      accountsFlowPane.getChildren().add(accountLabel);
      // TODO: ev fit labels to dimensions

      accountLabel.setOnMouseClicked(inputEvent -> {
        model.activeAccount.set(new Pair<>(true,account));
        ((Stage)((Node) inputEvent.getSource()).getScene().getWindow()).close();
        openMaster(model);
      });

    });
  }

  private void initAddNewAccountButton() {

  }

  public Button getAddAccountButton() {
    return this.addAccountButton;
  }
}