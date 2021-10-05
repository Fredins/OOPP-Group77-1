package org.group77.mailMe.controller;

import javafx.fxml.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.*;
import org.group77.mailMe.*;
import org.group77.mailMe.controller.utils.*;
import org.group77.mailMe.model.*;
import org.group77.mailMe.model.data.*;

import java.io.*;
import java.util.*;

public class ReadingController {
  @FXML private TextArea contentArea;
  @FXML private Label fromLabel;
  @FXML private Label subjectLabel;
  @FXML private Label toLabel;
  @FXML private Label dateLabel;
  @FXML private Button replyButton;

  /**
   * 1. set initial values for nodes
   * 2. set event handler for node
   */
  void init(Model model, Email email) {
    contentArea.setText(email.content());
    fromLabel.setText(email.from());
    subjectLabel.setText(email.subject());
    toLabel.setText(Arrays.toString(email.to()));
    // TODO date

    replyButton.setOnAction(inputEvent -> WindowOpener.openReply(model, fromLabel.getText()));
  }
}
