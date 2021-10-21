package org.group77.mailMe.controller;

import javafx.beans.value.*;
import javafx.fxml.*;
import javafx.scene.control.*;
import org.group77.mailMe.Control;
import org.group77.mailMe.model.Folder;


public class FolderItemController {
    @FXML
    private Label nameLabel;
    @FXML
    private Button button;

    /**
     * 1. set initial values for nodes
     * 2. set event handlers for nodes
     *
     * @param control the control layer
     * @param folder  the corresponding folder
     * @author Martin Fredin
     */
    void init(Control control, Folder folder) {
        nameLabel.setText(folder.name());
        button.getStyleClass().add(folder.name().equals("Inbox") ? "focused" : "unfocused");

        // attach event handlers
        button.setOnMouseClicked(i -> control.getActiveFolder().set(folder));
        control.getActiveFolder().addObserver(newFolder -> focused(newFolder, folder));
    }

    /**
     * Toggles persistent focus using css style classes
     * @param newFolder the new folder
     * @param folder    the folder associated with this controller
     * @author Martin Fredin
     */
    private void focused(Folder newFolder, Folder folder) {
        button.getStyleClass().clear();
        button.getStyleClass().add(folder.equals(newFolder) ? "focused" : "unfocused");
    }
}
