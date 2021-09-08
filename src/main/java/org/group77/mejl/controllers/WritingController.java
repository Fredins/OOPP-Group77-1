package org.group77.mejl.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

public class WritingController {

    @FXML
    private TextField toTextField;

    @FXML
    private TextField fromTextField;

    @FXML
    private TextField contentTextfield;

    @FXML
    private Button sendButton;

    @FXML
    private TextField subjectTextField;

    @FXML
    void sendEmail(ActionEvent event) {
        // EmailApp sendEmail function
    }

}
