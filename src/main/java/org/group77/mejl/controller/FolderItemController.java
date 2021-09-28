package org.group77.mejl.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import org.group77.mejl.model.Folder;

public class FolderItemController {
    private Folder folder;
    private MainController parent;

    @FXML
    private Label label;

    void init(Folder folder, MainController parent){
        this.folder = folder;
        this.parent = parent;
    }
}
