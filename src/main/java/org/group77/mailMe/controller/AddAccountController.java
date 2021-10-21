package org.group77.mailMe.controller;

import javafx.event.Event;
import javafx.fxml.*;
import javafx.geometry.*;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.text.Text;
import javafx.util.*;
import org.controlsfx.control.*;
import org.group77.mailMe.Control;

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
     * Initializes the AddAccount view.
     * Adds listeners to this addAccountButton and the login TextFields.
     *
     * @param control the control layer
     * @param onClose function which determines the behaviour when closing the window
     * @author Martin Fredin
     * @author Elin Hagman
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
     *
     * @param control    the control layer
     * @param inputEvent occurs when user presses this addAccountButton
     * @param onClose    function which determines the behaviour when closing the window
     * @author Elin Hagman, Martin Fredin
     */
    private void addAccount(Control control, Event inputEvent, Consumer<Node> onClose) {
        Notifications notification = Notifications.create()
                .position(Pos.TOP_CENTER)
                .hideAfter(Duration.seconds(2));
        try {
            control.addAccount(emailTextField.getText(), passwordField.getText());
            // call the closing function
            onClose.accept((Node) inputEvent.getSource());
            notification
                    .graphic(new Label(emailTextField.getText() + " added successfully"))
                    .show();
        } catch (Exception e) {
            notification
                    .title("Failure")
                    .text(e.getMessage())
                    .showWarning();
        }
    }

    /**
     * clears the error message
     * @author Elin Hagman
     */
    private void clearErrorMessage() {
        errorLabel.setText("");
    }

}
