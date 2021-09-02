package org.group77.mejl.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class HelloController {
    @FXML
    private Label label_greeting;

    @FXML
    private void greet() {
        label_greeting.setText("welcome to mejl!");
    }
}