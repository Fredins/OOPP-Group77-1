package org.group77.mailMe.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import org.group77.mailMe.model.Model;

/**
 * For applying filter to search
 *
 * @author David zamanian
 */

public class FilterController {
    @FXML private Button clearFilterButton;
    @FXML private Button addFilterButton;
    @FXML private TextField toTextField;
    @FXML private TextField fromTextField;
    @FXML private DatePicker maxDatePicker;
    @FXML private ChoiceBox<String> timeChoiceBox;


    /** init function
     *
     * @param model
     * @author David Zamanian
     */

    public void init(Model model){
        clearFilterButton.setOnMouseClicked(inputEvent -> {
            toTextField.clear();
            fromTextField.clear();
            maxDatePicker.setValue(null);
        });
        addFilterButton.setOnMouseClicked(inputEvent -> {
            System.out.println("Do something"); //TODO for Hampus to add
        });
        populateChoiceBox();
    }

    private void populateChoiceBox(){
        timeChoiceBox.getItems().clear();
        timeChoiceBox.getItems().add(0, "Newest To Oldest");
        timeChoiceBox.getItems().add(1, "Oldest To Newest");
    }
}