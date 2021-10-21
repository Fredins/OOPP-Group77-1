package org.group77.mailMe.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import org.group77.mailMe.model.Account;

/**
 * Controller class for the AccountListItem
 */
public class AccountListItemController {
    @FXML
    private Label emailAddressLabel;

    /**
     * 1. set initial values for nodes
     *
     * @param account the corresponding account item
     * @author Elin Hagman
     */
    public void init(Account account) {
        emailAddressLabel.setText(account.emailAddress());
    }
}