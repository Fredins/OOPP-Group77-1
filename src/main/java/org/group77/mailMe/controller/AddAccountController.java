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
import java.util.function.*;

public class AddAccountController {
  @FXML private TextField user;
  @FXML private Button addAccountBtn;
  @FXML private PasswordField passwordField;

  public void init(Model model, Consumer<Node> onClose) {
    // input handlers
    addAccountBtn.setOnAction(inputEvent -> addAccount(model, inputEvent, onClose));
    passwordField.setOnAction(inputEvent -> addAccount(model, inputEvent, onClose));
  }

  /**
   *
   */
  private void addAccount(Model model, Event inputEvent, Consumer<Node> onClose) {
    try {
      Account account = AccountFactory.createAccount(user.getText(), passwordField.getText().toCharArray());
      model.addAccount(account);
      model.accounts.add(account); // TODO change these to be in master controller listener (SRP)
      model.activeAccount.set(account);
      model.createFolders();
      // call the closing function
      onClose.accept((Node) inputEvent.getSource());
    } catch (Exception e) {
      e.printStackTrace(); // TODO feedback
    }
  }

}
