package org.group77.mailMe.controller;

import javafx.fxml.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import org.group77.mailMe.Control;
import org.group77.mailMe.model.Email;

/**
 * Controller for EmailItem
 */
public class EmailItemController {
    @FXML
    private AnchorPane root;
    @FXML
    private Label fromLabel;
    @FXML
    private Label subjectLabel;
    @FXML
    private Button button;

    /**
     * 1. set initial values for nodes
     * 2. set event handlers for nodes
     *
     * @param control the control layer
     * @param email   the corresponding email
     * @author David Zamanian
     * @author Martin Fredin
     */
    void init(Control control, Email email) {
        fromLabel.setText(email.from());
        subjectLabel.setText(email.subject());

        // attach event handlers
        button.setOnMouseClicked(inputEvent -> control.setActiveEmail(email));
        //Adds a dropShadow effect when selecting an email
        control.getActiveEmail().addObserver(newEmail -> dropShadow(newEmail, email));
    }

    /**
     *
     * @param newEmail the new email
     * @param email    the email associated with this controller
     * @author Martin Fredin
     */
    private void dropShadow(Email newEmail, Email email) {
        if (email.equals(newEmail)) {
            button.getStyleClass().add("dropshadow");
        } else {
            button.getStyleClass().remove("dropshadow");
        }

    }
}
