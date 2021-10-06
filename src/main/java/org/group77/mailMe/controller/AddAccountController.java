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
   *
   * Initializes the AddAccount view.
   *
   * Adds listeners to this addAccountButton and the login TextFields.
   *
   * @param model hold the application state
   * @param onClose
   */
  public void init(Model model, Consumer<Node> onClose) {

    addAccountButton.setOnAction(inputEvent -> addAccount(model, inputEvent, onClose));
    emailTextField.setOnKeyTyped(keyEvent -> clearErrorMessage());
    passwordField.setOnKeyTyped(keyEvent -> clearErrorMessage());

    //passwordField.setOnAction(inputEvent -> addAccount(model, inputEvent, onClose));
    // Why did we call this??? ^^^^
  }

  /**
   *
   * Tries to add an account from the user input in this emailTextfield and this passwordField.
   *
   * If login is successful, sets new account as active account in model and closes window.
   * If login fails, displays an error message thrown by Model in this errorLabel.
   *
   * @param model holds the application state
   * @param inputEvent occurs when user presses this addAccountButton
   * @param onClose // TODO: ask martin what this is
   *
   * @author Elin Hagman
   *
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

  private void clearErrorMessage() {
    errorLabel.setText("");
  }

}
