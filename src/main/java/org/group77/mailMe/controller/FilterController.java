package org.group77.mailMe.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
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
    @FXML private Button clearFilterButton; //button for clearing all filters
    @FXML private Button applyFilterButton; //button for applying all chosen filters
    @FXML private TextField toTextField; // text field for filtering on 'to'
    @FXML private TextField fromTextField; // text field for filtering on 'from'
    @FXML private DatePicker maxDatePicker; // date picker for filtering on max date.
    @FXML private DatePicker minDatePicker; // date picker for filtering on min date.
    @FXML private ChoiceBox<String> sortingChoiceBox; //list of sorting alternatives.
    @FXML private AnchorPane rootPane; // the underlying pane which all other components are children of.
    @FXML private Button closeButton; // button for closing the filter view.
    // Strings for the sorting choice box:
    private final String newToOldSorting = "Newest to Oldest";
    private final String oldToNewSorting = "Oldest to Newest";
    // set new to old sorting as default
    private final String defaultSorting = newToOldSorting;

    /** init function
     *
     * @param control
     * @author David Zamanian
     * @author Hampus Jernkrook
     */

    public void init(Control control){
        // upon clearing the filter, clear all fields except the sorting.
        // Sorting instead is set to default.
        clearFilterButton.setOnMouseClicked(inputEvent -> {
            clearAllFilters();
            // notify control to restore active emails
            control.clearFilter();
            // apply default sorting again
            applyDefaultSorting(control);
        });
        // when applying the chosen filters:
        applyFilterButton.setOnMouseClicked(inputEvent -> {
            //clear filter (restore to original emails) before applying anything
            control.clearFilter();
            // apply all filters chosen
            applyAllFilters(control);
        });
        // close button hides the filter view
        closeButton.setOnAction(i -> rootPane.getParent().setVisible(false));
        // populate the list/sorting choice box of possible sorting alternatives:
        populateChoiceBox();
        // set value in sorting choice box to default sorting.
        sortingChoiceBox.setValue(defaultSorting);

        //TODO: ask control to default sort upon initialisation? I.e.
        // control.sortByNewToOld();
    }

    private void populateChoiceBox(){
        sortingChoiceBox.getItems().clear();
        sortingChoiceBox.getItems().add(0, newToOldSorting);
        sortingChoiceBox.getItems().add(1, oldToNewSorting);
    }

    private void applyAllFilters(Control control) {
        // apply all filters chosen
        // if some string is entered in the to-field, then filter on to
        applyToFilter(control);
        applyFromFilter(control);
        applyMaxDateFilter(control);
        applyMinDateFilter(control);
        applySorting(control);
    }

    private void applyToFilter(Control control) {
        // if some string is entered in the to-field, then filter on to
        if (!Objects.equals(toTextField.getText(), "")) {
            System.out.println("GOING TO FILTER ON TO"); //TODO REMOVE
            control.filterOnTo(toTextField.getText());
        }
    }

    private void applyFromFilter(Control control) {
        if (!Objects.equals(fromTextField.getText(), "")) {
            System.out.println("GOING TO FILTER ON FROM"); //TODO REMOVE
            control.filterOnFrom(fromTextField.getText());
        }
    }

    private void applyMaxDateFilter(Control control) {
        if (!Objects.equals(maxDatePicker.getValue(), null)) {
            System.out.println("GOING TO FILTER ON MAX DATE"); //todo remove
            // convert the selected date to LocalDateTime.
            // For max date to be inclusive, set max date to be start of day after the chosen date.
            LocalDateTime date = maxDatePicker.getValue().plusDays(1).atStartOfDay();
            control.filterOnMaxDate(date);
        }
    }

    private void applyMinDateFilter(Control control) {
        if (!Objects.equals(minDatePicker.getValue(), null)) {
            System.out.println("GOING TO FILTER ON MIN DATE"); //todo remove
            // convert the selected date to LocalDateTime. Min date is inclusive.
            LocalDateTime date = minDatePicker.getValue().atStartOfDay();
            control.filterOnMinDate(date);
        }
    }

    private void applySorting(Control control) {
        if (!Objects.equals(sortingChoiceBox.getValue(), null)) {
            System.out.println("TIME CHOICE BOX IS NOT EMPTY"); //TODO REMOVE
            if (Objects.equals(sortingChoiceBox.getValue(), newToOldSorting)) {
                System.out.println("NEW TO OLD SORTING APPLIED"); //TODO REMOVE
                control.sortByNewToOld();
            } else if (Objects.equals(sortingChoiceBox.getValue(), oldToNewSorting)) {
                System.out.println("OLD TO NEW SORTING APPLIED"); //TODO REMOVE
                control.sortByOldToNew();
            }
        }
    }

    private void clearAllFilters() {
        toTextField.clear();
        fromTextField.clear();
        maxDatePicker.setValue(null);
        minDatePicker.setValue(null);
        sortingChoiceBox.setValue(defaultSorting);
    }

    private void applyDefaultSorting(Control control) {
        control.sortByNewToOld();
    }
}