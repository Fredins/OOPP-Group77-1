package org.group77.mejl.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import org.group77.mejl.model.Model;

import java.util.Properties;

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
        // TODO possibly convert props to custom data class (thin class)
        Properties props = new Properties();
        props.setProperty("password", password.getText());
        props.setProperty("protocol", protocol.getText());
        props.setProperty("host", host.getText());
        props.setProperty("port", port.getText());
        props.setProperty("user", user.getText());
        props.setProperty("identifier", identifier.getText());
        System.out.println("storing props");
        model.storeProps(props);

    }
}