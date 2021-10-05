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

  void init(Model m) {
    // input handlers
    addAccountBtn.setOnAction(i -> addAccount(m));
    passwordField.setOnAction(i -> addAccount(m));
  }

  private void addAccount(Model m) {
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
}
