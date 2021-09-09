package org.group77.mejl.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import org.group77.mejl.model.AccountInformation;
import org.group77.mejl.model.EmailApp;
import org.group77.mejl.model.Model;

import java.io.IOException;

public class AccountController {
    @FXML
    private TextField password;
    @FXML
    private TextField protocol;
    @FXML
    private TextField host;
    @FXML
    private TextField port;
    @FXML
    private TextField user;
    @FXML
    private TextField identifier;

    private final Model model = new Model();
    private EmailApp emailApp = new EmailApp();



    @FXML
    private void addEmail() throws IOException {
        boolean connection = emailApp.testConnection(identifier.getText(), host.getText(), Integer.parseInt(port.getText()), protocol.getText(), user.getText(), password.getText());
        if (connection) {
            System.out.println("Successfully added email!");
        } else{
            System.out.println("can't connect to email service provider");
            AddEmailErrorController.Error();

        }
    }




}