package org.group77.mailMe.controller;

import javafx.fxml.*;
import javafx.scene.control.*;
import org.group77.mailMe.model.*;

public class AddAccountController {
  @FXML private TextField user;
  @FXML private Button addAccountBtn;
  @FXML private PasswordField passwordField;

  void init(Model m){
    // input handlers
    addAccountBtn.setOnAction(i -> m.addAccount(user.getText(), passwordField.getText()));
    passwordField.setOnAction(i -> m.addAccount(user.getText(), passwordField.getText()));
  }

}
