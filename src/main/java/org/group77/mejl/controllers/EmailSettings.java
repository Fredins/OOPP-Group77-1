package org.group77.mejl.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import org.group77.mejl.model.ESP;
import org.group77.mejl.model.Model;

public class EmailSettings {
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

    @FXML
    private void addEmail(){
        // TODO check if connection is established
        ESP esp = new ESP(
                identifier.getText(),
                host.getText(),
                Integer.parseInt(port.getText()),
                protocol.getText(),
                user.getText(),
                password.getText()
        );
        model.writeESP(esp);

    }
}