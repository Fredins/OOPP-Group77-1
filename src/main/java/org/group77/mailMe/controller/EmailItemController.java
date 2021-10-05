package org.group77.mailMe.controller;

import javafx.fxml.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.util.*;
import org.group77.mailMe.model.*;
import org.group77.mailMe.model.data.*;

public class EmailItemController {
  @FXML private AnchorPane root;
  @FXML private Label fromLabel;
  @FXML private Label subjectLabel;


  void init(Model m, Email e) {
    fromLabel.setText(e.from());
    subjectLabel.setText(e.subject());

    // input handler
    root.setOnMouseClicked(i -> m.readingEmail.set(e));
  }
}
