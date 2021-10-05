package org.group77.mailMe.controller;

import javafx.fxml.*;
import javafx.geometry.*;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.util.Pair;
import org.group77.mailMe.Main;
import org.group77.mailMe.model.*;

import java.io.IOException;


public class StartPageController {
  @FXML private Label welcomeLabel;
  @FXML private BorderPane startPageBorderPane;
  @FXML private Button addAccountButton;
  @FXML private FlowPane accountsFlowPane;

  public void init(Model model) {


    // Add buttons for every stored accounts with actionEvent listeners
    initStoredAccounts(model);
    initAddNewAccountButton(); // TODO add listener
    /*
    accountsListView.setOnMouseClicked(inputEvent -> {
      ((Node) inputEvent.getSource()).getScene().getWindow().hide();
    });
    */
  }

  private void initStoredAccounts(Model model) {
    model.accounts.forEach(account -> {
      Label accountLabel = new Label(account.emailAddress());
      accountsFlowPane.getChildren().add(accountLabel);
      // TODO: ev fit labels to dimensions

      accountLabel.setOnMouseClicked(inputEvent -> {
        model.activeAccount.set(account);
        ((Stage)((Node) inputEvent.getSource()).getScene().getWindow()).close();
        openMaster(model);
      });

    });
  }
  private void openMaster(Model model){
      // initialize StartPageView and its Controller
      try {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("Master.fxml"));
        Pane pane = fxmlLoader.load();
        ((MasterController) fxmlLoader.getController()).init(model);
        Stage stage = new Stage();
        stage.setTitle("MailMe");
        stage.setScene(new Scene(pane));
        stage.show();

      }catch (IOException e){
        e.printStackTrace();
      }
  }

  private void initAddNewAccountButton() {

  }

  public Button getAddAccountButton() {
    return this.addAccountButton;
  }
}