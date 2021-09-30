package org.group77.mailMe.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import org.group77.mailMe.model.Folder;

public class FolderItemController {
    private Folder folder;
    private MainController parent;

    @FXML
    private Label label;

    void init(Folder folder, MainController parent){
        this.folder = folder;
        this.parent = parent;
        label.setText(folder.getName());
    }

    @FXML
    void viewEmails(){
        parent.loadEmails(folder.getEmails());
    }
}
