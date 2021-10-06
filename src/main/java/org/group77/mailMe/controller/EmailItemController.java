package org.group77.mailMe.controller;

import javafx.beans.value.*;
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
  @FXML private Button button;

  /**
   * 1. set initial values for nodes
   * 2. set event handlers for nodes
   */
  void init(Model model, Email email) {
    fromLabel.setText(email.from());
    subjectLabel.setText(email.subject());

    // input handler
    button.setOnMouseClicked(inputEvent -> model.readingEmail.set(email));

    // change handler
    model.readingEmail.addListener((ChangeListener<? super Email>) (obs, oldEmail, newEmail) -> {
      if(email.equals(newEmail)){
        button.getStyleClass().add("dropshadow");
      }else{
        button.getStyleClass().remove("dropshadow");
      }
    });
  }
}
