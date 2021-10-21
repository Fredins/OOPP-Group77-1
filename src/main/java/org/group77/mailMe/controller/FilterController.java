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

public class FilterController implements FilterControl {
    @FXML
    private Button clearFilterButton; //button for clearing all filters
    @FXML
    private Button applyFilterButton; //button for applying all chosen filters
    @FXML
    private TextField toTextField; // text field for filtering on 'to'
    @FXML
    private TextField fromTextField; // text field for filtering on 'from'
    @FXML
    private DatePicker maxDatePicker; // date picker for filtering on max date.
    @FXML
    private DatePicker minDatePicker; // date picker for filtering on min date.
    @FXML
    private ChoiceBox<String> sortingChoiceBox; //list of sorting alternatives.
    @FXML
    private AnchorPane rootPane; // the underlying pane which all other components are children of.
    @FXML
    private Button closeButton; // button for closing the filter view.
    // Strings for the sorting choice box:
    private final String newToOldSorting = "Newest to Oldest";
    private final String oldToNewSorting = "Oldest to Newest";
    // set new to old sorting as default
    private final String defaultSorting = newToOldSorting;
    private SearchControl searchControl; // component responsible for sorting

    /**
     * Initialise by adding actions to all buttons and set default sorting.
     * Also set component for search control.
     *
     * @param control       - the class to delegate on to towards the backend.
     * @param searchControl - the class responsible for sorting.
     * @author Hampus Jernkrook
     */
    public void init(Control control, SearchControl searchControl) {
        this.searchControl = searchControl;
        init(control);
    }

    /**
     * Upon initialisation: add actions to all buttons and set default sorting.
     *
     * @param control - the class to delegate on to towards the backend.
     * @author David Zamanian
     * @author Hampus Jernkrook
     */
    public void init(Control control) {
        // upon clearing the filter, clear all fields except the sorting.
        // Sorting instead is set to default.
        clearFilterButton.setOnMouseClicked(inputEvent -> {
            clearAllFilters();
            // notify control to restore active emails
            control.clearFilter();
            // apply default sorting again
            applyDefaultSorting(control);
            // ask search control to apply the searching again
            searchControl.applySearch(control);
        });
        // when applying the chosen filters:
        applyFilterButton.setOnMouseClicked(inputEvent -> {
            //clear filter (restore to original emails) before applying anything
            control.clearFilter();
            // apply potential search via search control
            searchControl.applySearch(control);
            // apply all filters chosen
            applyAllFilters(control);
        });
        // close button hides the filter view
        closeButton.setOnAction(i -> rootPane.getParent().setVisible(false));
        // populate the list/sorting choice box of possible sorting alternatives:
        populateChoiceBox();
        // set value in sorting choice box to default sorting.
        sortingChoiceBox.setValue(defaultSorting);

        // apply default sorting of inbox upon init
        applyDefaultSorting(control);
    }

    /**
     * Populate the sorting choice box with sorting alternatives.
     *
     * @author David Zamanian
     */
    private void populateChoiceBox() {
        sortingChoiceBox.getItems().clear();
        sortingChoiceBox.getItems().add(0, newToOldSorting);
        sortingChoiceBox.getItems().add(1, oldToNewSorting);
    }

    /**
     * Applies all filters.
     *
     * @param control - the class to delegate on to.
     * @author Hampus Jernkrook
     */
    private void applyAllFilters(Control control) {
        // apply all filters chosen
        // if some string is entered in the to-field, then filter on to
        applyToFilter(control);
        applyFromFilter(control);
        applyMaxDateFilter(control);
        applyMinDateFilter(control);
        sort(control);
    }

    /**
     * Apply all filters. Interface method.
     *
     * @param control - the class to delegate on to.
     * @author Hampus Jernkrook
     */
    public void applyFilter(Control control) {
        applyAllFilters(control);
    }

    /**
     * Apply the selected sorting order.
     *
     * @param control - the class to delegate on to.
     * @author Hampus Jernkrook
     */
    public void applySorting(Control control) {
        sort(control);
    }

    /**
     * Applies the filter on 'to'.
     *
     * @param control - the class to delegate on to.
     * @author Hampus Jernkrook
     */
    private void applyToFilter(Control control) {
        // if some string is entered in the to-field, then filter on to
        if (!Objects.equals(toTextField.getText(), "")) {
            control.filterOnTo(toTextField.getText());
        }
    }

    /**
     * Applies the filter on 'from'.
     *
     * @param control - the class to delegate on to.
     * @author Hampus Jernkrook
     */
    private void applyFromFilter(Control control) {
        // if some text is entered into the text field, then apply the filtering.
        if (!Objects.equals(fromTextField.getText(), "")) {
            control.filterOnFrom(fromTextField.getText());
        }
    }

    /**
     * Applies the filter on max date.
     *
     * @param control - the class to delegate on to.
     * @author Hampus Jernkrook
     */
    private void applyMaxDateFilter(Control control) {
        // if some max date is chosen, filter on that max date.
        if (!Objects.equals(maxDatePicker.getValue(), null)) {
            // convert the selected date to LocalDateTime.
            // For max date to be inclusive, set max date to be start of day after the chosen date.
            LocalDateTime date = maxDatePicker.getValue().plusDays(1).atStartOfDay();
            control.filterOnMaxDate(date);
        }
    }

    /**
     * Applies the filter on min date.
     *
     * @param control - the class to delegate on to.
     * @author Hampus Jernkrook
     */
    private void applyMinDateFilter(Control control) {
        // if some min date is chosen, filter on that min date.
        if (!Objects.equals(minDatePicker.getValue(), null)) {
            // convert the selected date to LocalDateTime. Min date is inclusive.
            LocalDateTime date = minDatePicker.getValue().atStartOfDay();
            control.filterOnMinDate(date);
        }
    }

    /**
     * Applies the chosen sorting order.
     *
     * @param control - the class to delegate on to.
     * @author Hampus Jernkrook
     */
    private void sort(Control control) {
        // if there is some sorting choice made, then apply the chosen order.
        if (!Objects.equals(sortingChoiceBox.getValue(), null)) {
            if (Objects.equals(sortingChoiceBox.getValue(), newToOldSorting)) {
                control.sortByNewToOld();
            } else if (Objects.equals(sortingChoiceBox.getValue(), oldToNewSorting)) {
                control.sortByOldToNew();
            }
        }
    }

    /**
     * Clear all filters and sets the sorting choice to default.
     *
     * @author Hampus Jernkrook
     */
    private void clearAllFilters() {
        toTextField.clear();
        fromTextField.clear();
        maxDatePicker.setValue(null);
        minDatePicker.setValue(null);
        sortingChoiceBox.setValue(defaultSorting);
    }

    /**
     * Applies the default sorting: new to old emails.
     *
     * @param control - the class to delegate on to.
     * @author Hampus Jernkrook
     */
    private void applyDefaultSorting(Control control) {
        control.sortByNewToOld();
    }
}