package org.group77.mailMe.controller;

import javafx.event.Event;
import javafx.fxml.*;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.text.Text;
import org.group77.mailMe.model.*;
import org.group77.mailMe.model.data.*;
import java.util.function.*;

public class AddAccountController {
  @FXML
  private TextField emailTextField;

  @FXML
  private PasswordField passwordField;

  @FXML
  private Button addAccountButton;

  @FXML
  private Label errorLabel;

  /**
   * add event handlers to nodes
   */
  public void init(Model model, Consumer<Node> onClose) {
    // input handlers
    addAccountButton.setOnAction(inputEvent -> addAccount(model, inputEvent, onClose));
    //passwordField.setOnAction(inputEvent -> addAccount(model, inputEvent, onClose));
  }

  /**
   * 1. create and add account
   * 2. close the window according to Consumer onClose
   */
  private void addAccount(Model model, Event inputEvent, Consumer<Node> onClose) {
    try {
      Account account = AccountFactory.createAccount(emailTextField.getText(), passwordField.getText().toCharArray());
      model.addAccount(account);
      // call the closing function
      onClose.accept((Node) inputEvent.getSource());
    } catch (Exception e) {
      // errorLabel.setText(e.getMessage()); TODO: give a good exception message in Model
      errorLabel.setText("Wrong login credentials, or domain is not supported");
    }
  }

}
