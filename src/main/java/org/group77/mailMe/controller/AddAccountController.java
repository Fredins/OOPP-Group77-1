package org.group77.mailMe.controller;

import javafx.event.Event;
import javafx.fxml.*;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.util.Pair;
import org.group77.mailMe.Main;
import org.group77.mailMe.model.*;
import org.group77.mailMe.model.data.*;

import java.io.IOException;

public class AddAccountController {
  @FXML private TextField user;
  @FXML private Button addAccountBtn;
  @FXML private PasswordField passwordField;

  public void init(Model model) {
    // input handlers
    addAccountBtn.setOnAction(inputEvent -> addAccount(model, inputEvent));
    passwordField.setOnAction(inputEvent -> addAccount(model, inputEvent));
  }

  private void addAccount(Model model, Event inputEvent) {
    try {
      Account account = AccountFactory.createAccount(user.getText(), passwordField.getText().toCharArray());
      model.addAccount(account);
      model.accounts.add(account);
      model.activeAccount.set(new Pair<>(true, account));
      model.createFolders();
      ((Stage)((Node) inputEvent.getSource()).getScene().getWindow()).close();
      openMaster(model);

    } catch (Exception e) {
      e.printStackTrace(); // TODO feedback
    }
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
}
