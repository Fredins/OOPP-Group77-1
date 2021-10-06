package org.group77.mailMe.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import org.group77.mailMe.model.data.Account;

public class AccountListItemController {

    @FXML
    private Label emailAddressLabel;

    @FXML
    private AnchorPane backgroundPane;

    public void init (Account account) {
        emailAddressLabel.setText(account.emailAddress());
        // if we add pictures or more features to account we can add it here

    }



    public AnchorPane getBackgroundPane() {
        return backgroundPane;
    }
}