package org.group77.mejl.controller;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import org.group77.mejl.model.ApplicationManager;

public class AddAccountController {

    //TODO possibly move this somewhere else or make use of singleton pattern to avoid
    // multiple AppManagers if not necessary? (Hampus)
    ApplicationManager applicationManager = new ApplicationManager();

    @FXML
    private TextField password;

    @FXML
    private TextField user;


    @FXML
    void addAccount(MouseEvent event) {
        try {
            if (applicationManager.addAccount(user.getText(),password.getText())) {
                // account was successfully created, close AddAccountView
            } else {
                // error, give error message?
            };
        }catch (Exception e){
            e.printStackTrace();
        }

    }

}
