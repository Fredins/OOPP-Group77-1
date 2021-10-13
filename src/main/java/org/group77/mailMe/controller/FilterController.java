package org.group77.mailMe.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import org.group77.mailMe.model.Model;

public class FilterController {
    @FXML private Button clearFilterButton;
    @FXML private Button addFilterButton;
    @FXML private TextField toTextField;
    @FXML private TextField fromTextField;
    @FXML private DatePicker maxDatePicker;




    void init (Model model){
        clearFilterButton.setOnMouseClicked(inputEvent -> {
            toTextField.clear();
            fromTextField.clear();
            maxDatePicker.setValue(null);
        });
        addFilterButton.setOnMouseClicked(inputEvent -> {
            System.out.println("Do something");
        });
    }
}