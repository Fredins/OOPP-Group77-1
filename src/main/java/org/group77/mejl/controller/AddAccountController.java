package org.group77.mejl.controller;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import org.group77.mejl.model.ApplicationManager;

public class AddAccountController {

    ApplicationManager applicationManager;

    @FXML
    private TextField password;

    @FXML
    private TextField user;


    @FXML
    void addAccount(MouseEvent event) {
        applicationManager.addAccount(user.getText(),password.getText());
    }

}
