package org.group77.mailMe.controller;

import javafx.fxml.*;
import javafx.scene.control.*;
import org.group77.mailMe.model.*;
import org.group77.mailMe.model.data.*;

public class AddAccountController {
  @FXML private TextField user;
  @FXML private Button addAccountBtn;
  @FXML private PasswordField passwordField;

  void init(Model m){
    // input handlers
    addAccountBtn.setOnAction(i -> addAccount(m));
    passwordField.setOnAction(i -> addAccount(m));
  }

  private void addAccount(Model m){
    try {
      Account account = AccountFactory.createAccount(user.getText(), passwordField.getText().toCharArray());
      m.addAccount(account);
      m.accounts.add(account);
    } catch (Exception e) {
      e.printStackTrace(); // TODO feedback
    };
  }

}
