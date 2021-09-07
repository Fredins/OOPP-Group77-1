package org.group77.mejl.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import org.group77.mejl.model.AccountInformation;
import org.group77.mejl.model.EmailApp;
import org.group77.mejl.model.Model;

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
    private void addEmail(){
        boolean connection = emailApp.testConnection(identifier.getText(), host.getText(), Integer.parseInt(port.getText()), protocol.getText(), user.getText(), password.getText());
        if (connection) {

        } else{
            System.out.println("can't connect to email service provider");
        }
    }



    @FXML
    private void addEmaill(){
        AccountInformation accountInformation = new AccountInformation(
                identifier.getText(),
                host.getText(),
                Integer.parseInt(port.getText()),
                protocol.getText(),
                user.getText(),
                password.getText()
        );
        if(model.connectESP(accountInformation)){
            model.writeESP(accountInformation);
        }else{
            System.out.println("can't connect to email service provider");
        }
    }
}