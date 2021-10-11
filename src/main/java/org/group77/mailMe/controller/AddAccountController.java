package org.group77.mailMe.controller;

import javafx.event.Event;
import javafx.fxml.*;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.text.Text;
import org.group77.mailMe.model.*;
import org.group77.mailMe.model.Control;
import org.group77.mailMe.model.data.*;
import java.util.function.*;

public class AddAccountController {
  @FXML private TextField emailTextField;
  @FXML private PasswordField passwordField;
  @FXML private Button addAccountButton;
  @FXML private Label errorLabel;

  /**
   * Initializes the AddAccount view.
   * Adds listeners to this addAccountButton and the login TextFields.
   * @param model hold the application state
   * @param onClose function which determines the behaviour when closing the window
   * @author Martin, Elin
   */
  public void init(Control control, Consumer<Node> onClose) {
    addAccountButton.setOnAction(inputEvent -> addAccount(control, inputEvent, onClose));
    emailTextField.setOnKeyTyped(keyEvent -> clearErrorMessage());
    passwordField.setOnKeyTyped(keyEvent -> clearErrorMessage());
  }
  /**
   * Tries to add an account in model from the user input in this emailTextField and this passwordField.
   * If login is successful, sets new account as active account in model and closes window.
   * If login fails, displays an error message thrown by model in this errorLabel.
   * @param model holds the application state
   * @param inputEvent occurs when user presses this addAccountButton
   * @param onClose function which determines the behaviour when closing the window
   * @author Elin Hagman, Martin
   */
  private void addAccount(Control control, Event inputEvent, Consumer<Node> onClose) {
    try {
      control.addAccount(emailTextField.getText(), passwordField.getText());
      // call the closing function
      onClose.accept((Node) inputEvent.getSource());
    } catch (Exception e) {
      errorLabel.setText(e.getMessage()); //TODO: give a good exception message in Model
      // errorLabel.setText("Wrong login credentials, or domain is not supported");
    }
  }
  private void clearErrorMessage() {
    errorLabel.setText("");
  }

}
