package org.group77.mailMe.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import org.group77.mailMe.model.Control;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * For applying filter to search
 *
 * @author David Zamanian
 * @author Hampus Jernkrook
 */

public class FilterController {
    @FXML private Button clearFilterButton;
    @FXML private Button addFilterButton;
    @FXML private TextField toTextField;
    @FXML private TextField fromTextField;
    @FXML private DatePicker maxDatePicker;
    @FXML private DatePicker minDatePicker;
    @FXML private ChoiceBox<String> timeChoiceBox;


    /** init function
     *
     * @param control
     * @author David Zamanian
     * @author Hampus Jernkrook
     */

    public void init(Control control){
        clearFilterButton.setOnMouseClicked(inputEvent -> {
            toTextField.clear();
            fromTextField.clear();
            maxDatePicker.setValue(null);
            minDatePicker.setValue(null);
            // notify control to restore active emails
            System.out.println("FILTERS ARE CLEARED!"); //todo remoVE
            control.clearFilter();
        });
        addFilterButton.setOnMouseClicked(inputEvent -> {
            //clear filter before applying anything
            control.clearFilter();
            // apply all filters chosen
            System.out.println("Do something"); //TODO for Hampus to add
            // if some string is entered in the to-field, then filter on to
            if (!Objects.equals(toTextField.getText(), "")) {
                System.out.println("GOING TO FILTER ON TO"); //TODO REMOVE
                control.filterOnTo(toTextField.getText());
            }
            if (!Objects.equals(fromTextField.getText(), "")) {
                System.out.println("GOING TO FILTER ON FROM"); //TODO REMOVE
                control.filterOnFrom(fromTextField.getText());
            }
            if (!Objects.equals(maxDatePicker.getValue(), null)) {
                System.out.println("GOING TO FILTER ON MAX DATE"); //todo remove
                // convert the selected date to LocalDateTime
                LocalDateTime date = maxDatePicker.getValue().atStartOfDay();
                control.filterOnMaxDate(date);
            }
            if (!Objects.equals(minDatePicker.getValue(), null)) {
                System.out.println("GOING TO FILTER ON MIN DATE"); //todo remove
                // convert the selected date to LocalDateTime
                LocalDateTime date = minDatePicker.getValue().atStartOfDay();
                control.filterOnMinDate(date);
            }
        });
        populateChoiceBox();
    }

    private void populateChoiceBox(){
        timeChoiceBox.getItems().clear();
        timeChoiceBox.getItems().add(0, "Newest To Oldest");
        timeChoiceBox.getItems().add(1, "Oldest To Newest");
    }
}