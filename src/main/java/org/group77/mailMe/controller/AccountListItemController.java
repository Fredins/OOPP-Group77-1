package org.group77.mailMe.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import org.group77.mailMe.model.data.Account;

public class AccountListItemController {
    @FXML private Label emailAddressLabel;

    /**
     * 1. set initial values for nodes
     * @param account the corresponding account item
     * @author Elin
     */
    public void init (Account account) {
        emailAddressLabel.setText(account.emailAddress());
        // if we add pictures or more features to account we can add it here

    }
}