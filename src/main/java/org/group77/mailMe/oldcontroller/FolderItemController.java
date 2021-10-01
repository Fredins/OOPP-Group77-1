package org.group77.mailMe.oldcontroller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import org.group77.mailMe.model.data.*;

import java.util.*;

public class FolderItemController {
  private Folder folder;
  private MainController parent;

  @FXML
  private Label label;

  void init(Folder folder, MainController parent) {
    this.folder = folder;
    this.parent = parent;
    label.setText(folder.name());
  }

  @FXML
  void viewEmails() {
    parent.loadEmails(List.of(folder.emails()));
  }
}
